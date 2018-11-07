package com.qj.schedule.bean;

import java.util.Map;

/**
 * 
 * @author qiaojiang
 *
 */
public class JobFlowNodeDetailBean {
	private int nodeId;
	private int jobId;
	private String jobTitle;
	private String cmd;
	private int flowId;
	private String flowTitle;
	private int rely;
	
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public int getFlowId() {
		return flowId;
	}
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	public String getFlowTitle() {
		return flowTitle;
	}
	public void setFlowTitle(String flowTitle) {
		this.flowTitle = flowTitle;
	}
	public int getRely() {
		return rely;
	}
	public void setRely(int rely) {
		this.rely = rely;
	}
}
