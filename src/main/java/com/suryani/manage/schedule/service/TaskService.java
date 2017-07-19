package com.suryani.manage.schedule.service;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.util.Constants;

@Service
public class TaskService {
    @Inject
    private BookingService bookingService;
    @Inject
    private BookingDoctorService bookingDoctorService;
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public void getTask() {
        List<Booking> taskList = bookingService.listAfterOneWeek();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        if (taskList != null && !taskList.isEmpty()) {
            for (Booking task : taskList) {
                final JSONObject jsonTask = new JSONObject();
                jsonTask.put(Constants.DEPT_NAME, task.getDeptName());
                jsonTask.put(Constants.TRIAGE_NO, task.getTriageNo());
                jsonTask.put(Constants.DOCTOR, task.getDoctor());
                jsonTask.put(Constants.DOCTOR_ID, task.getDoctorId());
                jsonTask.put(Constants.SelectDate, sdf.format(task.getSelectDate()));
                jsonTask.put(Constants.SelectTime, task.getSelectTime());
                jsonTask.put(Constants.icardid, task.getIcardid());
                jsonTask.put(Constants.username, task.getUsername());
                jsonTask.put(Constants.phone, task.getPhone());
                jsonTask.put(Constants.TIME_DESC, task.getTimeDesc());
                jsonTask.put(Constants.ID, task.getId());
                SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
                RunTaskExecutor tmp = new RunTaskExecutor(taskExecutor);
                tmp.doIt(jsonTask);

            }
            logger.info("启动任务数=======" + taskList.size());
        }
    }

}
