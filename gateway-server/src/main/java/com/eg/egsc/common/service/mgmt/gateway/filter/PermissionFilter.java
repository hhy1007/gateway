/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.gateway.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eg.egsc.framework.service.auth.service.PermissionService;
import com.eg.egsc.framework.service.util.HttpServletUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Permission Filter
 * 
 * @author guofeng
 * @since 2018年1月29日
 */
@Component
public class PermissionFilter extends ZuulFilter {

  protected final Logger logger = LoggerFactory.getLogger(PermissionFilter.class);

  @Value("${egsc.config.auth.enabled:false}")
  private boolean authEnabled;

  @Autowired
  private PermissionService permissionService;

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
    HttpServletResponse response = ctx.getResponse();
    boolean permissionCheck = permissionService.hasPermission(request, response);
    HttpServletUtil.initResponse((HttpServletResponse) response);
    if (permissionCheck) {
      ctx.setSendZuulResponse(true);
      ctx.setResponseStatusCode(200);
      ctx.set("isSuccess", true);
    } else {
      ctx.setSendZuulResponse(false);
      ctx.setResponseStatusCode(403);
      ctx.set("isSuccess", false);
      HttpServletUtil.populateRespons403(request, response);
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
    return 3;
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
