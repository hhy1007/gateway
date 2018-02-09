/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.eg.egsc.common.service.mgmt.gateway.dto;


/**
 * ZuulFilterResponseDto
 * 
 * @author guofeng
 * @since 2018年1月29日
 */
public class ZuulFilterResponseDto {

  private String timestamp;

  private String status;

  private String error;

  private String message;

  private String path;

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
