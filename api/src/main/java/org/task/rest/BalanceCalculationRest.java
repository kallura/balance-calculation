package org.task.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.task.models.Balance;
import org.task.repositories.BalanceRepository;
import org.test.exceptions.NotFoundTaskException;
import org.test.exceptions.OverloadException;
import org.test.exceptions.TimeoutTaskException;
import org.test.tasks.Task;
import org.test.TaskExecutor;
import org.test.tasks.TaskConditions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController("/")
public class BalanceCalculationRest {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    BalanceRepository balanceRepository;

    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    public String calculateBalance(@RequestBody TaskConditions filter) throws OverloadException {
        return taskExecutor.execute(new Task(filter, balanceRepository));
    }

    @RequestMapping(value = "/balance/{taskId}", method = RequestMethod.GET)
    public @ResponseBody Balance get(@PathVariable String taskId) throws InterruptedException,
            ExecutionException, TimeoutException, TimeoutTaskException, NotFoundTaskException {
        return taskExecutor.get(taskId);
    }

    @RequestMapping(value = "/balance/cancel/{taskId}", method = RequestMethod.PUT)
    public void cancel(@PathVariable String taskId) throws NotFoundTaskException {
        taskExecutor.cancel(taskId);
    }
}
