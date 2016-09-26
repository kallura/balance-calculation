package org.task.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.task.config.RepositoriesConfig;
import org.task.models.Balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoriesConfig.class)
public class BalanceRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BalanceRepository balanceRepository;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate.execute("insert into customers (id, customer_name) values (1, 'customer1')");
        jdbcTemplate.execute("insert into customers (id, customer_name) values (2, 'customer2')");
        jdbcTemplate.execute("insert into balance_statistic (customer_id, balance, time) values (1, 5000, '2016-09-30 23:59:00')");
        jdbcTemplate.execute("insert into transaction_table (time, amount, sender_id, receiver_id) values ('2016-10-01 19:07:11', 500, 1, 2)");
        jdbcTemplate.execute("insert into transaction_table (time, amount, sender_id, receiver_id) values ('2016-10-01 20:07:11', 500, 1, 2)");
        jdbcTemplate.execute("insert into transaction_table (time, amount, sender_id, receiver_id) values ('2016-10-01 20:07:11', 1500, 2, 1)");
    }

    @Test
    public void customerBalanceForTime() throws Exception {
        LocalDateTime time = LocalDateTime.of(2016, 10, 2, 12, 0);
        Balance balance = balanceRepository.findCustomerBalanceByTime("customer1", time);
        assertNotNull(balance);
        assertEquals(new BigDecimal(5500), balance.getBalance());
        assertEquals("customer1", balance.getCustomerName());
        assertEquals("customer1", balance.getCustomerName());
        assertEquals(time, balance.getTime());
    }
}