package com.company.supershop.security;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SuperShopWebAppInitializer /*implements WebApplicationInitializer*/ {

    /*@Override
    public void onStartup(ServletContext container) {
      XmlWebApplicationContext appContext = new XmlWebApplicationContext();
      appContext.setConfigLocation("/WEB-INF/dispatcher-servlet.xml");

      //container.addListener(new ContextLoaderListener(appContext));
      
      ServletRegistration.Dynamic dispatcher =
        container.addServlet("dispatcher", new DispatcherServlet(appContext));
      dispatcher.setLoadOnStartup(1);
      dispatcher.addMapping("/");
      
      container.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
      	.addMappingForUrlPatterns(null, false, "/*");
      

      
    }*/


    
 }