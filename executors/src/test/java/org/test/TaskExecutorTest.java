package org.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.task.models.Balance;
import org.task.repositories.BalanceRepository;
import org.test.context.BaseTestContext;
import org.test.exceptions.NotFoundTaskException;
import org.test.exceptions.TimeoutTaskException;
import org.test.tasks.Task;
import org.test.tasks.TaskConditions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CancellationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BaseTestContext.class)
public class TaskExecutorTest {

    @Autowired
    private TaskExecutor taskExecutor;

    private Balance balance;
    private TaskConditions taskConditions;

    @Before
    public void setUp() {
        final LocalDateTime now = LocalDateTime.now();
        taskConditions = new TaskConditions();
        taskConditions.setCustomerName("customer");
        taskConditions.setTime(now);
        taskConditions.setTimeout(1000);
        balance = new Balance();
        balance.setCustomerName("customer");
        balance.setBalance(new BigDecimal(1000));
        balance.setTime(now);
    }

    @Test
    public void execute() throws Exception {
        BalanceRepository repository = Mockito.mock(BalanceRepository.class);
        Mockito.when(repository.findCustomerBalanceByTime(taskConditions.getCustomerName(), taskConditions.getTime()))
                .thenAnswer(invocationOnMock -> balance);
        Task task = new Task(taskConditions, repository);
        String taskId = taskExecutor.execute(task);
        Balance balance = taskExecutor.get(taskId);
        assertNotNull(balance);
        assertEquals(taskConditions.getCustomerName(), balance.getCustomerName());
        assertEquals(taskConditions.getTime(), balance.getTime());
        assertEquals(new BigDecimal(1000), balance.getBalance());
    }


    @Test(expected = NotFoundTaskException.class)
    public void obtainResultOfCanceledTask() throws Exception {
        taskExecutor.get("notExist");
    }

    @Test(expected = NotFoundTaskException.class)
    public void obtainResultCancelOfCancelByUser() throws Exception {
        BalanceRepository repository = Mockito.mock(BalanceRepository.class);
        Mockito.when(repository.findCustomerBalanceByTime(taskConditions.getCustomerName(), taskConditions.getTime()))
                .thenAnswer(invocationOnMock -> {
                    Thread.sleep(500);
                    return balance;
                });
        Task task = new Task(taskConditions, repository);
        String taskId = taskExecutor.execute(task);
        taskExecutor.cancel(taskId);
        taskExecutor.get(taskId);
    }

    @Test(expected = CancellationException.class)
    public void obtainResultCancelOfCancelByTimeout() throws Exception {
        BalanceRepository repository = Mockito.mock(BalanceRepository.class);
        Mockito.when(repository.findCustomerBalanceByTime(taskConditions.getCustomerName(), taskConditions.getTime()))
                .thenAnswer(invocationOnMock -> {
                    Thread.sleep(1300);
                    return balance;
                });
        Task task = new Task(taskConditions, repository);
        String taskId = taskExecutor.execute(task);
        Thread.sleep(2000);
        taskExecutor.get(taskId);
    }

}