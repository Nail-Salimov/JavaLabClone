package server.repositories;

import java.sql.ResultSet;

public interface RowMapper <T>{
    T mapRow(ResultSet row) throws Exception;
}
