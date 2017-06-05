/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anic;

import org.quartz.*;
import org.slf4j.Logger;

import java.util.Date;

import static org.anic.Demo.*;

/**
 *
 * @author markus
 */
public class DemoJob implements Job {
    
    static Logger log = getLog(DemoJob.class);

    public void execute (JobExecutionContext context) throws JobExecutionException {

        //Job job = context.getJobInstance();
        JobDetail detail = context.getJobDetail();
        Trigger trigger = context.getTrigger();
        Date now = new Date();
        Date fireTime = context.getFireTime();
        Date nextTime = context.getNextFireTime();
        Date scheduledTime = context.getScheduledFireTime();

        int duration = randomInt(1, 60);

        log.info("Executing {}: trigger={}, now={}, duration={}s, scheduledTime={}, fireTime={}, nextTime={}",
                 detail.getKey(), trigger.getKey(), now, duration, scheduledTime, fireTime, nextTime);

        if (!sleep(duration)) {
            log.info("{}: INTERRUPTED", detail.getKey());
        }

        log.info("{}: done", detail.getKey());
    }
}
