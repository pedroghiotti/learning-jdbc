package com.pedroghiotti.learning.data.dao;

import com.pedroghiotti.learning.data.util.DatabaseUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class SimpleProductDao {
    private static final Logger LOGGER = Logger.getLogger(SimpleProductDao.class.getName());
    private static final String CREATE = "SELECT * FROM createproduct(?, ?, ?)";

    public UUID create(String name, BigDecimal price, String vendorName) {
        Connection connection = DatabaseUtils.getConnection();
        UUID returnVal = null;

        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setString(1, name);
            statement.setBigDecimal(2, price);
            statement.setString(3, vendorName);
            ResultSet queryResultSet = statement.executeQuery();
            while (queryResultSet.next()) {
                returnVal = (UUID) queryResultSet.getObject("createproduct");
            }
            connection.commit();
            queryResultSet.close();
            statement.close();
        } catch (SQLException createException) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                DatabaseUtils.handleSqlException("SimpleProductDao.create.rollback", rollbackException, LOGGER);
            }
            DatabaseUtils.handleSqlException("SimpleProductDao.create", createException, LOGGER);
        }

        return returnVal;
    }
}
