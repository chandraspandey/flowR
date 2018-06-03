package org.flowr.framework.core.promise.scheduled;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.flowr.framework.core.notification.Notification.NotificationDeliveryType;
import org.flowr.framework.core.promise.Promise.PromiseState;
import org.flowr.framework.core.promise.Promise.PromiseStatus;
import org.flowr.framework.core.promise.Promise.ScheduleStatus;
import org.flowr.framework.core.promise.RequestScale;
import org.flowr.framework.core.promise.Scale;


/**
 * Defines ScheduledProgressScale as enclosed model that has Time/Calendar characteristics which can be used for 
 * automatic negotiation of execution at future point of time.
 * @author Chandra Shekhar Pandey
 * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
 */

public class ScheduledProgressScale implements Scale{

	private ScheduleStatus scheduleStatus; 
	private Timestamp scheduledTimestamp;
	private Timestamp deferredTimestamp;
	
	private String subscriptionClientId;
	private NotificationDeliveryType notificationDeliveryType;
	private PromiseState promiseState;
	private PromiseStatus promiseStatus;
	protected String acknowledgmentIdentifier;
	private double MIN_SCALE;
	private double MAX_SCALE;	
	private double now;
	private double scaleUnit;
	private TimeUnit progressTimeUnit;
	private long INTERVAL;
	private Hashtable<String, String> metaDataAttributes;
	private EventOrigin eventOrigin;
	private Severity severity 	= Severity.LOW;
	private Priority priority	= Priority.LOW;
	
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	
	public Severity getSeverity() {
		return this.severity;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public Priority getPriority() {
		return this.priority;
	}
	
	public String getSubscriptionClientId() {
		return subscriptionClientId;
	}
	public void setSubscriptionClientId(String subscriptionClientId) {
		this.subscriptionClientId = subscriptionClientId;
	}
	
	public NotificationDeliveryType getNotificationDeliveryType() {
		return notificationDeliveryType;
	}
	public void setNotificationDeliveryType(NotificationDeliveryType notificationDeliveryType) {
		this.notificationDeliveryType = notificationDeliveryType;
	}
	
	public boolean equals(Object scheduledProgressScale){
		
		boolean isEqual = false;
		
		ScheduledProgressScale other = (ScheduledProgressScale)scheduledProgressScale;
		
		if(
				other != null &&
				this.scheduleStatus 	== other.scheduleStatus &&
				this.scheduledTimestamp == other.scheduledTimestamp &&
				this.deferredTimestamp 	== other.deferredTimestamp &&
				this.subscriptionClientId		== other.subscriptionClientId &&
				this.acknowledgmentIdentifier 	== other.acknowledgmentIdentifier &&
				this.promiseState 				== other.promiseState && 
				this.promiseStatus 				== other.promiseStatus &&
				this.MIN_SCALE					== other.MIN_SCALE &&
				this.MAX_SCALE					== other.MAX_SCALE &&
				this.now						== other.now &&
				this.scaleUnit					== other.scaleUnit &&
				this.progressTimeUnit 			== other.progressTimeUnit &&
				this.INTERVAL					== other.INTERVAL &&
				this.metaDataAttributes 		== other.metaDataAttributes &&
				this.eventOrigin				== other.eventOrigin
		){
			isEqual = true;
		}
		
		return isEqual;
	}
	
	@Override
	public void clone(Scale progressScale) {
		
		ScheduledProgressScale scheduledProgressScale = (ScheduledProgressScale)progressScale;
		
		if(scheduledProgressScale != null){
			this.setAcknowledgmentIdentifier(scheduledProgressScale.getAcknowledgmentIdentifier());
			this.setNow(scheduledProgressScale.getNow());
			this.setPromiseState(scheduledProgressScale.getPromiseState());
			this.setPromiseStatus(scheduledProgressScale.getPromiseStatus());
			this.setEventOrigin(scheduledProgressScale.getEventOrigin());
			this.setINTERVAL(scheduledProgressScale.getINTERVAL());
			this.setMAX_SCALE(scheduledProgressScale.getMAX_SCALE());
			this.setMIN_SCALE(scheduledProgressScale.getMIN_SCALE());
			this.setProgressTimeUnit(scheduledProgressScale.getProgressTimeUnit());
			this.setScaleUnit(scheduledProgressScale.getScaleUnit());
			this.setMetaDataAttributes(scheduledProgressScale.getMetaDataAttributes());
			this.setScheduleStatus(scheduledProgressScale.getScheduleStatus());
			this.setScheduledTimestamp(scheduledProgressScale.getScheduledTimestamp());
			this.setDeferredTimestamp(scheduledProgressScale.getDeferredTimestamp());
			this.setSubscriptionClientId(scheduledProgressScale.getSubscriptionClientId());
			this.setNotificationDeliveryType(scheduledProgressScale.getNotificationDeliveryType());
		}
	}
	
	/**
	 *  Override the pass by reference values of ProgressScale base class
	 * @param scheduledProgressScale
	 */
	public void clone(ScheduledProgressScale scheduledProgressScale){
		
		this.setAcknowledgmentIdentifier(scheduledProgressScale.getAcknowledgmentIdentifier());
		this.setNow(scheduledProgressScale.getNow());
		this.setPromiseState(scheduledProgressScale.getPromiseState());
		this.setPromiseStatus(scheduledProgressScale.getPromiseStatus());
		this.setEventOrigin(scheduledProgressScale.getEventOrigin());
		this.setINTERVAL(scheduledProgressScale.getINTERVAL());
		this.setMAX_SCALE(scheduledProgressScale.getMAX_SCALE());
		this.setMIN_SCALE(scheduledProgressScale.getMIN_SCALE());
		this.setProgressTimeUnit(scheduledProgressScale.getProgressTimeUnit());
		this.setScaleUnit(scheduledProgressScale.getScaleUnit());
		this.setMetaDataAttributes(scheduledProgressScale.getMetaDataAttributes());
		
		this.setScheduleStatus(scheduledProgressScale.getScheduleStatus());
		this.setScheduledTimestamp(scheduledProgressScale.getScheduledTimestamp());
		this.setDeferredTimestamp(scheduledProgressScale.getDeferredTimestamp());
		this.setSubscriptionClientId(scheduledProgressScale.getSubscriptionClientId());
		this.setNotificationDeliveryType(scheduledProgressScale.getNotificationDeliveryType());

	}

	public ScheduleStatus getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Timestamp getScheduledTimestamp() {
		return scheduledTimestamp;
	}

	public void setScheduledTimestamp(Timestamp scheduledTimestamp) {
		this.scheduledTimestamp = scheduledTimestamp;
	}

	public Timestamp getDeferredTimestamp() {
		return deferredTimestamp;
	}

	public void setDeferredTimestamp(Timestamp deferredTimestamp) {
		this.deferredTimestamp = deferredTimestamp;
	}
	public void acceptIfApplicable(RequestScale requestScale){
		
		// validate business capabilities
		this.subscriptionClientId 	= requestScale.getSubscriptionClientId();
		this.MIN_SCALE 				= requestScale.getMIN_SCALE();
		this.MAX_SCALE 				= requestScale.getMAX_SCALE();
		this.INTERVAL				= requestScale.getSamplingInterval();
		this.progressTimeUnit 		= requestScale.getProgressTimeUnit();
	}
	
	public PromiseState getPromiseState() {
		return promiseState;
	}
	public void setPromiseState(PromiseState promiseState) {		
		this.promiseState = promiseState;
	}
	public PromiseStatus getPromiseStatus() {
		return promiseStatus;
	}
	public void setPromiseStatus(PromiseStatus promiseStatus) {		 
		this.promiseStatus = promiseStatus;
	}
	
	public double getMIN_SCALE() {
		return MIN_SCALE;
	}
	public void setMIN_SCALE(double mIN_SCALE) {
		MIN_SCALE = mIN_SCALE;
	}
	public double getMAX_SCALE() {
		return MAX_SCALE;
	}
	public void setMAX_SCALE(double mAX_SCALE) {
		MAX_SCALE = mAX_SCALE;
	}

	public double getScaleUnit() {
		return scaleUnit;
	}
	public void setScaleUnit(double scaleUnit) {
		this.scaleUnit = scaleUnit;
	}
	public TimeUnit getProgressTimeUnit() {
		return progressTimeUnit;
	}
	public void setProgressTimeUnit(TimeUnit progressTimeUnit) {
		this.progressTimeUnit = progressTimeUnit;
	}
	public long getINTERVAL() {
		return INTERVAL;
	}
	public void setINTERVAL(long iNTERVAL) {
		INTERVAL = iNTERVAL;
	}
	public double getNow() {
		return now;
	}
	public void setNow(double now) {
		this.now = now;
	}

	public String getAcknowledgmentIdentifier() {
		return acknowledgmentIdentifier;
	}

	public void setAcknowledgmentIdentifier(String acknowledgmentIdentifier) {
		this.acknowledgmentIdentifier = acknowledgmentIdentifier;
	}
	
	public Hashtable<String, String> getMetaDataAttributes() {
		return this.metaDataAttributes;
	}

	public void setMetaDataAttributes(Hashtable<String, String> metaDataAttributes) {
		this.metaDataAttributes = metaDataAttributes;
	}

	public void setEventOrigin(EventOrigin eventOrigin) {
		this.eventOrigin = eventOrigin;
	}

	public EventOrigin getEventOrigin() {
		return this.eventOrigin;
	}

	public String toString(){
		
		return "\n ScheduledProgressScale{"+
				" | scheduleStatus : "+scheduleStatus+
				" | severity : "+severity+
				" | priority : "+priority+
				" | scheduledTimestamp : "+scheduledTimestamp+
				" | deferredTimestamp : "+deferredTimestamp+
				" | subscriptionClientId : "+subscriptionClientId+
				" | now : "+now+
				" | promiseState : "+promiseState+
				" | promiseStatus : "+promiseStatus+	
				" | acknowledgmentIdentifier : "+acknowledgmentIdentifier+				
				" | MIN_SCALE : "+MIN_SCALE+
				" | MAX_SCALE : "+MAX_SCALE+
				" | scaleUnit : "+scaleUnit+
				" | progressTimeUnit : "+progressTimeUnit+
				" | INTERVAL : "+INTERVAL+
				" | eventOrigin : "+eventOrigin+
				" | metaDataAttributes : "+metaDataAttributes+
				"}";
	}


}
