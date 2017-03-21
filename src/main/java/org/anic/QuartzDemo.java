package org.anic;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class QuartzDemo {

    static Logger log = LoggerFactory.getLogger(QuartzDemo.class);

    static JobDetail createLogJob (int id) {
        return newJob(LogJob.class)
                .withIdentity("JobLogger" + (id + 1), "*")
                .build();
    }

    static Trigger createIntervalTrigger (int id, int interval) {
        return newTrigger()
                .withIdentity("Trigger" + (id + 1), "*")
                .startNow()
                .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(interval)
                                .repeatForever())
                .build();
    }

    public static void main (String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            for (int j = 0; j < 5; ++j) {
                JobDetail job = createLogJob(j);
                Trigger trigger = createIntervalTrigger(j, 35);
                scheduler.scheduleJob(job, trigger);
            }

            log.info("start scheduling");
            scheduler.start();
            Thread.sleep(120000);
            log.info("shutting down");
            scheduler.shutdown(true);
        } catch (InterruptedException e) {
            log.info("InterruptedException", e);
        } catch (SchedulerException e) {
            log.info("SchedulerException", e);
        }
        log.info("bye");
    }

}
