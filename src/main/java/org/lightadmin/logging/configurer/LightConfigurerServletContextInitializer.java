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

import static org.lightadmin.logging.configurer.LoggingConfigurerSettings.*;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@SuppressWarnings("unused")
public class LightConfigurerServletContextInitializer implements ServletContextInitializer {

    private final LoggingConfigurerSettings configurerSettings;

    public LightConfigurerServletContextInitializer(String baseUrl) {
        this.configurerSettings = new LoggingConfigurerSettings(baseUrl);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter(LIGHT_CONFIGURER_BASE_URL, configurerSettings.getApplicationBaseUrl());
        servletContext.setInitParameter(LIGHT_CONFIGURER_BACK_TO_SITE_URL, configurerSettings.getBackToSiteUrl());
        servletContext.setInitParameter(LIGHT_CONFIGURER_DEMO_MODE, Boolean.toString(configurerSettings.isDemoMode()));

        new LightConfigurerWebApplicationInitializer().onStartup(servletContext);
    }

    public LightConfigurerServletContextInitializer backToSiteUrl(String url) {
        this.configurerSettings.setBackToSiteUrl(url);
        return this;
    }

    public LightConfigurerServletContextInitializer demoMode() {
        this.configurerSettings.setDemoMode(true);
        return this;
    }
}