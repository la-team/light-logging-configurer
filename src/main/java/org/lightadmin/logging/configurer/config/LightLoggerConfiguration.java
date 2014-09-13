package org.lightadmin.logging.configurer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.lightadmin.logging.configurer"})
public class LightLoggerConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/styles/**").addResourceLocations("classpath:/static/styles/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/static/scripts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }

    @Bean
    protected SpringResourceResourceResolver thymeleafResourceResolver() {
        return new SpringResourceResourceResolver();
    }

    @Bean
    public TemplateResolver templateResolver() {
        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(thymeleafResourceResolver());
        resolver.setPrefix("classpath:/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[]{"*"});
        viewResolver.setCache(false);
        return viewResolver;
    }

    private SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }
}