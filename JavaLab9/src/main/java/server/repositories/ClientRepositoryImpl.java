package server.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientRepositoryImpl implements ClientRepository {

    private final String SEARCH_COUNT = "SELECT count FROM products WHERE name = ?";
    private final String PRODUCT_IS_EXIST = "SELECT 1 FROM products WHERE name = ?";
    private final String UPDATE_PRODUCT = "UPDATE products SET count = ? WHERE name = ?";
    private final String FIND_ALL_PRODUCTS = "SELECT * FROM products";

    private final String DELETE_PRODUCT = "DELETE FROM products WHERE name = ?";

    private JdbcTemplate jdbcTemplate;

    public ClientRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
            jdbcTemplate.queryForObject(SEARCH_COUNT, new Object[]{name}, String.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    @Override
    public boolean buyProduct(String name, int count) {
        Integer countNow = getCount(name);
        if (countNow == null) {
            return false;

        } else if (countNow < count) {
            return false;
        } else {
            int state = jdbcTemplate.update(UPDATE_PRODUCT, countNow - count, name);

            if (state == 0) {
                throw new IllegalStateException("ничего не обновилось");
            }

            if (countNow == count){
                deleteProduct(name);
            }
            return true;
        }
    }

    @Override
    public Map<Object, Object> listProduct() {
        Map<String, Integer> map = new HashMap<>();
        try {
            List<Map<String, Object>> mapList  = jdbcTemplate.queryForList(FIND_ALL_PRODUCTS);

            return mapList.stream().collect(Collectors.toMap(k -> (String) k.get("name"), k -> (Integer) k.get("count")));
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void deleteProduct(String product) {
        try {
            jdbcTemplate.update(DELETE_PRODUCT, product);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(e);
        }
    }


}
