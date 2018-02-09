/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.gateway.filter;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eg.egsc.framework.service.auth.service.AuthenticationService;
import com.eg.egsc.framework.service.util.HttpServletUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Auth Filter
 * 
 * @author guofeng
 * @aince 2018年1月19日
 */
@Component
public class AuthFilter extends ZuulFilter {

  protected final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

  @Value("${egsc.config.auth.enabled:false}")
  private boolean authEnabled;

  @Autowired
  private AuthenticationService authenticationService;

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.IZuulFilter#run()
   */
  @Override
  public Object run() {
    logger.info("Zuul filter start.");
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    ServletResponse response = ctx.getResponse();
    boolean authCheck = authenticationService.authorize(request, response);
    HttpServletUtil.initResponse((HttpServletResponse) response);
    if (authCheck) {
      ctx.setSendZuulResponse(true);
      ctx.setResponseStatusCode(200);
      ctx.set("isSuccess", true);
    } else {
      ctx.setSendZuulResponse(false);
      ctx.setResponseStatusCode(401);
      ctx.set("isSuccess", false);
      HttpServletUtil.populateResponse401(request, response);
    }
    logger.info("Zuul filter end.");
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.IZuulFilter#shouldFilter()
   */
  @Override
  public boolean shouldFilter() {
    return authEnabled;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.ZuulFilter#filterOrder()
   */
  @Override
  public int filterOrder() {
    return 2;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.ZuulFilter#filterType()
   */
  @Override
  public String filterType() {
    return "pre";
  }
}
