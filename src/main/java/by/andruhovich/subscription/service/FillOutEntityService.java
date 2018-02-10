package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.*;
import by.andruhovich.subscription.dao.impl.*;
import by.andruhovich.subscription.entity.*;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class FillOutEntityService {
    static List<Publication> fillOutPublicationList(List<Publication> publications) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            for (Publication publication : publications) {
                publication.setGenre(publicationManagerDAO.findGenreByPublicationId(publication.getPublicationId()));
                publication.setPublicationType(publicationManagerDAO.findPublicationTypeByPublicationId(publication.getPublicationId()));
                List<Author> authors = authorPublicationManagerDAO.findAuthorsByPublicationId(publication.getPublicationId());
                publication.setAuthors(correctAuthorList(authors));
            }
            return publications;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    static List<Subscription> fillOutSubscriptionList(List<Subscription> subscriptions) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        List<Publication> publications = new LinkedList<>();

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            for (Subscription subscription : subscriptions) {
                subscription.setUser(subscriptionManagerDAO.findUserBySubscriptionId(subscription.getSubscriptionId()));
                Publication publication = publicationManagerDAO.findPublicationBySubscriptionId(subscription.getSubscriptionId());
                publications.add(publication);
                subscription.setPublication(fillOutPublicationList(publications).get(0));
                checkSubscriptionActive(subscription);
            }
            return subscriptions;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    static List<User> fillOutUserList(List<User> users) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int entityCount;

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            BlockManagerDAO blockManagerDAO = new BlockDAO(connection);
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            for (User user : users) {
                user.setAccount(userManagerDAO.findAccountByUserId(user.getUserId()));
                user.setRole(userManagerDAO.findRoleByUserId(user.getUserId()));
                entityCount = subscriptionManagerDAO.findSubscriptionCountByUserId(user.getUserId());
                user.setSubscriptions(subscriptionManagerDAO.findSubscriptionsByUserId(user.getUserId(), 0, entityCount));
                user.setAdmin(blockManagerDAO.findAdminByUserId(user.getUserId()));
                entityCount = blockManagerDAO.findBlockedUsersCountByAdminId(user.getUserId());
                user.setUsers(blockManagerDAO.findUsersByAdminId(user.getUserId(), 0, entityCount));
            }
            return users;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    static List<Payment> fillOutPaymentList(List<Payment> payments) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PaymentManagerDAO paymentManagerDAO = new PaymentDAO(connection);
            for (Payment payment : payments) {
                Subscription subscription = paymentManagerDAO.findSubscriptionByPaymentNumber(payment.getPaymentNumber());
                LinkedList<Subscription> subscriptions = new LinkedList<>();
                subscriptions.add(subscription);
                subscription = fillOutSubscriptionList(subscriptions).get(0);
                payment.setSubscription(subscription);
            }
            return payments;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    private static void checkSubscriptionActive(Subscription subscription) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Date date = Calendar.getInstance().getTime();

        if (date.getTime() > subscription.getEndDate().getTime()) {
            try {
                connection = connectionFactory.getConnection();
                SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
                subscription.setSubscriptionIsActive(false);
                subscriptionManagerDAO.update(subscription);
            } catch (DAOTechnicalException | ConnectionTechnicalException e) {
                throw new ServiceTechnicalException(e);
            } finally {
                connectionFactory.returnConnection(connection);
            }
        }
    }

    private static List<Author> correctAuthorList(List<Author> authors) {
        for (Author author : authors) {
            if ("-".equals(author.getAuthorFirstName())) {
                author.setAuthorFirstName(null);
            }
            if ("-".equals(author.getAuthorLastName())) {
                author.setAuthorLastName(null);
            }
        }
        return authors;
    }
}
