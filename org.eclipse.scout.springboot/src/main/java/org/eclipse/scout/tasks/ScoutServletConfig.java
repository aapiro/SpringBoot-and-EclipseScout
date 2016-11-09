package org.eclipse.scout.tasks;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.commons.authentication.FormBasedAccessController;
import org.eclipse.scout.rt.server.commons.authentication.TrivialAccessController;
import org.eclipse.scout.rt.ui.html.UiHttpSessionListener;
import org.eclipse.scout.rt.ui.html.UiServlet;
import org.eclipse.scout.tasks.scout.auth.CredentialVerifier;
import org.eclipse.scout.tasks.scout.auth.UiServletFilter;
import org.eclipse.scout.tasks.scout.platform.ScoutSpringWebappListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class ScoutServletConfig {

  private static final String CONTEXT_PATH = "/*";
  public static final String SERVICES_PATH = "/api";
  public static final String CONSOLE_PATH = "/h2-console";

  @Bean
  public ServletListenerRegistrationBean<ServletContextListener> webappEventListener() {
    return new ServletListenerRegistrationBean<>(new ScoutSpringWebappListener());
  }

  @Bean
  public ServletListenerRegistrationBean<HttpSessionListener> uiHttpSessionListener() {
    return new ServletListenerRegistrationBean<>(new UiHttpSessionListener());
  }

  @Bean
  public ServletRegistrationBean dispatcherRegistration(WebApplicationContext webApplicationContext) {
    return new ServletRegistrationBean(new UiServlet(), CONTEXT_PATH);
  }

  @Bean
  public UiServletFilter uiServletFilter(TrivialAccessController trivialAccessController, FormBasedAccessController formBasedAccessController, CredentialVerifier credentialVerifier) {
    return new UiServletFilter(trivialAccessController, formBasedAccessController, credentialVerifier);
  }

  @Bean
  public CredentialVerifier credentialVerifier() {
    return BEANS.get(CredentialVerifier.class);
  }

  @Bean
  public FilterRegistrationBean authenticationFilter(UiServletFilter uiServletFilter) {
    final FilterRegistrationBean reg = new FilterRegistrationBean();
    reg.setFilter(uiServletFilter);
    reg.addUrlPatterns(CONTEXT_PATH);
    reg.addInitParameter("filter-exclude", "/res/*, " + SERVICES_PATH + "/*, " + CONSOLE_PATH + "/*");
    reg.setName("authFilter");
    reg.setDispatcherTypes(DispatcherType.REQUEST); // apply this filter only for requests, but not for forwards or redirects.
    return reg;
  }

}
