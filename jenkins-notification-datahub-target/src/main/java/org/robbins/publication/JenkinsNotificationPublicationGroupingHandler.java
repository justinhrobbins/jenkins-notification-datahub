/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package org.robbins.publication;

import com.hybris.datahub.grouping.PublicationGroupingHandler;
import com.hybris.datahub.grouping.TargetItemCreationContext;
import com.hybris.datahub.model.CanonicalItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;


@ManagedResource(
		objectName = JenkinsNotificationPublicationGroupingHandler.OBJECT_NAME,
		description = "Jenkins Notification Publication Grouping Handler")
public class JenkinsNotificationPublicationGroupingHandler implements PublicationGroupingHandler
{
	public static final String OBJECT_NAME = "com.hybris.datahub:name=jenkinsNotificationTransformer";

	private static final Logger logger = LoggerFactory.getLogger(JenkinsNotificationPublicationGroupingHandler.class);

	private static final String CANONINICAL_TYPE = "JenkinsNotificationCanonicalItem";
	private static final String BUID_STATUS = "buildStatus";
	private static final String STATUS = "SUCCESS";

	private int order;

	@Override
	public <T extends CanonicalItem> List<T> group(T item, TargetItemCreationContext context)
	{
		logger.debug("Grouping CanonicalItem: " + item.toString());
		return item.getField(BUID_STATUS).equals(STATUS) ? new ArrayList<>() : Arrays.asList(item);
	}

	@Override
	public <T extends CanonicalItem> boolean isApplicable(T item, TargetItemCreationContext context)
	{
		return item.getType().equals(CANONINICAL_TYPE);
	}

	@ManagedOperation(description="Return relative execution order")
	@Override
	public int getOrder()
	{
		return 0;
	}

	@ManagedOperation(description="Set relative execution order")
	@Required
	public void setOrder(int order)
	{
		this.order = order;
	}
}
