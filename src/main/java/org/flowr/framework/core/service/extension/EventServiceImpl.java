package org.flowr.framework.core.service.extension;

import java.util.Arrays;
import java.util.Properties;

import org.flowr.framework.core.constants.FrameworkConstants;
import org.flowr.framework.core.event.pipeline.EventBus;
import org.flowr.framework.core.event.pipeline.EventPipeline;
import org.flowr.framework.core.event.pipeline.EventPipelineBus;
import org.flowr.framework.core.event.pipeline.EventPipelineBusExecutor;
import org.flowr.framework.core.event.pipeline.Pipeline.PipelineFunctionType;
import org.flowr.framework.core.event.pipeline.Pipeline.PipelineType;
import org.flowr.framework.core.flow.EventPublisher;
import org.flowr.framework.core.model.EventModel;
import org.flowr.framework.core.model.MetaData;
import org.flowr.framework.core.service.ServiceFramework;

/**
 * 
 * 
 * @author Chandra Shekhar Pandey
 * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
 */

public class EventServiceImpl implements EventService{

	private ServiceUnit serviceUnit 					= ServiceUnit.POOL;
	private String dependencyName						= EventService.class.getSimpleName();
	private DependencyType dependencyType 				= DependencyType.MANDATORY;
	private static EventBus eventBus					= new EventPipelineBus();
	private String serviceName							= FrameworkConstants.FRAMEWORK_SERVICE_EVENT;
	private ServiceStatus serviceStatus					= ServiceStatus.UNUSED;
	private ServiceType serviceType						= ServiceType.EVENT;
	private EventPipelineBusExecutor eventBusExecutor 	= null;
	private ServiceFramework<?,?> serviceFramework			= null;
	
	public EventRegistrationStatus registerEventPipeline(String pipelineName,PipelineType pipelineType, PipelineFunctionType 
			pipelineFunctionType,EventPublisher<EventModel> eventPublisher) {		
		
		EventRegistrationStatus status = EventRegistrationStatus.UNREGISTERED;
	
		if(!eventPublisher.isSubscribed()) {
		
			EventPipeline processSubscriber  =  eventBus.lookup(pipelineName,pipelineType,pipelineFunctionType);
			
			if(processSubscriber != null) {
				eventPublisher.subscribe(processSubscriber);
				status = EventRegistrationStatus.REGISTERED;
			}
		}else {
			status = EventRegistrationStatus.REGISTERED;
		}
		
		return status;
	}
	
	public void process() {
		
		this.serviceFramework.getNotificationService().notify(eventBusExecutor.process());
	}
	
	public EventServiceImpl() {
		
		Arrays.asList(PipelineFunctionType.values()).forEach(
				
				(e) -> {
					switch(e) {
						
						case PIPELINE_MANAGEMENT_EVENT:{
							EventPipeline processSubscriber  =  new EventPipeline();
							processSubscriber.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_MANAGEMENT);
							processSubscriber.setPipelineType(PipelineType.TRANSFER);
							processSubscriber.setPipelineFunctionType(PipelineFunctionType.PIPELINE_MANAGEMENT_EVENT);
							eventBus.addEventPipeline(processSubscriber);
							break;
						}case PIPELINE_PROMISE_EVENT:{
							EventPipeline processSubscriber  =  new EventPipeline();
							processSubscriber.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE);
							processSubscriber.setPipelineType(PipelineType.TRANSFER);
							processSubscriber.setPipelineFunctionType(PipelineFunctionType.PIPELINE_PROMISE_EVENT);							
							eventBus.addEventPipeline(processSubscriber);
							break;
						}case PIPELINE_PROMISE_PHASED_EVENT:{
							EventPipeline processSubscriber  =  new EventPipeline();
							processSubscriber.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE_PHASED);
							processSubscriber.setPipelineType(PipelineType.TRANSFER);
							processSubscriber.setPipelineFunctionType(PipelineFunctionType.PIPELINE_PROMISE_PHASED_EVENT);							
							eventBus.addEventPipeline(processSubscriber);
							break;
						}case PIPELINE_PROMISE_SCHEDULED_EVENT:{
							EventPipeline processSubscriber  =  new EventPipeline();
							processSubscriber.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE_SCHEDULED);
							processSubscriber.setPipelineType(PipelineType.TRANSFER);
							processSubscriber.setPipelineFunctionType(PipelineFunctionType.PIPELINE_PROMISE_SCHEDULED_EVENT);							
							eventBus.addEventPipeline(processSubscriber);
							break;
						}case PIPELINE_PROMISE_STAGED_EVENT:{
							EventPipeline processSubscriber  =  new EventPipeline();
							processSubscriber.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE_STAGED);
							processSubscriber.setPipelineType(PipelineType.TRANSFER);
							processSubscriber.setPipelineFunctionType(PipelineFunctionType.PIPELINE_PROMISE_STAGED_EVENT);							
							eventBus.addEventPipeline(processSubscriber);
							break;
						}case PIPELINE_PROMISE_STREAM_EVENT:{
							EventPipeline processSubscriber  =  new EventPipeline();
							processSubscriber.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE_STREAM);
							processSubscriber.setPipelineType(PipelineType.TRANSFER);
							processSubscriber.setPipelineFunctionType(PipelineFunctionType.PIPELINE_PROMISE_STREAM_EVENT);							
							eventBus.addEventPipeline(processSubscriber);
							break;
						}default:{
							break;
						}
						
					}
				}
		);
		
		eventBusExecutor = new EventPipelineBusExecutor(eventBus);
		
		/*
		ManagedProcessHandler managedProcessHandler = new ManagedProcessHandler();
		EventPipeline subscriberManagedProcess  =  new EventPipeline();
		subscriberManagedProcess.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_MANAGEMENT);
		eventBus.addEventPipeline(subscriberManagedProcess);
		managedProcessHandler.subscribe(subscriberManagedProcess);
		
		EventPipeline subscriberPromise =  new EventPipeline();
		subscriberManagedProcess.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE);
		eventBus.addEventPipeline(subscriberPromise);
		//promiseHandler.subscribe(subscriberPromise);		
		
		
		EventPipeline subscriberPhasedPromise =  new EventPipeline();
		subscriberManagedProcess.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE_PHASED);
		eventBus.addEventPipeline( subscriberPhasedPromise);
		//phasedPromiseHandler.subscribe(subscriberPhasedPromise);
		
		
		EventPipeline subscriberScheduledPromise  =  new EventPipeline();
		subscriberManagedProcess.setPipelineName(FrameworkConstants.FRAMEWORK_PIPELINE_PROMISE_SCHEDULED);
		eventBus.addEventPipeline(subscriberScheduledPromise);
		//scheduledPromiseHandler.subscribe(subscriberScheduledPromise);
		
		
		boolean keepRunning = true;
		
		Runnable r = new Runnable() {
	
			@Override
			public void run() {
				
				while(keepRunning) {
										
					try {
						System.out.println("Runnable");
						eventBusExecutor.process();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		};
		
		Thread t = new Thread(r);
		
		t.start();
		
		return eventBus;*/
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
	public void addServiceListener(EventPublisher<MetaData> engineListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceStatus startup(Properties configProperties) {
		
		serviceStatus = ServiceStatus.STARTED;
		
		return serviceStatus;
	}

	@Override
	public ServiceStatus shutdown(Properties configProperties) {
		serviceStatus = ServiceStatus.STOPPED;
		return serviceStatus;
	}

	@Override
	public void setServiceFramework(ServiceFramework<?,?> serviceFramework) {
		this.serviceFramework = serviceFramework;
	}

}
