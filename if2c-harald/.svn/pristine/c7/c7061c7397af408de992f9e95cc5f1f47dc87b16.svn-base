package com.if2c.harald.sheduler;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.if2c.harald.db.Config;
import com.if2c.harald.db.JobInfo;
import com.if2c.harald.job.JobBase;
import com.if2c.harald.router.HaraldClient;

public class Sheduler {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		List<Class<JobBase>> list = Config.getConf().getClasses();
		for (int i = 0; i < list.size(); i++) {
			Class<JobBase> c = (Class<JobBase>) list.get(i);
			JobBase ci = c.newInstance();
			Map<String, JobInfo> jobInfos = Config.getConf().getJobInfos();
			ci.setInterval(jobInfos.get(ci.getClass().getName()).getInterval());
			ci.setRepeatCount(jobInfos.get(ci.getClass().getName())
					.getRepeatCount());
			ci.setStartDate(jobInfos.get(ci.getClass().getName())
					.getStartDate());
			ci.setCronExpression(jobInfos.get(ci.getClass().getName())
					.getCronExpression());
			if (ci instanceof JobBase) {
				try {
					// define the job and tie it to our HelloJob class
					JobDetail job = JobBuilder.newJob(c)
							.withIdentity(ci + "", ci + "_goup").build();
					String interval = BeanUtils.getProperty(ci, "interval");
					String repeatCount = BeanUtils.getProperty(ci,
							"repeatCount");
					String start = BeanUtils.getProperty(ci, "startDate");

					// Trigger the job to run now, and then repeat every 40
					// seconds
					SimpleScheduleBuilder sim = simpleSchedule();
					if (repeatCount == null) {
						sim = sim.repeatForever();
					} else {
						sim = sim.withRepeatCount(Integer.valueOf(repeatCount));
					}
					if (interval != null)
						sim = sim.withIntervalInSeconds(Integer
								.valueOf(interval));
					Trigger trigger = null;
					if (start == null) {
						trigger = newTrigger()
								.withIdentity(ci + "trigger", ci + "group")
								.startNow().withSchedule(sim).build();
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date startDate = sdf.parse(start);
						trigger = newTrigger()
								.withIdentity(ci + "trigger", ci + "group")
								.startAt(startDate).withSchedule(sim).build();
					}
					if (ci.getCronExpression() != null
							&& !ci.getCronExpression().equals(""))
						trigger = (CronTrigger) TriggerBuilder
								.newTrigger()
								.withIdentity(ci + "trigger", ci + "group")
								.withSchedule(
										CronScheduleBuilder.cronSchedule(ci
												.getCronExpression())).startNow().build();
					try {
						// Grab the Scheduler instance from the Factory
						Scheduler scheduler = StdSchedulerFactory
								.getDefaultScheduler();
						scheduler.scheduleJob(job, trigger);
						// and start it off
						scheduler.start();
					} catch (SchedulerException se) {
						se.printStackTrace();
					}
				} catch (Exception se) {
					se.printStackTrace();
				}
			}
		}
		HaraldClient.getInstance().init();
	}

}
