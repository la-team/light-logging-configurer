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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@Service
public class LogbackLoggerManager implements LoggerManager<Logger, Level> {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, String> getLoggerContextParameters() {
        return loggerContext().getCopyOfPropertyMap();
    }

    @Override
    public List<Logger> getLoggers() {
        return loggerContext().getLoggerList();
    }

    @Override
    public Logger changeLevel(String loggerName, Level level) {
        Logger logger = loggerContext().getLogger(loggerName);
        logger.setLevel(level);

        this.logger.info("Logging level changed for {} to {}", logger, level);

        return logger;
    }

    private LoggerContext loggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }
}