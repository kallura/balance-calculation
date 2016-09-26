package org.task.repositories;

import org.task.models.BalanceStatistic;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceStatisticRepository {

    BigDecimal findCustomerBalanceByMonthAndYear(int customerId, int month, int year);

    List<BalanceStatistic> findCustomersBalancePerMonths();

    void saveCustomersBalancePerMonths(List<BalanceStatistic>  balanceStatistics);
}
