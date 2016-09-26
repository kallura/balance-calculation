package org.task.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.task.models.Balance;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BalanceRepositoryImpl implements BalanceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BalanceStatisticRepository balanceStatisticRepository;

    @Override
    public Balance findCustomerBalanceByTime(String customerName, LocalDateTime time) {
        int customerId = customerRepository.findCustomerIdByName(customerName);
        BigDecimal lastMonthBalance = balanceStatisticRepository.findCustomerBalanceByMonthAndYear(customerId,
                time.minusMonths(1).getMonthValue(), time.minusMonths(1).getYear());
        int month = time.getMonthValue();
        int year = time.getYear();
        if (lastMonthBalance != null) {
            LocalDateTime localDateTime = time.minusMonths(1);
            month = localDateTime.getMonthValue();
            year = localDateTime.getYear();
        }
        BigDecimal currentMonthBalance = jdbcTemplate
                .query("SELECT \n" +
                                "    SUM(amount) - (SELECT \n" +
                                "            SUM(amount)\n" +
                                "        FROM\n" +
                                "            transactions\n" +
                                "        WHERE\n" +
                                "            sender_Id = ? AND YEAR(time) = ?\n" +
                                "                AND MONTH(time) > ?\n" +
                                "                AND time <= ?) AS balance\n" +
                                "FROM\n" +
                                "    transactions\n" +
                                "WHERE\n" +
                                "    receiver_id = ? AND YEAR(time) = ?\n" +
                                "        AND MONTH(time) > ?\n" +
                                "        AND time <= ?;",
                        new Object[]{customerId, year, month, Timestamp.valueOf(time),
                                customerId, year, month, Timestamp.valueOf(time)}, resultSet -> resultSet.next() ? resultSet.getBigDecimal(1): null);
        Balance balance = new Balance();
        balance.setCustomerName(customerName);
        balance.setTime(time);
        if (lastMonthBalance != null && currentMonthBalance != null) {
            balance.setBalance(lastMonthBalance.add(currentMonthBalance));
        } else if (lastMonthBalance != null) {
            balance.setBalance(lastMonthBalance);
        } else {
            balance.setBalance(currentMonthBalance);
        }
        return balance;
    }
}
