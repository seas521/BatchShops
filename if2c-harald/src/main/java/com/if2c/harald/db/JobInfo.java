package com.if2c.harald.db;

public class JobInfo {
	private String name;
	private Integer repeatCount;
	private String startDate;
	private Integer interval;
	private String cronExpression;
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Override
	public String toString() {
		return "JobInfo [name=" + name + ", repeatCount=" + repeatCount
				+ ", startDate=" + startDate + ", interval=" + interval
				+ ", cronExpression=" + cronExpression + ", className="
				+ className + "]";
	}

}
