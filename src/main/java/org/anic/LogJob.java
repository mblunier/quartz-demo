/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anic;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author markus
 */
public class LogJob implements Job {
    
    static Logger log = LoggerFactory.getLogger(LogJob.class);
    
    @Override
    public void execute (JobExecutionContext context) throws JobExecutionException {
        //Job job = context.getJobInstance();
        JobDetail detail = context.getJobDetail();
        Date now = new Date();
        Date fireTime = context.getFireTime();
        Date nextTime = context.getNextFireTime();
        Date schedTime = context.getScheduledFireTime();
        
        log.info("{}: now={}, schedTime={}, fireTime={}, nextTime={}",
                 detail.getKey(), now, schedTime, fireTime, nextTime);
//        System.out.println(detail.getKey() +
//                ": now=" + now +
//                ", schedTime=" + schedTime +
//                ", fireTime=" + fireTime +
//                ", nextTime=" + nextTime);
    }
}
