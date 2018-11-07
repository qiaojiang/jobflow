package com.qj.schedule.bean;

/**
 * 
 * @author qiaojiang
 *
 */
public class ExecutionBean {
	private int id;
	private int jobId;
	private String jobTitle;
	private int nodeId;
	private int relyNode;
	private String cmd;
	private String token;
	private String createTime;
	private String startTime;
	private String endTime;
	private int groupId;
	private short retryNum;
	private int retryInterval;
	private short execNum;
	private int hostId;
	private short status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public int getRelyNode() {
		return relyNode;
	}
	public void setRelyNode(int relyNode) {
		this.relyNode = relyNode;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public short getRetryNum() {
		return retryNum;
	}
	public void setRetryNum(short retryNum) {
		this.retryNum = retryNum;
	}
	public int getRetryInterval() {
		return retryInterval;
	}
	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}
	public short getExecNum() {
		return execNum;
	}
	public void setExecNum(short execNum) {
		this.execNum = execNum;
	}
	public int getHostId() {
		return hostId;
	}
	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	
}
