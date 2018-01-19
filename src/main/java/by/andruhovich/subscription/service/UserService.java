package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.AccountDAO;
import by.andruhovich.subscription.dao.impl.BlockDAO;
import by.andruhovich.subscription.dao.impl.PaymentDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.coder.PasswordCoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserService {
    private static final String USER = "User";
    private static final String ADMIN = "Admin";

    public boolean checkLoginByPassword(String login, String password) throws ServiceTechnicalException {
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            UserDAO userDAO = daoFactory.createUserDAO();
            String dataBasePassword = userDAO.findPasswordByLogin(login);
            String hashedPassword = PasswordCoder.hashPassword(password);
            return hashedPassword.equals(dataBasePassword);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        }
    }

    public boolean isLoginExist(String login) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            UserDAO userDAO = daoFactory.createUserDAO();
            return userDAO.isLoginExist(login);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        }
    }

    public boolean signUp(String lastname, String firstname, Date birthdate, String address, String city,
                          String postalIndex, String login, String password) throws ServiceTechnicalException {
        try {
            if (!isLoginExist(login)) {
                DAOFactory daoFactory = DAOFactory.getInstance();
                UserDAO userDAO = daoFactory.createUserDAO();
                AccountDAO accountDAO = daoFactory.createAccountDAO();
                int intPostalIndex = Integer.parseInt(postalIndex);
                password = PasswordCoder.hashPassword(password);
                Account account = accountDAO.createEmptyAccount();
                User user = new User(lastname, firstname, birthdate, address, city, intPostalIndex, login, password, account);
                userDAO.create(user);
                return true;
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        }
    }

    public boolean blockUser(String userLogin, String adminLogin) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            if (isLoginExist(userLogin)) {
                UserDAO userDAO = daoFactory.createUserDAO();
                User user = userDAO.findUserByLogin(userLogin);
                User admin = userDAO.findUserByLogin(adminLogin);
                Date date = Calendar.getInstance().getTime();
                Block block = new Block(user, admin, date);
                BlockDAO blockDAO = daoFactory.createBlockDAO();
                blockDAO.create(block);
                return true;
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        }
    }

    public boolean unblockUser(String login) throws ServiceTechnicalException {
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            if (isLoginExist(login)) {
                UserDAO userDAO = daoFactory.createUserDAO();
                User user = userDAO.findUserByLogin(login);
                BlockDAO blockDAO = daoFactory.createBlockDAO();
                return blockDAO.deleteBlockByUserId(user.getUserId());
            }
            return true;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public boolean updateUser(String lastname, String firstname, Date birthdate, String address, String city,
                              String postalIndex, String login, String password) throws ServiceTechnicalException {
        try {
            if (isLoginExist(login)) {
                DAOFactory daoFactory = DAOFactory.getInstance();
                UserDAO userDAO = daoFactory.createUserDAO();
                int userId = userDAO.findUserByLogin(login).getUserId();
                Account account = userDAO.findAccountByUserId(userId);
                int intPostalIndex = Integer.parseInt(postalIndex);
                password = PasswordCoder.hashPassword(password);
                User user = new User(lastname, firstname, birthdate, address, city, intPostalIndex, login, password, account);
                return userDAO.update(user);
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public List<User> showUsers() throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            UserDAO userDAO = daoFactory.createUserDAO();
            return userDAO.findAll();
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public User findUserByLogin(String login) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            UserDAO userDAO = daoFactory.createUserDAO();
            return userDAO.findUserByLogin(login);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public User findUserByAccountNumber(String accountNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        int intAccountNumber = Integer.parseInt(accountNumber);
        try {
            UserDAO userDAO = daoFactory.createUserDAO();
            return userDAO.findUserByAccountNumber(intAccountNumber);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public User findUserBySubscriptionId(String id) throws ServiceTechnicalException {
        int intId = Integer.parseInt(id);
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            UserDAO userDAO = daoFactory.createUserDAO();
            return userDAO.findUserBySubscriptionId(intId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public User findUserByPaymentNumber(String paymentNumber) throws ServiceTechnicalException {
        int intPaymentNumber = Integer.parseInt(paymentNumber);
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            PaymentDAO paymentDAO = daoFactory.createPaymentDAO();
            Subscription subscription = paymentDAO.findSubscriptionByPaymentNumber(intPaymentNumber);
            if (subscription != null) {
                UserDAO userDAO = daoFactory.createUserDAO();
                return userDAO.findUserBySubscriptionId(subscription.getSubscriptionId());
            }
            return null;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }
}
