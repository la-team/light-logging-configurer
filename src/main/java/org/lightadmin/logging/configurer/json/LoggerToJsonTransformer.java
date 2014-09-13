package org.lightadmin.logging.configurer.json;

import ch.qos.logback.classic.Logger;
import com.google.common.base.Function;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class LoggerToJsonTransformer implements Function<Logger, JsonLogger> {

    private static final LoggerToJsonTransformer INSTANCE = new LoggerToJsonTransformer();

    private LoggerToJsonTransformer() {
    }

    public static Function<Logger, JsonLogger> toJson() {
        return INSTANCE;
    }

    @Override
    public JsonLogger apply(Logger logger) {
        return new JsonLogger(logger);
    }
}