package org.robbins.event;

import com.hybris.datahub.api.event.DataHubEventListener;
import com.hybris.datahub.api.event.DataHubInitializationCompletedEvent;
import com.hybris.datahub.service.DataHubFeedService;
import com.hybris.datahub.validation.ValidationException;

import org.robbins.constants.JenkinsNotificationExtensionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates a FEED after the initialization of the Data Hub.
 */
public class FeedCreationListener implements DataHubEventListener<DataHubInitializationCompletedEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(FeedCreationListener.class);

	private DataHubFeedService feedService;


	@Override
	public void handleEvent(final DataHubInitializationCompletedEvent event)
	{
		logger.info("Checking for existing of event driven feed");
		if (feedService.findDataFeedByName(JenkinsNotificationExtensionConstants.FEED_NAME) == null)
		{
			try
			{
				logger.debug("Attempting to create event driven feed");
				feedService.createFeed(JenkinsNotificationExtensionConstants.FEED_NAME, JenkinsNotificationExtensionConstants.POOL_NAME,
						"MANUAL", "MANUAL", "NAMED_POOL", "A test feed to demonstrate " +
								"event driven composition and publication");
			}
			catch (final ValidationException e)
			{
				logger.error("Error creating feed", e);
			}
		}
	}

	@Override
	public Class<DataHubInitializationCompletedEvent> getEventClass()
	{
		return DataHubInitializationCompletedEvent.class;
	}

	@Override
	public boolean executeInTransaction()
	{
		return true;
	}

	@Required
	public void setFeedService(final DataHubFeedService feedService)
	{
		this.feedService = feedService;
	}
}
