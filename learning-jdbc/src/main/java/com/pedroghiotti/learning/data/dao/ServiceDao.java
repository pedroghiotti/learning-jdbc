package com.pedroghiotti.learning.data.dao;

import com.pedroghiotti.learning.data.entity.Service;
import com.pedroghiotti.learning.data.util.DatabaseUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ServiceDao implements Dao<Service, UUID> {
    private static final Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());

    private static final String GET_ALL = "SELECT service_id, name, price FROM wisdom.services";
    private static final String GET_ALL_LIMIT = "SELECT service_id, name, price FROM wisdom.services LIMIT ?";
    private static final String GET_BY_ID = "SELECT service_id, name, price FROM wisdom.services WHERE service_id = ?";
    private static final String CREATE = "INSERT INTO wisdom.services (service_id, name, price) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE wisdom.services SET name = ?, price = ? WHERE service_id = ?";
    private static final String DELETE = "DELETE FROM wisdom.services WHERE service_id = ?";

    @Override
    public Service create(Service entity) {
        UUID serviceId = UUID.randomUUID();
        String serviceName = entity.getName();
        BigDecimal servicePrice = entity.getPrice();

        Connection connection = DatabaseUtils.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setObject(1, serviceId);
            statement.setString(2, serviceName);
            statement.setBigDecimal(3, servicePrice);
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException createException) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                DatabaseUtils.handleSqlException("ServiceDao.create.rollback", rollbackException, LOGGER);
            }
            DatabaseUtils.handleSqlException("ServiceDao.create", createException, LOGGER);
        }

        Optional<Service> service = this.getById(serviceId);

        return service.orElse(null);
    }

    @Override
    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();

        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_ALL)) {
            ResultSet queryResultSet = statement.executeQuery();
            services = this.processResultSet(queryResultSet);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getAll", e, LOGGER);
        }

        return services;
    }

    public List<Service> getAll(int limit) {
        List<Service> services = new ArrayList<>();
        
        if (limit <= 0) {
            return services;
        }

        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_ALL_LIMIT)) {
            statement.setInt(1, limit);
            ResultSet queryResultSet = statement.executeQuery();
            services = this.processResultSet(queryResultSet);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getAllLimit", e, LOGGER);
        }

        return services;
    }

    @Override
    public Optional<Service> getById(UUID uuid) {
        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setObject(1, uuid);

            ResultSet queryResultSet = statement.executeQuery();
            List<Service> services = this.processResultSet(queryResultSet);

            if (services.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(services.get(0));
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getAll", e, LOGGER);
        }

        return Optional.empty();
    }

    @Override
    public Service update(Service entity) {
        UUID serviceId = entity.getServiceId();
        String serviceName = entity.getName();
        BigDecimal servicePrice = entity.getPrice();

        Connection connection = DatabaseUtils.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, serviceName);
            statement.setBigDecimal(2, servicePrice);
            statement.setObject(3, serviceId);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (SQLException updateException) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                DatabaseUtils.handleSqlException("ServiceDao.update.rollback", rollbackException, LOGGER);
            }
            DatabaseUtils.handleSqlException("ServiceDao.update", updateException, LOGGER);
        }

        return this.getById(serviceId).get();
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
            DatabaseUtils.handleSqlException("ServiceDao.delete", e, LOGGER);
        }
    }

    private List<Service> processResultSet(ResultSet rs) throws SQLException {
        List<Service> services = new ArrayList<>();

        while (rs.next()) {
            Service service = new Service();

            service.setServiceId((UUID) rs.getObject("service_id"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getBigDecimal("price"));
            services.add(service);
        }

        return services;
    }
}
