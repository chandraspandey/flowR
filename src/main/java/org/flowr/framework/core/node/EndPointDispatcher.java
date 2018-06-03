package org.flowr.framework.core.node;

import java.util.Collection;

import org.flowr.framework.core.event.Event;
import org.flowr.framework.core.event.pipeline.EventPipeline;
import org.flowr.framework.core.model.EventModel;
import org.flowr.framework.core.notification.Notification.NotificationProtocolType;
import org.flowr.framework.core.notification.NotificationQueue.QueueStagingType;
import org.flowr.framework.core.notification.subscription.NotificationSubscription;
import org.flowr.framework.core.service.ServiceEndPoint;
import org.flowr.framework.core.service.internal.NotificationService.NotificationServiceStatus;

/**
 * 
 * 
 * @author Chandra Shekhar Pandey
 * Copyright � 2018 by Chandra Shekhar Pandey. All rights reserved.
 */

public interface EndPointDispatcher {

	public void configure(NotificationProtocolType notificationProtocolType, Collection<ServiceEndPoint> serviceEndPointList
		, EventPipeline eventPipeline);
	
	public void handle(QueueStagingType queueStagingType);
	
	public NotificationServiceStatus dispatch(Event<EventModel> event);
	
	public Collection<ServiceEndPoint> getEndPointList() ;

	public Collection<NotificationSubscription> getNotificationProtocolTypelist(NotificationProtocolType 
			notificationProtocolType);

}
