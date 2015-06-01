package com.if2c.harald.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhw
 * @createTime:2014年7月22日 
 */
public class SmsMessage {
	private Long batch_id;//批次id
	private String mobile;//手机号，查询用
	private String content;//短信内容
	private String create_time;//创建时间
	private List<String> mobileList;//批次手机号
	private String messageType;//短信类型（按内容分）
	private String messageTitle;//短信标题
	private String timeStart;//查询条件  开始时间
	private String timeTo;//查询条件 结束时间
	private int is_send;//是否发送 1yes 0 no 
	private String send_time;//短信发送时间
	private String update_time;//更新时间
	private String set_send_time;//设置定时发送时间
	
	public Long getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(Long batch_id) {
		this.batch_id = batch_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public List<String> getMobileList() {
		return mobileList;
	}
	public void setMobileList(List<String> mobileList) {
		this.mobileList = mobileList;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}
	public int getIs_send() {
		return is_send;
	}
	public void setIs_send(int is_send) {
		this.is_send = is_send;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getSet_send_time() {
		return set_send_time;
	}
	public void setSet_send_time(String set_send_time) {
		this.set_send_time = set_send_time;
	}

}
