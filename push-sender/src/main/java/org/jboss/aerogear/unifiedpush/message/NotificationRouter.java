/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import javax.inject.Inject;

import org.jboss.aerogear.unifiedpush.api.FlatPushMessageInformation;
import org.jboss.aerogear.unifiedpush.api.PushApplication;
import org.jboss.aerogear.unifiedpush.api.Variant;
import org.jboss.aerogear.unifiedpush.api.VariantType;
import org.jboss.aerogear.unifiedpush.message.holder.MessageHolderWithVariants;
import org.jboss.aerogear.unifiedpush.message.token.TokenLoader;
import org.jboss.aerogear.unifiedpush.service.GenericVariantService;
import org.jboss.aerogear.unifiedpush.service.VerificationService;
import org.jboss.aerogear.unifiedpush.service.metrics.IPushMessageMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.TopicProcessor;

/**
 * Takes a request for sending {@link UnifiedPushMessage} and submits it to messaging subsystem for further processing.
 *
 * Router splits messages to specific variant types (push network type) so that they can be processed separately,
 * giving attention to limitations and requirements of specific push networks.
 *
 * {@link NotificationRouter} receives a request for sending a {@link UnifiedPushMessage} and queues one message per variant type, both in transaction.
 * The transactional behavior makes sure the request for sending notification is recorded and then asynchronously processed.
 *
 * The further processing of the push message happens in {@link TokenLoader}.
 */
@Service
public class NotificationRouter {

    private final Logger logger = LoggerFactory.getLogger(NotificationRouter.class);

    @Inject
    private GenericVariantService genericVariantService;
    @Inject
    private IPushMessageMetricsService metricsService;

    @Inject
    private TopicProcessor<MessageHolderWithVariants> dispatchVariantMessageEvent;

    /**
	 * Receives a request for sending a {@link UnifiedPushMessage} and queues
	 * one message per variant type, both in one transaction.
     *
	 * Once this method returns, message is recorded and will be eventually
	 * delivered in the future.
     *
	 * @param pushApplication
	 *            the push application
	 * @param message
	 *            the message
     */
    @Transactional
    public void submit(PushApplication pushApplication, InternalUnifiedPushMessage message) {
        logger.debug("Processing send request with '{}' payload", message.getMessage());

        // collections for all the different variants:
        final VariantMap variants = new VariantMap();

        final List<String> variantIDs = message.getCriteria().getVariants();

        // if the criteria payload did specify the "variants" field,
        // we look up each of those mentioned variants, by their "variantID":
        if (variantIDs != null) {

            variantIDs.forEach(variantID -> {
                Variant variant = genericVariantService.findByVariantID(variantID);

                // does the variant exist ?
                if (variant != null) {
                    variants.add(variant);
                }
            });
        } else {
            // No specific variants have been requested,
            // we get all the variants, from the given PushApplicationEntity:
            variants.addAll(pushApplication.getVariants());
        }

        // TODO: Not sure the transformation should be done here...
		// There are likely better places to check if the metadata is way to
		// long
        String jsonMessageContent = message.toStrippedJsonString() ;
        if (jsonMessageContent != null && jsonMessageContent.length() >= 4500) {
            jsonMessageContent = message.toMinimizedJsonString();
        }

        final FlatPushMessageInformation pushMessageInformation =
                metricsService.storeNewRequestFrom(
                        pushApplication.getPushApplicationID(),
                        jsonMessageContent,
                        message.getIpAddress(),
                        message.getClientIdentifier()
                );

		// we split the variants per type since each type may have its own
		// configuration (e.g. batch size)
        variants.forEach((variantType, variant) -> {
        	if (variant != null && !variant.isEmpty()){
        		logger.info(String.format("Internal dispatching of push message for one %s variant (by %s)", variantType.getTypeName(), message.getClientIdentifier()));
        		dispatchVariantMessageEvent.onNext(new MessageHolderWithVariants(pushMessageInformation, message, variantType, variant));
        	}
        });
    }

	/**
	 * Map for storing variants split by the variant type
	 */
	private static class VariantMap extends EnumMap<VariantType, List<Variant>> {
		private static final long serialVersionUID = -1942168038908630961L;
		private static final Logger logger = LoggerFactory.getLogger(NotificationRouter.class);
		VariantMap() {
			super(VariantType.class);
		}

		void add(Variant variant) {
			List<Variant> list = this.get(variant.getType());
			if (list == null) {
				list = new ArrayList<>();
				this.put(variant.getType(), list);
			}

			// Prevent dev/null messages from being sent.
        	if (VerificationService.isDevNullVariant(variant.getName())){
        		logger.warn("Push message requst to {} variant has been aborted!", variant.getName());
        		return;
        	}

			list.add(variant);
		}

		void addAll(Collection<Variant> variants) {
			variants.forEach(this::add);
		}

	}
}
