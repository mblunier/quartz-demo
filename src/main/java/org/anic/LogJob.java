/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anic;

import java.util.Date;
import java.util.Random;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.abs;

/**
 *
 * @author markus
 */
public class LogJob implements Job {
    
    static Logger log = LoggerFactory.getLogger(LogJob.class);
    static Random random = new Random();
    
    public void execute (JobExecutionContext context) throws JobExecutionException {
        //Job job = context.getJobInstance();
        JobDetail detail = context.getJobDetail();
        Trigger trigger = context.getTrigger();
        Date now = new Date();
        Date fireTime = context.getFireTime();
        Date nextTime = context.getNextFireTime();
        Date scheduledTime = context.getScheduledFireTime();

        long delay = abs(10 + random.nextLong() % 60000);

        log.info("{}: trigger={}, now={}, scheduledTime={}, fireTime={}, nextTime={}, delay={}",
                 detail.getKey(), trigger.getKey(), now, scheduledTime, fireTime, nextTime, delay);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            log.info("{}: INTERRUPTED", detail.getKey());
        } finally {
            log.info("{}: done", detail.getKey());
        }
    }
}
