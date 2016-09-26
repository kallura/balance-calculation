package org.task.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.task.models.BalanceStatistic;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class BalanceStatisticRepositoryImpl implements BalanceStatisticRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigDecimal findCustomerBalanceByMonthAndYear(int customerId, int month, int year) {
        return jdbcTemplate.query("SELECT \n" +
                        "    balance\n" +
                        "FROM\n" +
                        "    balance_statistic\n" +
                        "WHERE\n" +
                        "    customer_id = ? AND YEAR(time) = ?\n" +
                        "        AND MONTH(time) = ?;",
                new Object[]{customerId, year, month}, resultSet -> resultSet.next() ? resultSet.getBigDecimal(1): null);
    }

    @Override
    public List<BalanceStatistic> findCustomersBalancePerMonths() {
        return jdbcTemplate.query("SELECT \n" +
                "    income.customer_id,\n" +
                "    timestamp(concat(income.dt, '-01')) dt,\n" +
                "    income.amount - outcome.amount balance\n" +
                "FROM\n" +
                "    (SELECT \n" +
                "        receiver_id customer_id,\n" +
                "            DATE_FORMAT(time, '%Y-%m') dt,\n" +
                "            SUM(amount) amount\n" +
                "    FROM\n" +
                "        db.transaction_table t\n" +
                "    WHERE\n" +
                "        DATE_FORMAT(time, '%Y-%m') > COALESCE((SELECT \n" +
                "                MAX(DATE_FORMAT(time, '%Y-%m'))\n" +
                "            FROM\n" +
                "                balance_statistic b\n" +
                "            WHERE\n" +
                "                b.customer_id = t.receiver_id), '1900-01')\n" +
                "    GROUP BY receiver_id , dt) income,\n" +
                "    (SELECT \n" +
                "        sender_id customer_id,\n" +
                "            DATE_FORMAT(time, '%Y-%m') dt,\n" +
                "            SUM(amount) amount\n" +
                "    FROM\n" +
                "        db.transaction_table t\n" +
                "    WHERE\n" +
                "        DATE_FORMAT(time, '%Y-%m') > COALESCE((SELECT \n" +
                "                MAX(DATE_FORMAT(time, '%Y-%m'))\n" +
                "            FROM\n" +
                "                balance_statistic b\n" +
                "            WHERE\n" +
                "                b.customer_id = t.sender_id), '1900-01')\n" +
                "    GROUP BY sender_id , dt) outcome\n" +
                "WHERE\n" +
                "    income.customer_id = outcome.customer_id\n" +
                "        AND income.dt = outcome.dt;", (resultSet, i) -> {
            BalanceStatistic statistic = new BalanceStatistic();
            statistic.setCustomerId(resultSet.getInt("customer_id"));
            statistic.setBalance(resultSet.getBigDecimal("balance"));
            statistic.setTime(resultSet.getTimestamp("dt"));
            return statistic;
        });
    }

    @Override
    public void saveCustomersBalancePerMonths(List<BalanceStatistic> balanceStatistics) {
        jdbcTemplate.batchUpdate("INSERT INTO balance_statistic (customer_id, balance, time) \n" +
                        "VALUES (?, ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BalanceStatistic customer = balanceStatistics.get(i);
                        ps.setLong(1, customer.getCustomerId());
                        ps.setBigDecimal(2, customer.getBalance());
                        ps.setTimestamp(3, customer.getTime());
                    }

                    @Override
                    public int getBatchSize() {
                        return balanceStatistics.size();
                    }
                });
    }
}
