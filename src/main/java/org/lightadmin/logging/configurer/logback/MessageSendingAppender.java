/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.logging.configurer.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lightadmin.logging.configurer.json.JsonLoggingEvent;
import org.springframework.messaging.core.MessageSendingOperations;

public class MessageSendingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final ObjectMapper objectMapper;

    private MessageSendingOperations<String> messagingTemplate;

    public MessageSendingAppender() {
        this.objectMapper = new ObjectMapper();
        this.messagingTemplate = null;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (this.messagingTemplate == null) {
            return;
        }
        convertAndSend(eventObject);
    }

    private void convertAndSend(ILoggingEvent eventObject) {
        try {
            String jsonLoggingEvent = this.objectMapper.writeValueAsString(JsonLoggingEvent.wrap(eventObject));
            this.messagingTemplate.convertAndSend("/topic/logs", jsonLoggingEvent);
        } catch (Exception e) {
            addError("Exception while attempting to broadcast logging event message", e);
        }
    }

    public void setMessagingTemplate(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
}