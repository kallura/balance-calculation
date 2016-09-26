package org.task.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.task.models.BalanceStatistic;
import org.task.repositories.BalanceStatisticRepository;

import java.util.List;

public class ScheduledBalanceStatistic {

    @Autowired
    private BalanceStatisticRepository balanceStatisticRepository;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void executeBalanceCalculationJob() {
        List<BalanceStatistic> balanceStatistics = balanceStatisticRepository.findCustomersBalancePerMonths();
        balanceStatisticRepository.saveCustomersBalancePerMonths(balanceStatistics);
    }
}
