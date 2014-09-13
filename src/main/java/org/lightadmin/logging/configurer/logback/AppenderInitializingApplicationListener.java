package org.lightadmin.logging.configurer.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.core.MessageSendingOperations;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Iterators.filter;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class AppenderInitializingApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final MessageSendingOperations<String> messagingTemplate;

    public AppenderInitializingApplicationListener(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggers = context.getLoggerList();

        for (Logger logger : loggers) {
            Iterator<Appender<ILoggingEvent>> loggersIterator = logger.iteratorForAppenders();
            Iterator<MessageSendingAppender> filteredLoggersIterator = filter(loggersIterator, MessageSendingAppender.class);

            while (filteredLoggersIterator.hasNext()) {
                filteredLoggersIterator.next().setMessagingTemplate(messagingTemplate);
            }
        }
    }
}