package org.anic;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class QuartzDemo {

    public static void main (String[] args) {
        try {
            JobDetail job = newJob(LogJob.class)
                            .withIdentity("JobLogger")
                            .build();
            Trigger trigger = newTrigger()
                            .withIdentity("Trigger1", "Group1")
                            .startNow()
                            .withSchedule(simpleSchedule()
                                            .withIntervalInSeconds(40)
                                            .repeatForever())
                            .build();

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(job, trigger);

            scheduler.start();
            Thread.sleep(120000);
            scheduler.shutdown();
        } catch (InterruptedException e) {
            // ignore
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
