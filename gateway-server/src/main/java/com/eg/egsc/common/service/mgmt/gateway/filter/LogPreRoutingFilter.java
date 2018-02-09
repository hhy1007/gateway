/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eg.egsc.common.component.audit.service.AuditLogService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Log PreFilter
 * 
 * @author guofeng
 * @since 2018年1月29日
 */
@Component
public class LogPreRoutingFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(LogPreRoutingFilter.class);

  @Autowired
  private AuditLogService auditLogService;

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.IZuulFilter#run()
   */
  @Override
  public Object run() {
    logger.info("LogPreRoutingFilter start.");
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    logger.info("Authorization: " + request.getHeader("Authorization"));
    ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
    logger.info("FrontType: " + request.getHeader("FrontType"));
    ctx.addZuulRequestHeader("FrontType", request.getHeader("FrontType"));
    logger.info("TokenMaxAge: " + request.getHeader("TokenMaxAge"));
    ctx.addZuulRequestHeader("TokenMaxAge", request.getHeader("TokenMaxAge"));
    logger.info("RequestURL: " + request.getRequestURL());
    logger.info("RemoteAddr: " + request.getRemoteAddr());
    auditLogService.preRoutingFilter(request);
    ctx.setSendZuulResponse(true);
    ctx.setResponseStatusCode(200);
    ctx.set("isSuccess", true);
    logger.info("LogPreRoutingFilter end.");
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.IZuulFilter#shouldFilter()
   */
  @Override
  public boolean shouldFilter() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.ZuulFilter#filterOrder()
   */
  @Override
  public int filterOrder() {
    return 1;
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
