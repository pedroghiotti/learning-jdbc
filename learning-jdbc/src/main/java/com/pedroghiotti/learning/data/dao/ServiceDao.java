package com.pedroghiotti.learning.data.dao;

import com.pedroghiotti.learning.data.entity.Service;
import com.pedroghiotti.learning.data.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ServiceDao implements Dao<Service, UUID>{
    private static final Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());

    private static String GET_ALL = "SELECT service_id, name, price FROM wisdom.services";

    @Override
    public Service create(Service entity) {
        return null;
    }

    @Override
    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();

        Connection connection = DatabaseUtils.getConnection();
        try(Statement statement = connection.createStatement()) {
            ResultSet queryResultSet = statement.executeQuery(GET_ALL);
            services = this.processResultSet(queryResultSet);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getAll", e, LOGGER);
        }

        return services;
    }

    @Override
    public Optional<Service> getById(UUID uuid) {
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
