package com.qj.schedule.bean;

class EntityBean {
	
	public final int TYPE_JOB = 1;
	
	public final int TYPE_JOBFLOW = 2;
	
	int entityType;

	int entityId;

	public int getEntityType() {
		return entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	
	
	
}
