package com.pedroghiotti.learning.data.dao;

import com.pedroghiotti.learning.data.entity.Service;
import com.pedroghiotti.learning.data.util.DatabaseUtils;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ServiceDao implements Dao<Service, UUID>{
    private static final Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());

    private static final String GET_ALL = "SELECT service_id, name, price FROM wisdom.services";
    private static final String GET_BY_ID = "SELECT service_id, name, price FROM wisdom.services WHERE service_id = ?";
    private static final String CREATE = "INSERT INTO wisdom.services (service_id, name, price) VALUES (?, ?, ?)";

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

        try(PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_ALL)) {
            ResultSet queryResultSet = statement.executeQuery();
            services = this.processResultSet(queryResultSet);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getAll", e, LOGGER);
        }

        return services;
    }

    @Override
    public Optional<Service> getById(UUID uuid) {
        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)){
            statement.setObject(1, uuid);

            ResultSet queryResultSet = statement.executeQuery();
            List<Service> services = this.processResultSet(queryResultSet);

            if(services.isEmpty()) {
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
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    private List<Service> processResultSet(ResultSet rs) throws SQLException {
        List<Service> services = new ArrayList<>();

        while(rs.next()) {
            Service service = new Service();

            service.setServiceId((UUID) rs.getObject("service_id"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getBigDecimal("price"));
            services.add(service);
        }

        return services;
    }
}
