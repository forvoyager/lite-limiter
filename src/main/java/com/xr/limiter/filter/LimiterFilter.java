package com.xr.limiter.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.xr.limiter.common.LimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-06 11:43
 * @Description: 限流拦截器
 */
@Component
public class LimiterFilter implements ApplicationContextAware, Filter {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private ApplicationContext applicationContext;

  private PathMatcher pathMatcher = new AntPathMatcher();
  
  @Autowired
  private LimiterConfig limiterConfig;

  /**
   * 限流白名单列表（不受限流控制的URL）
   */
  private List<String> whiteList = new ArrayList<>();

  // 每秒1个许可
  final RateLimiter rateLimiter = RateLimiter.create(0.1);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    /**
     * 1、加载限流配置
     */
    logger.info("init limiter start...");

    whiteList.addAll( Arrays.asList(limiterConfig.getWhiteList().split(",")) );
    
    logger.info("init limiter success.");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    // 如果限流开启则进行限流控制，否则直接放过
    if(limiterConfig.isLimiterAvailable()){
      String url = ((HttpServletRequest)request).getServletPath();
      
      // 过滤限流白名单
      if(skipWhiteList( url )){
        logger.info("limit for url:{}", url);
        if(!rateLimiter.tryAcquire()){
          throw new RuntimeException("限流。。。。");
        }
      }
    }

    chain.doFilter(request, response);
  }

  /**
   * 跳过限流白名单中的URL，剩下的url是需要限流的。
   * 
   * @param url
   * @return
   */
  private boolean skipWhiteList(String url){
    for(String regex : whiteList){
      if(pathMatcher.match(regex, url)) {
        return false; // 白名单中的URL不限流
      }
    }
    
    return true;
  }
  
  @Override
  public void destroy() {
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public LimiterConfig getLimiterConfig() {
    return limiterConfig;
  }

  public LimiterFilter setLimiterConfig(LimiterConfig limiterConfig) {
    this.limiterConfig = limiterConfig;
    return this;
  }

  private String getConfigValue(String key){
    return this.applicationContext.getEnvironment().getProperty(key);
  }
}
