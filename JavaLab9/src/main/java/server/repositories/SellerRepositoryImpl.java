package server.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SellerRepositoryImpl implements SellerRepository {
    private static Connection connection;
    private static SellerRepositoryImpl sellerRepository;

    private final String SEARCH_COUNT = "SELECT count FROM products WHERE name = ?";
    private final String PRODUCT_IS_EXIST = "SELECT 1 FROM products WHERE name = ?";
    private final String UPDATE_PRODUCT = "UPDATE products SET count = ? WHERE name = ?";
    private final String ADD_PRODUCT = "INSERT INTO products (name, count) VALUES (?, ?)";

    private final String DELETE_PRODUCT = "DELETE FROM products WHERE name = ?";

    private JdbcTemplate jdbcTemplate;

    public SellerRepositoryImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    public Integer getCount(String name) {

        try {
            return jdbcTemplate.queryForObject(SEARCH_COUNT, new Object[]{name}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
           return null;
        }
    }

    private boolean productIsExist(String name) {
        try {
            jdbcTemplate.queryForObject(PRODUCT_IS_EXIST, new Object[]{name}, String.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void addProduct(String name, int count) {
        Integer number = getCount(name);
        if (number == null) {
            addNewProduct(name, count);
        } else if (number >= 0) {
            addOldProduct(name, count, number);
        } else {
            throw new IllegalStateException("Отрицательное количество товара");
        }
    }

    private void addOldProduct(String name, int count, int countNow) {
        try {
            int affectedRows = jdbcTemplate.update(UPDATE_PRODUCT, countNow + count, name);
            if (affectedRows == 0) {
                throw new IllegalStateException("ничего не обновилось");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void addNewProduct(String name, int count) {
        try {
            int affectedRows = jdbcTemplate.update(ADD_PRODUCT, name, count);
            if (affectedRows == 0) {
                throw new IllegalStateException("ничего не обновилось");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }


    private void deleteProductFromTable(String product) {
        try {
            int affectedRows = jdbcTemplate.update(DELETE_PRODUCT, product);
            if (affectedRows == 0) {
                throw new IllegalStateException("ничего не обновилось");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean deleteProduct(String name) {
        boolean isExist = productIsExist(name);

        if (isExist) {
            deleteProductFromTable(name);
            return true;
        } else {
            return false;
        }
    }
}
