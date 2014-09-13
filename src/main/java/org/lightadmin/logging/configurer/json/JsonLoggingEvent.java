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