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

import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class LoggingConfigurerSettings {

    private static final String LIGHT_CONFIGURER_BACK_TO_SITE_DEFAULT_URL = "http://lightadmin.org";

    public static final String LIGHT_CONFIGURER_BASE_URL = "light:configurer:base-url";
    public static final String LIGHT_CONFIGURER_BACK_TO_SITE_URL = "light:configurer:back-to-site-url";
    public static final String LIGHT_CONFIGURER_DEMO_MODE = "light:configurer:demo-mode";

    private final String applicationBaseUrl;
    private final String backToSiteUrl;
    private final boolean demoMode;

    public LoggingConfigurerSettings(ServletContext servletContext) {
        this.applicationBaseUrl = servletContext.getInitParameter(LIGHT_CONFIGURER_BASE_URL);
        this.backToSiteUrl = backToSiteUrl(servletContext);
        this.demoMode = Boolean.valueOf(servletContext.getInitParameter(LIGHT_CONFIGURER_DEMO_MODE));
    }

    public String getApplicationBaseUrl() {
        return applicationBaseUrl;
    }

    public String getBackToSiteUrl() {
        return backToSiteUrl;
    }

    public boolean isDemoMode() {
        return demoMode;
    }

    private String backToSiteUrl(ServletContext servletContext) {
        final String backToSiteUrl = servletContext.getInitParameter(LIGHT_CONFIGURER_BACK_TO_SITE_URL);
        return StringUtils.isEmpty(backToSiteUrl) ? LIGHT_CONFIGURER_BACK_TO_SITE_DEFAULT_URL : backToSiteUrl;
    }
}