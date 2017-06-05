package org.anic;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by markus on 03.06.17.
 */
public class Demo {

    static Random random = new Random();

    /**
     *
     * @param lower
     * @param upper
     * @return An integer greater or equal than lower and less than upper
     */
    static int randomInt (int lower, int upper) {
        return abs(lower + random.nextInt() % upper);
    }

    static Logger getLog (Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    static boolean sleep (int seconds) {
        try {
            Thread.sleep(1000 * seconds);
            return true;
        } catch (InterruptedException ignore) { }
        return false;
    }

    static String schedulerInfo (Scheduler scheduler) throws SchedulerException {
        StringBuilder sb = new StringBuilder();
        SchedulerMetaData meta = scheduler.getMetaData();
        sb.append(scheduler.getSchedulerName()).append(".").append(scheduler.getSchedulerInstanceId())
          .append(", version ").append(meta.getVersion())
          .append(", running since ").append(meta.getRunningSince())
          .append(", having executed ").append(meta.getNumberOfJobsExecuted()).append(" jobs");
        return sb.toString();
    }

    static String jobInfo (Scheduler scheduler, JobExecutionContext context) throws SchedulerException {
        StringBuilder sb = new StringBuilder();
        JobDetail job = context.getJobDetail();
        sb.append(job.getKey())
          .append(": ")
          .append(context.toString());
        return sb.toString();
    }

    static String triggerInfo (Scheduler scheduler, TriggerKey key) throws SchedulerException {
        StringBuilder sb = new StringBuilder();
        Trigger trigger = scheduler.getTrigger(key);
        sb.append(key.toString())
          .append(':')
          .append(" lastTime=").append(trigger.getPreviousFireTime())
          .append(" nextTime=").append(trigger.getNextFireTime());
        return sb.toString();
    }

}
