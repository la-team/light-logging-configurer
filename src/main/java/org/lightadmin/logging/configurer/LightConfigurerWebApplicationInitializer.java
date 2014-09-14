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

import org.lightadmin.logging.configurer.config.LightLoggerConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.regex.Pattern;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@SuppressWarnings("unused")
@Order(LOWEST_PRECEDENCE)
public class LightConfigurerWebApplicationInitializer implements WebApplicationInitializer {

    private static final Pattern BASE_URL_PATTERN = Pattern.compile("(/)|(/[\\w-]+)+");

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        LoggingConfigurerSettings configuration = new LoggingConfigurerSettings(servletContext);

        if (lightConfigurerNotEnabled(configuration)) {
            servletContext.log("Light Configurer Module is disabled by default. Skipping.");
            return;
        }

        if (notValidBaseUrl(configuration.getApplicationBaseUrl())) {
            servletContext.log("Light Configurer Module's 'baseUrl' property must match " + BASE_URL_PATTERN.pattern() + " pattern. Skipping.");
            return;
        }

        registerLightConfigurerDispatcher(servletContext, configuration);
        registerHiddenHttpMethodFilter(servletContext, configuration);
        registerCharsetFilter(servletContext, configuration);
    }

    private void registerLightConfigurerDispatcher(final ServletContext servletContext, LoggingConfigurerSettings configuration) {
        final DispatcherServlet lightConfigurerDispatcher = new DispatcherServlet(createApplicationContext());

        ServletRegistration.Dynamic lightConfigurerDispatcherRegistration = servletContext.addServlet("light-configurer-dispatcher", lightConfigurerDispatcher);
        lightConfigurerDispatcherRegistration.setLoadOnStartup(2);
        lightConfigurerDispatcherRegistration.addMapping(dispatcherUrlMapping(configuration.getApplicationBaseUrl()));
    }

    private AnnotationConfigWebApplicationContext createApplicationContext() {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(configurations());
        webApplicationContext.setDisplayName("LightConfigurer WebApplicationContext");
        webApplicationContext.setNamespace("light-configurer");
        return webApplicationContext;
    }

    private Class[] configurations() {
        return new Class[]{LightLoggerConfiguration.class};
    }

    private void registerHiddenHttpMethodFilter(final ServletContext servletContext, LoggingConfigurerSettings configuration) {
        final String urlMapping = urlMapping(configuration.getApplicationBaseUrl());

        servletContext.addFilter("LightConfigurerHiddenHttpMethodFilter", HiddenHttpMethodFilter.class).addMappingForUrlPatterns(null, false, urlMapping);
    }

    private void registerCharsetFilter(ServletContext servletContext, LoggingConfigurerSettings configuration) {
        final String urlMapping = urlMapping(configuration.getApplicationBaseUrl());

        servletContext.addFilter("LightConfigurerCharsetFilter", characterEncodingFilter()).addMappingForServletNames(null, false, urlMapping);
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    private String dispatcherUrlMapping(String url) {
        if (rootUrl(url)) {
            return "/";
        }
        return urlMapping(url);
    }

    private String urlMapping(String baseUrl) {
        if (rootUrl(baseUrl)) {
            return "/*";
        }
        return baseUrl + "/*";
    }

    private boolean rootUrl(final String url) {
        return "/".equals(url);
    }

    private boolean lightConfigurerNotEnabled(final LoggingConfigurerSettings configuration) {
        return isEmpty(configuration.getApplicationBaseUrl());
    }

    private boolean notValidBaseUrl(String url) {
        return !BASE_URL_PATTERN.matcher(url).matches();
    }
}