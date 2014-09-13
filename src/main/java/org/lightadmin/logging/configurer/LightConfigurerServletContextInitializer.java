package org.lightadmin.logging.configurer;

import org.springframework.boot.context.embedded.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

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

        new LightConfigurerWebApplicationInitializer().onStartup(servletContext);
    }
}