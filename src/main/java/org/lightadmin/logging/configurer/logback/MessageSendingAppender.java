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