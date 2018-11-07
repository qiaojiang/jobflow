package com.qj.schedule.bean;

/**
 * 
CREATE TABLE `jf_jobflow_node` (
  `node_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `job_id` int(11) unsigned DEFAULT NULL COMMENT '作业ID',
  `flow_id` int(11) unsigned DEFAULT NULL COMMENT '作业流ID',
  `rely` int(11) unsigned DEFAULT NULL COMMENT '依赖节点ID',
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

 * @author qiaojiang
 *
 */
public class JobFlowNodeBean {
	private int nodeId;
	private int jobId;
	private int flowId;
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
	public int getFlowId() {
		return flowId;
	}
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	public int getRely() {
		return rely;
	}
	public void setReply(int rely) {
		this.rely = rely;
	}
	
	
}
