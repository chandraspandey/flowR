package org.flowr.framework.core.service;

import static org.flowr.framework.core.constants.ExceptionConstants.ERR_CONFIG;
import static org.flowr.framework.core.constants.ExceptionMessages.MSG_CONFIG;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;

import org.flowr.framework.core.config.ServiceConfiguration;
import org.flowr.framework.core.exception.ConfigurationException;
import org.flowr.framework.core.node.Autonomic;
import org.flowr.framework.core.node.EndPoint;
import org.flowr.framework.core.node.EndPoint.EndPointStatus;
import org.flowr.framework.core.process.callback.Callback;
import org.flowr.framework.core.process.callback.RunnableCallback;

/**
 * 
 * 
 * @author Chandra Shekhar Pandey
 * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
 */

public class ServiceEndPoint implements EndPoint,RunnableCallback<SimpleEntry<ServiceEndPoint, EndPointStatus>>, 
	Autonomic<String,Boolean> {

	private EndPointStatus endPointStatus 				= EndPointStatus.UNREACHABLE;
	private boolean isNegotiated 						= false;
	private ServiceConfiguration serviceConfiguration 	= null;	
	private boolean keepRunning 						= true;
	private Timestamp lastUpdated;
	private String endPointType							= null;
	private Callback<SimpleEntry<ServiceEndPoint, EndPointStatus>> callback = null;
	
	public ServiceEndPoint(ServiceConfiguration serviceConfiguration){
		
		this.serviceConfiguration = serviceConfiguration;
	}
	
	@Override
	public EndPointStatus getEndPointStatus() {

		return endPointStatus;
	}
	
	public void setEndPointType(String endPointType){
		this.endPointType = endPointType;
	}
	
	public String getEndPointType(){
		return this.endPointType;
	}

	@Override
	public Boolean negotiate(String urlSpec) throws ConfigurationException {
		
		try {
			URL url = new URL(urlSpec);
			URLConnection con = url.openConnection();
			con.setReadTimeout((int)this.serviceConfiguration.getTimeout());
			con.connect();		
			isNegotiated = true;
		} catch ( SocketTimeoutException s) {
			s.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.lastUpdated 		=  Timestamp.from(Instant.now());
		
		if(!isNegotiated){
			throw new ConfigurationException(
					ERR_CONFIG,
					MSG_CONFIG, 
					"Service EndPoint URL : "+urlSpec+" responded with endPointStatus : "+endPointStatus);
		}
		
		return isNegotiated;
	}
	
	@Override
	public Timestamp getLastUpdated(){
		
		return this.lastUpdated;
	}
	
	@Override
	public ServiceConfiguration getServiceConfiguration(){
		
		return this.serviceConfiguration;
	}


	@Override
	public boolean isNegotiated() {
		return isNegotiated;
	}

	@Override
	public void run() {

		while(keepRunning && !isNegotiated){
			
			try {
				
				if(heartbeat() ){
					endPointStatus 	= EndPointStatus.REACHABLE;
					keepRunning = false;
				}else{
					endPointStatus 	= EndPointStatus.UNREACHABLE;
				}				
				
				Thread.sleep(1000);
			} catch (ConfigurationException e) {
				e.printStackTrace();
				endPointStatus 	= EndPointStatus.UNREACHABLE;
				keepRunning = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
				endPointStatus 	= EndPointStatus.UNREACHABLE;
				keepRunning = false;
			}
		}
		
		isNegotiated = true;
		doCallback(new SimpleEntry<ServiceEndPoint, EndPointStatus>(this, endPointStatus));
		System.out.println("ServiceEndPoint : keepRunning : "+keepRunning);
	}
	
	public boolean heartbeat() throws ConfigurationException{
		
		return negotiate(this.serviceConfiguration.getNotificationEndPoint());
	}
	
	
	public void setKeepRunning(boolean keepRunning){
		this.keepRunning = keepRunning;
	}
	
	@Override
	public void register(Callback<SimpleEntry<ServiceEndPoint, EndPointStatus>> callback) {
		this.callback = callback;
	}

	@Override
	public SimpleEntry<ServiceEndPoint, EndPointStatus> doCallback(SimpleEntry<ServiceEndPoint, EndPointStatus> status) {

		return this.callback.doCallback(status);
	}
	
	
	public String toString(){
		
		return "ServiceEndPoint{"+
				" endPointStatus : "+endPointStatus+	
				" | isNegotiated : "+isNegotiated+	
				" | keepRunning : "+keepRunning+				
				" | lastUpdated : "+lastUpdated+
				" | serviceConfiguration : "+serviceConfiguration+
				"}\n";

	}


}
