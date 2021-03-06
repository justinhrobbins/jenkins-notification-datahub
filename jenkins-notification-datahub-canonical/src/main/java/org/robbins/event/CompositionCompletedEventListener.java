package org.robbins.event;

import com.hybris.datahub.api.event.CompositionCompletedEvent;
import com.hybris.datahub.api.event.DataHubEventListener;
import com.hybris.datahub.api.event.InitiatePublicationEvent;
import com.hybris.datahub.service.EventPublicationService;
import com.hybris.datahub.service.impl.AbstractPoolEventListener;

import java.util.Arrays;

import org.robbins.constants.JenkinsNotificationExtensionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CompositionCompletedEventListener extends AbstractPoolEventListener
		implements DataHubEventListener<CompositionCompletedEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(CompositionCompletedEventListener.class);

	private EventPublicationService eventPublicationService;

	@Override
	public void handleEvent(final CompositionCompletedEvent event)
	{
		final String poolName = getPoolNameFromId(event.getPoolId());
		if (JenkinsNotificationExtensionConstants.POOL_NAME.equals(poolName))
		{
			logger.debug("Handling composition completed event for pool : {}", JenkinsNotificationExtensionConstants.POOL_NAME);
			final InitiatePublicationEvent publishEvent = new InitiatePublicationEvent(event.getPoolId(),
                    Arrays.asList(JenkinsNotificationExtensionConstants.TARGET_SYSTEM_NAME));
			eventPublicationService.publishEvent(publishEvent);
		}
	}

	@Override
	public Class<CompositionCompletedEvent> getEventClass()
	{
		return CompositionCompletedEvent.class;
	}

	@Override
	public boolean executeInTransaction()
	{
		return true;
	}

	@Required
	public void setEventPublicationService(final EventPublicationService eventPublicationService)
	{
		this.eventPublicationService = eventPublicationService;
	}
}
