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
package org.lightadmin.logging.configurer.json;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.MoreObjects;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class JsonLoggingEvent {
    private final String loggerName;
    private final Level level;
    private final String message;

    JsonLoggingEvent(ILoggingEvent loggingEvent) {
        this.message = loggingEvent.getFormattedMessage();
        this.level = loggingEvent.getLevel();
        this.loggerName = loggingEvent.getLoggerName();
    }

    public static JsonLoggingEvent wrap(ILoggingEvent loggingEvent) {
        return new JsonLoggingEvent(loggingEvent);
    }

    public String getMessage() {
        return message;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public Level getLevel() {
        return level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("logger", loggerName)
                .add("level", level)
                .add("message", message)
                .toString();
    }
}