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