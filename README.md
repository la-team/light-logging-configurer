# Pluggable Java web library for runtime logging level configuration

The goal of the project is to provide a logger configuration UI for changing logging levels directly from web interface.

## License ##

* <b>Light Logging Configurer</b> is released under version 2.0 of the Apache License.

## Integration examples ##

* [LightAdmin and Spring Boot](https://github.com/la-team/lightadmin-springboot)
* [LightAdmin DEMO](https://github.com/la-team/lightadmin-demo)

## Documentation & Support ##

* Web site: [lightadmin.org](http://lightadmin.org)
* Live demo: [lightadmin.org/demo/logger](http://lightadmin.org/demo/logger/)
* CI Server: [lightadmin.org/jenkins](http://lightadmin.org/jenkins)
* Use Google Groups for posting questions: [groups.google.com/group/lightadmin](http://groups.google.com/group/lightadmin)
* Use Stack Overflow for posting questions with <b>lightadmin</b> tag
* Contact LightAdmin Team directly on Twitter: <b>@lightadm_team</b>

## Getting started ##

Light Logging Configurer is designed in such a way that it should integrate with your existing Java web applications with very little effort.

To install Light Logging Configurer alongside your application, simply add the required dependency, enable its WebApplicationInitializer using servlet context init parameters or register our ServletContextInitializer implementation if you're running Spring Boot app. The last step would be to adjust your logback configuration with "websocket-aware" appender.

### Adding Light Logging Configurer to a Maven project ###
To add Light Logging Configurer to a Maven-based project, add the light-logging-configurer artifact to your compile-time dependencies:

```xml
<dependency>
  <groupId>org.lightadmin</groupId>
  <artifactId>light-logging-configurer</artifactId>
  <version>1.0.0.RC1</version>
</dependency>
```

### Configuring Light Logging Configurer web module in Servlet 3.0 environment ###
To install Light Logging Configurer alongside your existing web application, you need to include the appropriate configuration to your WebApplicationInitializer.

```java
@Override
public void onStartup(ServletContext servletContext) throws ServletException {
  servletContext.setInitParameter(LIGHT_CONFIGURER_BASE_URL, "/logger");
  servletContext.setInitParameter(LIGHT_CONFIGURER_BACK_TO_SITE_URL, "http://lightadmin.org");
}
```

The equivalent of the above in a standard <b>web.xml</b> will also work identically to this configuration. 

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0" metadata-complete="false">
         
  <context-param>
    <param-name>light:configurer:base-url</param-name>
    <param-value>/logger</param-value>
  </context-param>

  <context-param>
    <param-name>light:configurer:back-to-site-url</param-name>
    <param-value>http://lightadmin.org</param-value>
  </context-param>

</web-app>
```

### Configuring Light Logging Configurer web module for Spring Boot application ###
To install Light Logging Configurer alongside your existing Spring Boot web application, you need to register ServletContextInitializer.

```java
public class Application extends SpringBootServletInitializer {
  @Bean
  public ServletContextInitializer lightConfigurerServletContextInitializer() {
      return new LightConfigurerServletContextInitializer("/logger");
  }
}
```

### Configuring Light Logging Configurer Logback appender ###


```xml
<configuration>
  <appender name="MessageSendingAppender" class="org.lightadmin.logging.configurer.logback.MessageSendingAppender"/>
  
  <logger name="org.lightadmin" level="INFO"/>
  
  <root level="INFO">
    <appender-ref ref="MessageSendingAppender"/>
  </root>
</configuration>
```

## Screenshots

<b>Main Screen:</b>

![Main Screen](https://github.com/la-team/light-logging-configurer/raw/master/screenshots/main.png "Main Screen")
