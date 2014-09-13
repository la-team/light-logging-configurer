package org.lightadmin.logging.configurer.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import static org.lightadmin.logging.configurer.LightConfigurerWebApplicationInitializer.LIGHT_CONFIGURER_BACK_TO_SITE_URL;
import static org.lightadmin.logging.configurer.LightConfigurerWebApplicationInitializer.LIGHT_CONFIGURER_BASE_URL;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@Controller
public class ApplicationController {

    @Inject
    private ServletContext servletContext;

    @RequestMapping(value = "/", method = GET)
    public String index(Model model) {
        model.addAttribute("baseUrl", baseUri());
        model.addAttribute("backToSiteUrl", backToSiteUrl());
        return "index";
    }

    private String baseUri() {
        String applicationBaseUrl = servletContext.getInitParameter(LIGHT_CONFIGURER_BASE_URL);

        return fromCurrentContextPath().path(applicationBaseUrl).build().toUriString();
    }

    private String backToSiteUrl() {
        String backToSiteUrl = servletContext.getInitParameter(LIGHT_CONFIGURER_BACK_TO_SITE_URL);
        return isEmpty(backToSiteUrl) ? "http://lightadmin.org" : backToSiteUrl;
    }
}