package com.xr.limiter.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-06 13:06
 * @Description: 限流配置信息
 */
@Configuration
@PropertySource("classpath:limiter.properties")
public class LimiterConfig {

  /**
   * 是否启动限流 true对所有访问进行限流 false不限流
   */
  @Value("${limiter.available}")
  private boolean limiterAvailable;

  /**
   * 不受限流控制的URL白名单，多个用英文逗号隔开，例如：/,/limiter/*
   * 默认：/limiter/* 对限流模块相关的操作
   */
  @Value("${limiter.whitelist}")
  private String whiteList = "/limiter/*";
  
  /**
   * 配置信息存储类型 localhost（默认），redis
   */
  @Value("${limiter.store.type}")
  private String storeType;

  public boolean isLimiterAvailable() {
    return limiterAvailable;
  }

  public LimiterConfig setLimiterAvailable(boolean limiterAvailable) {
    this.limiterAvailable = limiterAvailable;
    return this;
  }

  public String getWhiteList() {
    return whiteList;
  }

  public LimiterConfig setWhiteList(String whiteList) {
    this.whiteList = whiteList;
    return this;
  }

  public String getStoreType() {
    return storeType;
  }

  public LimiterConfig setStoreType(String storeType) {
    this.storeType = storeType;
    return this;
  }
}
