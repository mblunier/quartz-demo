package org.anic;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import static org.anic.Demo.getLog;
import static org.anic.Demo.sleep;
import static org.anic.Demo.triggerInfo;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class QuartzDemo {

    static Logger log = getLog(QuartzDemo.class);

    static JobDetail createLogJob (int id) {
        return newJob(LogJob.class)
                .withIdentity("LoggerJob" + (id + 1), "*")
                .build();
    }

    static Trigger createIntervalTrigger (int id, int interval) {
        return newTrigger()
                .withIdentity("Trigger" + (id + 1), "*")
                .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(interval)
                                .repeatForever())
                .startNow()
                .build();
    }

    static Trigger createTimezoneTrigger (int id, String timezone) {
        return newTrigger()
                .withIdentity("Trigger" + (id + 1), "*")
                .withSchedule(cronSchedule("0 3/3 * * * ?")
                                .inTimeZone(TimeZone.getTimeZone(timezone)))
                .startAt(new Date(System.currentTimeMillis() + 30000))
                .build();
    }

    private static void logStatus (Scheduler scheduler) {
        try {
            log.info("Scheduler " + scheduler.getSchedulerName());
            log.info("Instance: " + scheduler.getSchedulerInstanceId());
            SchedulerMetaData meta = scheduler.getMetaData();
            log.info("Jobs executed: " + meta.getNumberOfJobsExecuted());
            Set<TriggerKey> triggers = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
            log.info(triggers.size() + " triggers:");
            for (TriggerKey key : triggers) {
                log.info(triggerInfo(scheduler, key));
            }
        } catch (SchedulerException e) {
            log.warn("SchedulerException", e);
        }
    }

    public static void main (String[] args) {

        Scheduler scheduler = null;

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.getListenerManager().addJobListener(new LogJobListener());
            scheduler.getListenerManager().addTriggerListener(new DemoTriggerListener());
            log.info("Scheduler initialized: " + scheduler.getMetaData().getSummary());
        } catch (SchedulerException e) {
            log.warn("SchedulerException during initialization", e);
        }

        if (scheduler == null) {
            log.error("aborting...");
            return;
        }

        try {
            for (int j = 0; j < 5; ++j) {
                JobDetail job = createLogJob(j);
                Trigger trigger = createIntervalTrigger(j, 35);
                scheduler.scheduleJob(job, trigger);
            }
            {
                JobDetail job = createLogJob(10);
                Trigger trigger = createTimezoneTrigger(10, "Asia/Bangkok");
                scheduler.scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            log.warn("SchedulerException during job creation", e);
        }

        try {
            log.info("start scheduling");
            scheduler.start();
            // running for 120 x 5s = 10m
            for (int loop = 0; loop < 120; ++loop) {
                sleep(5);
                logStatus(scheduler);
            }
        } catch (SchedulerException e) {
            log.warn("SchedulerException during execution", e);
        }

        try {
            log.info("shutting down");
            scheduler.shutdown(true);

        } catch (SchedulerException e) {
            log.warn("SchedulerException during termination", e);
        }

        log.info("bye");
        System.exit(0);
    }
}
