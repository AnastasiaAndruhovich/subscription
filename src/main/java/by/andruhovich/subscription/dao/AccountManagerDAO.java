package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.Connection;

public abstract class AccountManagerDAO  extends MediatorManagerDAO <Account>{
    public AccountManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract Account createEmptyAccount() throws DAOTechnicalException;

    public abstract BigDecimal findBalanceById(int accountNumber) throws DAOTechnicalException;
    public abstract BigDecimal findLoanById(int accountNumber) throws DAOTechnicalException;

    public abstract BigDecimal recharge(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
    public abstract BigDecimal takeLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
    public abstract BigDecimal repayLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
    public abstract BigDecimal withdraw(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
}
