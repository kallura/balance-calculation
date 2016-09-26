package org.task.repositories.initializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.task.schedulers.ScheduledBalanceStatistic;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DbInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ScheduledBalanceStatistic scheduledBalanceStatistic;

    public void insertData() {
        jdbcTemplate.update("insert into customers (id, customer_name) values (1, 'Kseniia O')");
        jdbcTemplate.update("insert into customers (id, customer_name) values (2, 'Ciklum')");
        jdbcTemplate.batchUpdate("insert into transactions (time, amount, sender_Id, receiver_id) values (?, ?, ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()
                                .minusYears(3)
                                .plusDays(i)
                                .plusHours(i)));
                        ps.setBigDecimal(2, new BigDecimal(1000 + i * 5));
                        ps.setInt(3, i % 2 == 0 ? 1 : 2);
                        ps.setInt(4, i % 2 != 0 ? 1 : 2);
                    }

                    @Override
                    public int getBatchSize() {
                        return 600;
                    }
                });
    }

    @PostConstruct
    public void initDB() {
        insertData();
        scheduledBalanceStatistic.executeBalanceCalculationJob();
    }
}
