package org.task.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int findCustomerIdByName(String name) {
        return jdbcTemplate.queryForObject("SELECT \n" +
                        "    id\n" +
                        "FROM\n" +
                        "    customers\n" +
                        "WHERE\n" +
                        "    customer_name = ?;",
                Integer.class, name);
    }
}
