package org.anic;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;

/**
 * Created by markus on 03.06.17.
 */
public class DemoJobListener extends JobListenerSupport {

    private final Logger log = getLog();

    public String getName() {
        return "DemoJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("Job {} is about to be executed", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        super.jobExecutionVetoed(context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobKey key = context.getJobDetail().getKey();
        if (jobException != null) {
            log.warn("Job {} was executed with an error", key, jobException);
        } else {
            log.info("Job {} was successfully executed", key);
        }
    }
}
