/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.eg.egsc.framework.service.core.ApplicationStarter;

/**
 * ZuulServerApplication
 * 
 * @author guofeng
 * @since 2018年1月29日
 */
@SpringBootApplication
@EnableZuulProxy
@ComponentScan(basePackages = {"com.eg.egsc"})
public class ZuulServerApplication extends SpringBootServletInitializer {

  /**
   * @see org.springframework.boot.web.support.SpringBootServletInitializer#configure
   *      (org.springframework.boot.builder.SpringApplicationBuilder)
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(ZuulServerApplication.class);
  }

  /**
   * TODO
   * 
   * @param args void
   */
  public static void main(String[] args) {
    ApplicationStarter.run(ZuulServerApplication.class, args);
  }

  /**
   * TODO
   * 
   * @return CorsFilter
   */
  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
