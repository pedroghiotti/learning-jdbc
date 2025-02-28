package com.pedroghiotti.learning.data.dao;

import com.pedroghiotti.learning.data.entity.Customer;
import com.pedroghiotti.learning.data.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class CustomerDao implements Dao<Customer, UUID> {
    private static final Logger LOGGER = Logger.getLogger(CustomerDao.class.getName());

    private static final String GET_ALL = "SELECT customer_id, first_name, last_name, email, phone, address FROM wisdom.customers";
    private static final String GET_BY_ID = "SELECT customer_id, first_name, last_name, email, phone, address FROM wisdom.customers WHERE customer_id = ?";
    private static final String CREATE = "INSERT INTO wisdom.customers (customer_id, first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE wisdom.customers SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";
    private static final String DELETE = "DELETE FROM wisdom.customers WHERE customer_id = ?";

    @Override
    public Customer create(Customer entity) {
        Connection connection = DatabaseUtils.getConnection();

        UUID customerId = UUID.randomUUID();

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setObject(1, customerId);
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPhone());
            statement.setString(6, entity.getAddress());
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException createException) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                DatabaseUtils.handleSqlException("CustomerDao.create.rollback", rollbackException, LOGGER);
            }
            DatabaseUtils.handleSqlException("CustomerDao.create", createException, LOGGER);
        }

        Optional<Customer> customer = this.getById(customerId);

        return customer.orElse(null);
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_ALL)) {
            ResultSet queryResultSet = statement.executeQuery();
            customers = this.processResultSet(queryResultSet);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("CustomerDao.getAll", e, LOGGER);
        }

        return customers;
    }

    @Override
    public Optional<Customer> getById(UUID uuid) {
        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setObject(1, uuid);

            ResultSet queryResultSet = statement.executeQuery();
            List<Customer> customers = this.processResultSet(queryResultSet);

            if (customers.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(customers.get(0));
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("CustomerDao.getAll", e, LOGGER);
        }

        return Optional.empty();
    }

    @Override
    public Customer update(Customer entity) {
        UUID customerId = entity.getCustomerId();

        Connection connection = DatabaseUtils.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPhone());
            statement.setString(5, entity.getAddress());
            statement.setObject(6, customerId);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (SQLException updateException) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                DatabaseUtils.handleSqlException("CustomerDao.update.rollback", rollbackException, LOGGER);
            }
            DatabaseUtils.handleSqlException("CustomerDao.update", updateException, LOGGER);
        }

        return this.getById(customerId).get();
    }

    @Override
    public void delete(UUID uuid) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setObject(1, uuid);
            statement.executeUpdate();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("CustomerDao.delete", e, LOGGER);
        }
    }

    private List<Customer> processResultSet(ResultSet rs) throws SQLException {
        List<Customer> customers = new ArrayList<>();

        while (rs.next()) {
            Customer customer = new Customer();

            customer.setCustomerId((UUID) rs.getObject("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));
            customers.add(customer);
        }

        return customers;
    }
}
