package com.qj.schedule.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qj.schedule.util.ConfigUtil2;

/**
 * Cluster选主
 *  
 * @author qiaojiang
 *
 */
public class MasterSelector extends LeaderSelectorListenerAdapter {
	
	private static final Logger LOGGER = LogManager.getLogger(MasterSelector.class);
	
	private String name;
	
	private  String zkServer;
	
	private  String zkPath;

    private LeaderSelector leaderSelector;
    
    private CuratorFramework curatorClient;
    
    private AtomicInteger leaderCount = new AtomicInteger();
    
    private Callback callback; 

    public MasterSelector(String name, Callback callback){
    	this.name = name;
    	this.callback = callback;
    	this.zkServer = ConfigUtil2.get("zookeeper_server");
    	this.zkPath = ConfigUtil2.get("zookeeper_master_path");
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZkServer() {
		return zkServer;
	}

	public void setZkServer(String zkServer) {
		this.zkServer = zkServer;
	}

	public String getZkPath() {
		return zkPath;
	}

	public void setZkPath(String zkPath) {
		this.zkPath = zkPath;
	}
	
	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	private void init() {
		//初始化curate client
		this.curatorClient = CuratorFrameworkFactory.newClient(this.zkServer, new ExponentialBackoffRetry(1000, 3));
		//初始化leader selector
        this.leaderSelector = new LeaderSelector(this.curatorClient, this.zkPath, this);
        //放弃leadership时自动加入选举队列
        this.leaderSelector.autoRequeue();
	}

	public void start() {
		this.init();
    	//启动curator client
    	this.curatorClient.start();
    	//启动leader selector
        this.leaderSelector.start();
    }

    public void close() {
    	//关闭eader selector
    	if(this.leaderSelector != null) {
    		this.leaderSelector.close();
    	}
        //关闭curator client
    	if(this.curatorClient != null) {
    		this.curatorClient.close();
    	} 
    }
    
	public void takeLeadership(CuratorFramework client) throws Exception {
        try{
        	LOGGER.info(this.name + " take the leadership, count:" + leaderCount.getAndIncrement());
        	//回调应用函数
        	this.callback.callback();
            Thread.sleep(1000);
        }catch (InterruptedException e){
        	LOGGER.error(this.name + " was interrupted");
            Thread.currentThread().interrupt();
        }finally{
        	LOGGER.info(this.name + " relinquishing the leadership");
        }
	}

}
