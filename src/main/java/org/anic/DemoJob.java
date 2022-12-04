/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anic;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author markus
 */
public class DemoJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(DemoJob.class);

    public void execute(JobExecutionContext context) {

        JobDetail detail = context.getJobDetail();
        Trigger trigger = context.getTrigger();
        Date now = new Date();
        Date fireTime = context.getFireTime();
        Date nextTime = context.getNextFireTime();
        Date scheduledTime = context.getScheduledFireTime();

        int duration = DemoUtils.randomInt(1, 60);

        log.info("Executing {}: trigger={}, now={}, duration={}s, scheduledTime={}, fireTime={}, nextTime={}",
                detail.getKey(), trigger.getKey(), now, duration, scheduledTime, fireTime, nextTime);

        if (!DemoUtils.sleep(duration)) {
            log.info("{}: INTERRUPTED", detail.getKey());
        }

        log.info("{}: done", detail.getKey());
    }
}
