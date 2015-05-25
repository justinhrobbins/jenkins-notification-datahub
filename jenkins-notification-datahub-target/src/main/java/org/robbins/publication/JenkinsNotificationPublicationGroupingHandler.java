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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("#{'${publishable.status.list}'.split(',')}")
	private List<String> publishableStatuses;

	private int order;

	@Override
	public <T extends CanonicalItem> List<T> group(T item, TargetItemCreationContext context)
	{
		logger.debug("Grouping CanonicalItem: " + item.toString());
		return publishableStatuses.contains(item.getField(BUID_STATUS)) ? Arrays.asList(item) : new ArrayList<>();
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
