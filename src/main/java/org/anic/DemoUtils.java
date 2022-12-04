package org.anic;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.util.Date;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by markus on 03.06.17.
 */
public class DemoUtils {

    static Random random = new Random();

    /**
     * Generate a random integer within a given interval.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return An integer greater or equal than lower and less than upper
     */
    static int randomInt(int lower, int upper) {
        return abs(lower + random.nextInt() % upper);
    }

    /**
     * Dely thread execution for a given number of seconds.
     *
     * @param seconds Sleeping interval.
     * @return Whether the end of the interval was reached (true) or
     * sleeping was interrupted (false).
     */
    static boolean sleep(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
            return true;
        } catch (InterruptedException ignore) {
        }
        return false;
    }

    static String schedulerInfo(Scheduler scheduler) throws SchedulerException {
        SchedulerMetaData meta = scheduler.getMetaData();
        return scheduler.getSchedulerName() + "." + scheduler.getSchedulerInstanceId() +
                ", version " + meta.getVersion() +
                ", running since " + meta.getRunningSince() +
                ", having executed " + meta.getNumberOfJobsExecuted() + " jobs";
    }

    static String jobInfo(JobExecutionContext context) throws SchedulerException {
        JobDetail job = context.getJobDetail();
        return job.getKey() +
                ": " + context;
    }

    static String triggerInfo(Scheduler scheduler, TriggerKey key) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(key);
        Date nextTime = trigger.getNextFireTime();
        return key.toString() +
                ':' +
                " prevTime=" + trigger.getPreviousFireTime() +
                " nextTime=" + nextTime +
                " overnextTime=" + trigger.getFireTimeAfter(nextTime) +
                " finalTime=" + trigger.getFinalFireTime();
    }

}
