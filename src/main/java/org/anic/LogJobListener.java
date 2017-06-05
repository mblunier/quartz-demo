package org.anic;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;

/**
 * Created by markus on 03.06.17.
 */
public class LogJobListener extends JobListenerSupport {

    private Logger log = getLog();

    @Override
    public String getName () {
        return "LogJobListener";
    }

    @Override
    public void jobToBeExecuted (JobExecutionContext context) {
        log.info("Job to be executed: " + context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed (JobExecutionContext context) {
        super.jobExecutionVetoed(context);
    }

    @Override
    public void jobWasExecuted (JobExecutionContext context, JobExecutionException jobException) {
        if (jobException != null) {
            log.warn("Job was executed with an error: " + context.getJobDetail().getKey(), jobException);
        } else {
            log.info("Job was successfully executed: " + context.getJobDetail().getKey());
        }
    }
}
