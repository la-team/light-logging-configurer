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
package org.lightadmin.logging.configurer.web;

import org.lightadmin.logging.configurer.LoggingConfigurerSettings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

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
    private LoggingConfigurerSettings loggingConfigurerSettings;

    @RequestMapping(value = "/", method = GET)
    public String index(Model model) {
        model.addAttribute("baseUrl", absoluteBaseUrl());
        model.addAttribute("backToSiteUrl", loggingConfigurerSettings.getBackToSiteUrl());
        model.addAttribute("demoMode", loggingConfigurerSettings.isDemoMode());
        return "index";
    }

    private String absoluteBaseUrl() {
        return fromCurrentContextPath().path(loggingConfigurerSettings.getApplicationBaseUrl()).build().toUriString();
    }
}