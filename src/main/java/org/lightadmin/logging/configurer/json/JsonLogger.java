package org.lightadmin.logging.configurer.json;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.base.MoreObjects;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class JsonLogger {

    private String name;
    private Level level;

    public JsonLogger(Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel();
    }

    @JsonCreator
    public JsonLogger() {
    }

    public String getName() {
        return name;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public Level getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("level", level)
                .toString();
    }
}