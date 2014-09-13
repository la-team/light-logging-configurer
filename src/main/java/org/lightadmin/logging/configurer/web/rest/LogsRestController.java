package org.lightadmin.logging.configurer.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.lightadmin.logging.configurer.json.JsonLogger;
import org.lightadmin.logging.configurer.logback.LoggerManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.collect.Lists.transform;
import static org.lightadmin.logging.configurer.json.LoggerToJsonTransformer.toJson;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@RestController
@RequestMapping("/rest/logs")
public class LogsRestController {

    @Inject
    private LoggerManager<Logger, Level> loggerManager;

    @RequestMapping(value = "/hostname", method = GET, produces = APPLICATION_JSON_VALUE)
    public String loggerParameters() {
        return loggerManager.getLoggerContextParameters().get("HOSTNAME");
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public List<JsonLogger> listLoggers() {
        return transform(loggerManager.getLoggers(), toJson());
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE)
    public JsonLogger changeLevel(@RequestBody JsonLogger jsonLogger) {
        Logger logger = loggerManager.changeLevel(jsonLogger.getName(), jsonLogger.getLevel());

        return toJson().apply(logger);
    }
}