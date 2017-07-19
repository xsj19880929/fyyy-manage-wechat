package com.suryani.manage.schedule.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.booking.service.DateTimeService;

@Component
public class TaskExecutorExample {

    private class MessagePrinterTask implements Runnable {
        @Inject
        private DateTimeService dateTimeService;
        private String message;

        public MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
            System.out.println(message);
            List<DateTime> times = dateTimeService.list("", "");
            System.out.println(times);
        }

    }

    private TaskExecutor taskExecutor;

    public TaskExecutorExample(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void printMessages() {
        for (int i = 0; i < 25; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message" + i));
        }
    }
}
