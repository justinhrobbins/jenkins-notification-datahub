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
package org.robbins.adapter;

import com.hybris.datahub.adapter.AdapterService;
import com.hybris.datahub.api.publication.PublicationException;
import com.hybris.datahub.domain.TargetItemMetadata;
import com.hybris.datahub.dto.item.ErrorData;
import com.hybris.datahub.dto.publication.PublicationResult;
import com.hybris.datahub.model.TargetItem;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.paging.DefaultDataHubPageRequest;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import com.hybris.datahub.service.PublicationActionService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.robbins.domain.JenkinsNotificationTargetItem;
import org.robbins.raspberry.pi.client.PiActionClient;
import org.robbins.raspberry.pi.model.PiAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class JenkinsNotificationTargetSystemAdapter implements AdapterService {

    private static final Logger logger = LoggerFactory.getLogger(JenkinsNotificationTargetSystemAdapter.class);
    private static final String TARGET_SYSTEM_TYPE = "JenkinsNotificationTargetSystemAdapter";
    private static final int PAGE_SIZE = 10;
    private static final String ACTION_NAME = "playSound";
    private static final String ACTION_VALUE = "TPS_REPORT.wav";

    private PublicationActionService publicationActionService;
    private PiActionClient piActionClient;

    @Override
    public String getTargetSystemType() {
        return TARGET_SYSTEM_TYPE;
    }

    @Override
    public void publish(final TargetSystemPublication targetSystemPublication, final String s) throws PublicationException {
        List<ErrorData> errors = publishTargetItems(targetSystemPublication);
        completePublication(targetSystemPublication, errors);
    }

    private List<ErrorData> publishTargetItems(final TargetSystemPublication targetSystemPublication)
    {
        logger.info("Publishing {}", targetSystemPublication);

        final List<ErrorData> errors = new ArrayList<>();
        for (TargetItemMetadata itemMetadata : targetSystemPublication.getTargetSystem().getTargetItemMetadata())
        {
            final Class<? extends TargetItem> targetItemType = TargetItem.getItemClass(itemMetadata.getItemType());
            int pageNumber = 0;
            List<? extends TargetItem> items;
            do
            {
                final DataHubPageable pageable = new DefaultDataHubPageRequest(pageNumber, PAGE_SIZE);
                DataHubPage<? extends TargetItem> page = publicationActionService.findByPublication(targetSystemPublication.getPublicationId(), targetItemType, pageable);
                items = page.getContent();
                for (final TargetItem targetItem : items)
                {
                    sendTargetItem(targetItem, errors);
                }
                pageNumber ++;
            } while(CollectionUtils.isNotEmpty(items));
        }
        return errors;
    }

    private void sendTargetItem(final TargetItem targetItem, final List<ErrorData> errors)
    {
        try {
            if (targetItem instanceof JenkinsNotificationTargetItem)
            {
                PiAction piAction = createPiActionFromTargetItem((JenkinsNotificationTargetItem)targetItem);

                piActionClient.postAction(piAction);
            }
        }
        catch (Exception e) {
            logger.error("Failed to publish target item: " + targetItem.toString());
            errors.add(buildPublicationError(targetItem, e));
        }
    }

    private PiAction createPiActionFromTargetItem(final JenkinsNotificationTargetItem targetItem)
    {
        PiAction piAction = new PiAction();
        piAction.setName(ACTION_NAME);
        piAction.setValue(ACTION_VALUE);
        logger.debug(piAction.toString());
        return piAction;
    }

    private ErrorData buildPublicationError(final TargetItem targetItem, final Exception e)
    {
        final ErrorData errorData = new ErrorData();
        errorData.setCanonicalItemId(targetItem.getCanonicalItem().getId());
        errorData.setCode("publication failure");
        errorData.setMessage(e.getMessage());
        return errorData;
    }

    private void completePublication(final TargetSystemPublication targetSystemPublication, final List<ErrorData> errors) {
        final PublicationResult publicationResult = new PublicationResult();
        publicationResult.setExportErrorDatas(errors);
        publicationActionService.completeTargetSystemPublication(targetSystemPublication.getPublicationId(), publicationResult);
    }

    @Required
    public void setPublicationActionService(PublicationActionService publicationActionService) {
        this.publicationActionService = publicationActionService;
    }

    @Required
    public void setPiActionClient(final PiActionClient piActionClient)
    {
        this.piActionClient = piActionClient;
    }
}
