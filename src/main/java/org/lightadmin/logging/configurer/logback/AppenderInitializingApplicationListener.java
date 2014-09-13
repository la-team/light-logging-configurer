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