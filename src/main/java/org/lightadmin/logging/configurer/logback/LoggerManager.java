package org.lightadmin.logging.configurer.logback;

import java.util.List;
import java.util.Map;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public interface LoggerManager<LOGGER, LEVEL> {

    Map<String, String> getLoggerContextParameters();

    List<LOGGER> getLoggers();

    LOGGER changeLevel(String logger, LEVEL level);
}