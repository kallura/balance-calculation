package org.task.repositories;


import org.task.models.Balance;

import java.time.LocalDateTime;

public interface BalanceRepository {

    Balance findCustomerBalanceByTime(String customerName, LocalDateTime time);
}
