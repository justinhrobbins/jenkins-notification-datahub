package org.robbins.resources;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.robbins.constants.JenkinsNotificationExtensionConstants;
import org.robbins.dto.JenkinsNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/jenkins-notification")
public class JenkinsNotificationResource
{
	private static final Logger log = LoggerFactory.getLogger(JenkinsNotificationResource.class);
	private MessageChannel rawFragmentMessageInputChannel;

	@POST
	public Response postJenkinsNotification(final JenkinsNotification notification) throws ExecutionException, InterruptedException
	{
		log.info("Received Jenkins Notification: {}", notification);

		rawFragmentMessageInputChannel.send(createMessage(notification));

		return Response.ok().build();
	}

	private Message createMessage(final JenkinsNotification notification)
	{
		final Map<String, String> attributes = new HashMap<>();
		attributes.put("isoCode", "en");
		attributes.put("name", notification.getName());
		attributes.put("url", notification.getUrl());
		attributes.put("buildUrl", notification.getBuild().getUrl());
		attributes.put("buildFullUrl", notification.getBuild().getFull_url());
		attributes.put("buildNumber", notification.getBuild().getNumber());
		attributes.put("buildPhase", notification.getBuild().getPhase());
		attributes.put("buildStatus", notification.getBuild().getStatus());

		final List<Map<String, String>> payload = new ArrayList<>();
		payload.add(attributes);

		final Map<String, Object> headers = new HashMap<>();
		headers.put("itemType", "JenkinsNotificationRawItem");
		headers.put("feedName", JenkinsNotificationExtensionConstants.FEED_NAME);

		return new GenericMessage<>(payload, headers);
	}

	@Required
	public void setRawFragmentInputChannel(final MessageChannel channel)
	{
		this.rawFragmentMessageInputChannel = channel;
	}
}
