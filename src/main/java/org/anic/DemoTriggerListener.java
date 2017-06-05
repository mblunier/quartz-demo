package org.anic;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;

import static org.anic.Demo.randomInt;

/**
 * Created by markus on 03.06.17.
 */
public class DemoTriggerListener extends TriggerListenerSupport {

    private Logger log = getLog();

    @Override
    public String getName () {
        return "DemoTriggerListener";
    }

    @Override
    public boolean vetoJobExecution (Trigger trigger, JobExecutionContext context) {
        boolean admit = randomInt(0,6) < 4;
        log.info((admit ? "Admitting" : "Preventing") + " execution of job " + trigger.getJobKey());
        return !admit;
    }

    @Override
    public void triggerFired (Trigger trigger, JobExecutionContext context) {
        log.info("Trigger " + trigger.getKey() + " fired, executing job " + trigger.getJobKey());
    }

    @Override
    public void triggerMisfired (Trigger trigger) {
        log.info("Trigger " + trigger.getKey() + " misfired, next execution: " + trigger.getNextFireTime());
    }

    @Override
    public void triggerComplete (Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.info("Trigger " + trigger.getKey() + " completed, executed " + trigger.getJobKey());
    }
}
