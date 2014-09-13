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
package org.lightadmin.logging.configurer;

import org.springframework.boot.context.embedded.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static org.lightadmin.logging.configurer.LightConfigurerWebApplicationInitializer.LIGHT_CONFIGURER_BACK_TO_SITE_URL;
import static org.lightadmin.logging.configurer.LightConfigurerWebApplicationInitializer.LIGHT_CONFIGURER_BASE_URL;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@SuppressWarnings("unused")
public class LightConfigurerServletContextInitializer implements ServletContextInitializer {

    private final String baseUrl;

    public LightConfigurerServletContextInitializer(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter(LIGHT_CONFIGURER_BASE_URL, baseUrl);
        servletContext.setInitParameter(LIGHT_CONFIGURER_BACK_TO_SITE_URL, "http://lightadmin.org");

        new LightConfigurerWebApplicationInitializer().onStartup(servletContext);
    }
}