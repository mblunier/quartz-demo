package org.anic;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzDemo {

    private static final Logger log = LoggerFactory.getLogger(QuartzDemo.class);

    public static void main(String[] args) {

        Scheduler scheduler = createScheduler();
        if (scheduler == null) {
            log.error("Aborting...");
            return;
        }

        final int NUM_INTERVAL_JOBS = 5;

        try {
            for (int j = 0; j < NUM_INTERVAL_JOBS; ++j) {
                String group = "Interval" + (j % 3);
                JobDetail job = createJob(j, "IntervalJob", group);
                Trigger trigger = createIntervalTrigger(j, group, DemoUtils.randomInt(2, 35));
                scheduler.scheduleJob(job, trigger);
            }
            {
                int id = NUM_INTERVAL_JOBS + 1;
                String group = "East";
                JobDetail job = createJob(id, "CronJob", group);
                Trigger trigger = createTimezoneTrigger(id, group, "Asia/Bangkok", DemoUtils.randomInt(3, 5));
                scheduler.scheduleJob(job, trigger);
            }
            {
                int id = NUM_INTERVAL_JOBS + 2;
                String group = "West";
                JobDetail job = createJob(id, "CronJob", group);
                Trigger trigger = createTimezoneTrigger(id, group, "America/New_York", DemoUtils.randomInt(3, 5));
                scheduler.scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            log.warn("SchedulerException during job creation", e);
        }

        try {
            log.info("Start scheduling");
            scheduler.start();
            // running for 120 x 5s = 10m
            for (int loop = 0; loop < 120; ++loop) {
                DemoUtils.sleep(5);
                logStatus(scheduler, loop);
            }
        } catch (SchedulerException e) {
            log.warn("SchedulerException during execution", e);
        }

        try {
            log.info("Shutting down");
            scheduler.shutdown(true);

        } catch (SchedulerException e) {
            log.warn("SchedulerException during termination", e);
        }

        log.info("Bye");
        System.exit(0);
    }

    private static Scheduler createScheduler() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.getListenerManager().addJobListener(new DemoJobListener());
            scheduler.getListenerManager().addTriggerListener(new DemoTriggerListener());
            log.info("Scheduler initialized: {}", scheduler.getMetaData().getSummary());
            return scheduler;
        } catch (SchedulerException e) {
            log.warn("SchedulerException during initialization", e);
            return null;
        }
    }

    private static JobDetail createJob(int id, String name, String group) {
        log.info("Creating job {} in group {}", name + (id + 1), group);
        return newJob(DemoJob.class)
                .withIdentity(name + (id + 1), group)
                .build();
    }

    private static Trigger createIntervalTrigger(int id, String group, int interval) {
        String name = "Trigger" + (id + 1);
        log.info("Creating interval trigger {} with interval {} in group {}", name, interval, group);
        return newTrigger()
                .withIdentity(name, group)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(interval)
                        .repeatForever())
                .startNow()
                .build();
    }

    private static Trigger createTimezoneTrigger(int id, String group, String timezone, int delaySecs) {
        String name = "Trigger" + (id + 1);
        log.info("Creating cron trigger {} with timezone {} in group {} with {}s delay", name, timezone, group, delaySecs);
        return newTrigger()
                .withIdentity(name, group)
                .withSchedule(cronSchedule("0 3/3 * * * ?")
                        .inTimeZone(TimeZone.getTimeZone(timezone)))
                .startAt(Date.from(Instant.now().plusSeconds(delaySecs)))
                .build();
    }

    private static void logStatus(Scheduler scheduler, int loop) {
        try {
            log.info("== {}. {}", loop, DemoUtils.schedulerInfo(scheduler));
            List<JobExecutionContext> jobs = scheduler.getCurrentlyExecutingJobs();
            log.info("==== {} jobs currently executing:", jobs.size());
            for (JobExecutionContext context : jobs) {
                log.info(DemoUtils.jobInfo(context));
            }
            Set<TriggerKey> triggers = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
            log.info("==== {} active triggers:", triggers.size());
            for (TriggerKey key : triggers) {
                log.info(DemoUtils.triggerInfo(scheduler, key));
            }
        } catch (SchedulerException e) {
            log.warn("SchedulerException", e);
        }
    }

}
