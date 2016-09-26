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

@RestController
@RequestMapping("/balance")
public class BalanceCalculationRest {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private BalanceRepository balanceRepository;

    @RequestMapping(method = RequestMethod.POST)
    public String calculateBalance(@RequestBody TaskConditions conditions) throws OverloadException {
        return taskExecutor.execute(new Task(conditions, balanceRepository));
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public @ResponseBody Balance get(@PathVariable String taskId) throws InterruptedException,
            ExecutionException, TimeoutException, TimeoutTaskException, NotFoundTaskException {
        return taskExecutor.get(taskId);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    public void cancel(@PathVariable String taskId) throws NotFoundTaskException {
        taskExecutor.cancel(taskId);
    }
}
