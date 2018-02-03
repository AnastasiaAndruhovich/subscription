package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.AuthorDAO;
import by.andruhovich.subscription.dao.impl.AuthorPublicationDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.util.LinkedList;
import java.util.List;

public class AuthorService extends BaseService{
    private int findIdByAuthorName(String authorFirstName, String authorLastName, String publisherName)
            throws ServiceTechnicalException {
        Author author = new Author(publisherName, authorLastName, authorFirstName);
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorDAO authorDAO = null;

        try {
            authorDAO = daoFactory.createAuthorDAO();
            return authorDAO.findIdByEntity(author);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorDAO);
        }

    }

    public int addAuthor(String authorFirstName, String authorLastName, String publisherName)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorDAO authorDAO = null;
        Author author = new Author(publisherName, authorLastName ,authorFirstName);
        try {
            authorDAO = daoFactory.createAuthorDAO();
            return authorDAO.create(author);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorDAO);
        }
    }

    public List<Author> createAuthorList(List<String> authorFirstNames, List<String> authorLastNames,
                                         String publisherName) throws ServiceTechnicalException {
        List<Author> authors = new LinkedList<>();

        for (int i = 0; i < authorFirstNames.size(); i++) {
            int authorId = findIdByAuthorName(authorFirstNames.get(i), authorLastNames.get(i), publisherName);
            if (authorId == -1) {
                authorId = addAuthor(authorFirstNames.get(i), authorLastNames.get(i), publisherName);
            }
            Author author = new Author(authorId, publisherName, authorLastNames.get(i), authorFirstNames.get(i));
            authors.add(author);
        }
        return authors;
    }

    public boolean updateAuthor(String id, String authorLastName, String authorFirstName, String publisherName)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorDAO authorDAO = null;
        int intId = Integer.parseInt(id);
        Author author = new Author(intId, publisherName, authorLastName ,authorFirstName);

        try {
            authorDAO = daoFactory.createAuthorDAO();
            return authorDAO.update(author);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorDAO);
        }
    }

    public boolean deleteAuthor(String authorId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorPublicationDAO authorPublicationDAO = null;
        AuthorDAO authorDAO = null;
        int intAuthorId = Integer.parseInt(authorId);

        try {
            authorDAO = daoFactory.createAuthorDAO();
            /*authorPublicationDAO = daoFactory.createAuthorPublicationDAO();
            return authorPublicationDAO.deletePublicationsByAuthorId(intAuthorId);*/
            return authorDAO.delete(intAuthorId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorPublicationDAO);
            daoFactory.closeDAO(authorDAO);
        }
    }

    public int findAuthorPageCount() throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorDAO authorDAO = null;

        try {
            authorDAO = daoFactory.createAuthorDAO();
            int count = authorDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorDAO);
        }
    }

    public List<Author> showAuthors(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorDAO authorDAO = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            authorDAO = daoFactory.createAuthorDAO();
            return authorDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorDAO);
        }
    }
}
