package org.anic;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;

/**
 * Created by markus on 03.06.17.
 */
public class DemoTriggerListener extends TriggerListenerSupport {

    private final Logger log = getLog();

    public String getName() {
        return "DemoTriggerListener";
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        boolean admit = DemoUtils.randomInt(0, 6) < 4;
        log.info("Randomly {} execution of job {}", admit ? "admitting" : "preventing", trigger.getJobKey());
        return !admit;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.info("Trigger {} fired, executing job {}", trigger.getKey(), trigger.getJobKey());
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("Trigger {} misfired, next execution: {}", trigger.getKey(), trigger.getNextFireTime());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.info("Trigger {} completed, executed {}", trigger.getKey(), trigger.getJobKey());
    }
}
