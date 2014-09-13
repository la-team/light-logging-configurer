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

    public static final String LIGHT_CONFIGURER_BASE_URL = "light:configurer:base-url";

    private static final Pattern BASE_URL_PATTERN = Pattern.compile("(/)|(/[\\w-]+)+");

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (lightConfigurerNotEnabled(servletContext)) {
            servletContext.log("Light Configurer Module is disabled by default. Skipping.");
            return;
        }

        if (notValidBaseUrl(lightConfigurerBaseUrl(servletContext))) {
            servletContext.log("Light Configurer Module's 'baseUrl' property must match " + BASE_URL_PATTERN.pattern() + " pattern. Skipping.");
            return;
        }

        registerLightConfigurerDispatcher(servletContext);
        registerHiddenHttpMethodFilter(servletContext);
        registerCharsetFilter(servletContext);
    }

    private void registerLightConfigurerDispatcher(final ServletContext servletContext) {
        final DispatcherServlet lightConfigurerDispatcher = new DispatcherServlet(createApplicationContext());

        ServletRegistration.Dynamic lightConfigurerDispatcherRegistration = servletContext.addServlet("light-configurer-dispatcher", lightConfigurerDispatcher);
        lightConfigurerDispatcherRegistration.setLoadOnStartup(2);
        lightConfigurerDispatcherRegistration.addMapping(dispatcherUrlMapping(lightConfigurerBaseUrl(servletContext)));
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

    private void registerHiddenHttpMethodFilter(final ServletContext servletContext) {
        final String urlMapping = urlMapping(lightConfigurerBaseUrl(servletContext));

        servletContext.addFilter("LightConfigurerHiddenHttpMethodFilter", HiddenHttpMethodFilter.class).addMappingForUrlPatterns(null, false, urlMapping);
    }

    private void registerCharsetFilter(final ServletContext servletContext) {
        final String urlMapping = urlMapping(lightConfigurerBaseUrl(servletContext));

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

    private boolean lightConfigurerNotEnabled(final ServletContext servletContext) {
        return isEmpty(lightConfigurerBaseUrl(servletContext));
    }

    private boolean notValidBaseUrl(String url) {
        return !BASE_URL_PATTERN.matcher(url).matches();
    }

    private String lightConfigurerBaseUrl(ServletContext servletContext) {
        return servletContext.getInitParameter(LIGHT_CONFIGURER_BASE_URL);
    }
}