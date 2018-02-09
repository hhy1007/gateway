/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.gateway.filter;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eg.egsc.common.component.audit.dto.AuditLogDto;
import com.eg.egsc.common.component.audit.service.AuditLogService;
import com.eg.egsc.common.service.mgmt.gateway.dto.ZuulFilterResponseDto;
import com.eg.egsc.framework.service.util.MessageUtils;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Log ErrorFilter
 * 
 * @author guofeng
 * @since 2018年1月29日
 */
@Component
public class LogErrorRoutingFilter extends ZuulFilter {

  private static Logger log = LoggerFactory.getLogger(LogErrorRoutingFilter.class);
  
  private static final String SYSTEM_INTENAL_ERROR = "系统内部错误";

  @Override
  public String filterType() {
    return "error";
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    return false;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    log.info("LogErrorRoutingFilter start");
    log.info(ctx.getResponseBody());
    HttpServletRequest request = ctx.getRequest();
    String responseData = null;
    String uri = request.getRequestURI();
    if (StringUtils.contains(uri, "/api/")) {
      responseData = JSONObject.toJSON(MessageUtils.getResponseByMessage(SYSTEM_INTENAL_ERROR)).toString();
    }else{
      responseData = SYSTEM_INTENAL_ERROR;
    }
    ctx.setResponseBody(responseData);
    log.info("LogErrorRoutingFilter end");
    return null;

  }
}
