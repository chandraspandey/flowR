package org.flowr.framework.core.service.internal;

import static org.flowr.framework.core.constants.ExceptionConstants.ERR_CONFIG;
import static org.flowr.framework.core.constants.ExceptionMessages.MSG_CONFIG;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.flowr.framework.core.config.Configuration.ConfigurationType;
import org.flowr.framework.core.constants.FrameworkConstants;
import org.flowr.framework.core.event.pipeline.Pipeline.PipelineFunctionType;
import org.flowr.framework.core.event.pipeline.Pipeline.PipelineType;
import org.flowr.framework.core.exception.ConfigurationException;
import org.flowr.framework.core.flow.EventPublisher;
import org.flowr.framework.core.node.Circuit;
import org.flowr.framework.core.node.Circuit.CircuitStatus;
import org.flowr.framework.core.node.EndPoint.EndPointStatus;
import org.flowr.framework.core.node.IntegratedCircuit;
import org.flowr.framework.core.process.management.ManagedProcessHandler;
import org.flowr.framework.core.service.ServiceEndPoint;
import org.flowr.framework.core.service.ServiceFramework;

/**
 * 
 * 
 * @author Chandra Shekhar Pandey
 * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
 */

public class HighAvailabilityServiceImpl implements HighAvailabilityService{

	private ServiceUnit serviceUnit 								= ServiceUnit.SINGELTON;
	private String dependencyName									= HighAvailabilityService.class.getSimpleName();
	private DependencyType dependencyType 							= DependencyType.MANDATORY;
	private String serviceName										= FrameworkConstants.FRAMEWORK_SERVICE_HEALTH;
	private ServiceType serviceType									= ServiceType.HEALTH;
	private ServiceFramework<?,?> serviceFramework					= null;
	private ManagedProcessHandler managedProcessHandler 			= ManagedService.getDefaultProcessHandler();
	private Circuit clientCircuit 									= null;
	private Circuit serverCircuit 									= null;	
	private ServiceStatus serviceStatus								= ServiceStatus.UNUSED;
	
	@Override
	public void setServiceFramework(ServiceFramework<?,?> serviceFramework) {
		
		this.serviceFramework = serviceFramework;
		
		this.serviceFramework.getEventService().registerEventPipeline(
				FrameworkConstants.FRAMEWORK_PIPELINE_HEALTH,
				PipelineType.TRANSFER, 
				PipelineFunctionType.HEALTH 
				,managedProcessHandler);		

	}
	
	
	@Override
	public Circuit getCircuit(ConfigurationType configurationType) {
		
		Circuit circuit = null;
		
		switch(configurationType) {
		
			case CLIENT:{
				circuit = clientCircuit;
				break;
			}case SERVER:{
				circuit = serverCircuit;
				break;
			}
		}
		
		return circuit;
	}
	
	@Override
	public void buildCircuit() throws ConfigurationException {
				
		Iterator<ConfigurationType> iter = Arrays.asList(ConfigurationType.values()).iterator();
						
			while(iter.hasNext()) {
				
				ConfigurationType configurationType = iter.next();
				
				switch(configurationType) {
					
					case CLIENT:{
						
						try {
						
							clientCircuit = new IntegratedCircuit();
							clientCircuit.buildCircuit(configurationType);
						
						} catch (InterruptedException | ExecutionException e) {
							clientCircuit.setCircuitStatus(CircuitStatus.UNAVAILABLE);
							e.printStackTrace();
							throw new ConfigurationException(
									ERR_CONFIG,
									MSG_CONFIG, 
									"Node instance creation interuppted.");							
						}	
						break;
					}case SERVER:{
						
						try {
							
							serverCircuit = new IntegratedCircuit();
							serverCircuit.buildCircuit(configurationType);
						} catch (InterruptedException | ExecutionException e) {
							serverCircuit.setCircuitStatus(CircuitStatus.UNAVAILABLE);
							e.printStackTrace();
							throw new ConfigurationException(
									ERR_CONFIG,
									MSG_CONFIG, 
									"Node instance creation interuppted.");
						}	
						break;
					}default:{
						break;
					}				
				}				
			}
			
		
	}
	
	@Override
	public EndPointStatus addServiceEndpoint(ConfigurationType configurationType, ServiceEndPoint serviceEndPoint) 
			throws ConfigurationException {
		
		EndPointStatus status = EndPointStatus.UNREACHABLE;
		
		switch(configurationType) {
		
			case CLIENT:{
				status = clientCircuit.addServiceEndpoint(serviceEndPoint);
				break;
			}case SERVER:{	
				status = serverCircuit.addServiceEndpoint(serviceEndPoint);
				break;
			}default:{
				break;
			}	
		}
		
		return status;
	}
	
	@Override
	public EndPointStatus removeServiceEndpoint(ConfigurationType configurationType, ServiceEndPoint serviceEndPoint) 
			throws ConfigurationException {
		
		EndPointStatus status = EndPointStatus.UNREACHABLE;
		
		switch(configurationType) {
		
			case CLIENT:{
				status = clientCircuit.removeServiceEndpoint(serviceEndPoint);
				break;
			}case SERVER:{	
				status = serverCircuit.removeServiceEndpoint(serviceEndPoint);
				break;
			}default:{
				break;
			}	
		}
		
		return status;
	}
	
	@Override
	public void setServiceType(ServiceType serviceType) {
		
		this.serviceType = serviceType;
	}
	
	@Override
	public ServiceType getServiceType() {
		
		return this.serviceType;
	}
	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@Override
	public String getServiceName() {

		return this.serviceName;
	}
	
	@Override
	public void setServiceUnit(ServiceUnit serviceUnit) {
		this.serviceUnit = serviceUnit;
	}

	@Override
	public ServiceUnit getServiceUnit() {
		return this.serviceUnit;
	}

	@Override
	public DependencyStatus loopTest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDependencyName() {
		return this.dependencyName;
	}

	@Override
	public DependencyType getDependencyType() {
		return this.dependencyType;
	}

	@Override
	public DependencyStatus verify() {
		DependencyStatus status = DependencyStatus.UNSATISFIED;
		
		return status;
	}

	@Override
	public void addServiceListener(EventPublisher engineListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceStatus startup(Properties configProperties) {
		
		try {
			buildCircuit();
			serviceStatus = ServiceStatus.STARTED;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			serviceStatus = ServiceStatus.ERROR;
		}
		return serviceStatus;
	}

	@Override
	public ServiceStatus shutdown(Properties configProperties) {
		
		clientCircuit.shutdownCircuit();
		serverCircuit.shutdownCircuit();
		serviceStatus = ServiceStatus.STOPPED;
		return serviceStatus;
	}

}