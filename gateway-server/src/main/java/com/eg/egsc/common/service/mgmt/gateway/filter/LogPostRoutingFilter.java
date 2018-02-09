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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eg.egsc.common.component.audit.service.AuditLogService;
import com.eg.egsc.common.service.mgmt.gateway.dto.ZuulFilterResponseDto;
import com.eg.egsc.framework.client.dto.ResponseDto;
import com.eg.egsc.framework.service.util.MessageUtils;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * Log PostFilter
 * 
 * @author guofeng
 * @since 2018年1月29日
 */
@Component
public class LogPostRoutingFilter extends ZuulFilter {

  protected final Logger logger = LoggerFactory.getLogger(LogPostRoutingFilter.class);

  @Autowired
  private AuditLogService auditLogService;

  private static final String SYSTEM_INTENAL_ERROR = "系统内部错误";

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.IZuulFilter#run()
   */
  @Override
  public Object run() {
    logger.info("LogPostRoutingFilter start.");
    RequestContext ctx = RequestContext.getCurrentContext();
    String responseData = null;
    boolean isDownload = false;
    HttpServletRequest request = ctx.getRequest();
    HttpServletResponse response = ctx.getResponse();
    try {
      if (request.getRequestURL() != null
          && (request.getRequestURL().toString().contains("export") || request.getRequestURL()
              .toString().contains("download"))
          || request.getRequestURL().toString().contains(".xlsx")) {
        isDownload = true;
      } else {
        InputStream responseDataStream = ctx.getResponseDataStream();
        responseData =
            responseDataStream == null ? null : CharStreams.toString(new InputStreamReader(
                responseDataStream, "UTF-8"));
      }
    } catch (Exception e) {
      logger.error("Get responseData failed.", e);
    }
    auditLogService.postRoutingFilter(request, response, responseData, isDownload);
    ctx.setSendZuulResponse(true);
    ctx.setResponseStatusCode(response.getStatus());
    if (response.getStatus() != 200) {
      ZuulFilterResponseDto zuulErrorDto = getZullErrorDto(responseData);
      if (zuulErrorDto != null) {
        String errorMessage = zuulErrorDto.getMessage();
        responseData = errorMessageToJson(request, errorMessage);
      }
      
      if(responseData == null) {
        responseData = SYSTEM_INTENAL_ERROR;
      }
    }
    ctx.setResponseBody(responseData);
    logger.info("LogPostRoutingFilter end.");
    return null;
  }

  /**
   * TODO
   * 
   * @param responseData
   * @param zuulErrorDto
   * @return ZuulFilterResponseDto
   */
  private ZuulFilterResponseDto getZullErrorDto(String responseData) {
    ZuulFilterResponseDto zuulErrorDto = null;
    try {
      zuulErrorDto = JSON.parseObject(responseData, ZuulFilterResponseDto.class);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return zuulErrorDto;
  }

  public String errorMessageToJson(HttpServletRequest request, String errorMessage) {
    String responseData = null;
    if (StringUtils.contains(request.getRequestURI(), "/api/")) {
      responseData = JSONObject.toJSON(MessageUtils.getResponseByMessage(errorMessage)).toString();
    } else {
      responseData = errorMessage;
    }
    return responseData;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.netflix.zuul.IZuulFilter#shouldFilter()
   */
  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    return (boolean) ctx.get("isSuccess");
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
    return "post";
  }
}
