package org.test.tasks;

import org.task.models.Balance;
import org.task.repositories.BalanceRepository;

import java.util.concurrent.Callable;

public class Task implements Callable<Balance> {

    private final TaskConditions taskConditions;
    private final BalanceRepository balanceRepository;

    public Task(TaskConditions taskConditions, BalanceRepository balanceRepository) {
        this.taskConditions = taskConditions;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Balance call() throws Exception {
        return balanceRepository.findCustomerBalanceByTime(taskConditions.getCustomerName(),
                taskConditions.getTime());
    }

    public TaskConditions getTaskConditions() {
        return taskConditions;
    }
}
