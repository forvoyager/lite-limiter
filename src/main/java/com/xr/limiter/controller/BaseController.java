package com.xr.limiter.controller;

import com.xr.limiter.util.FreemarkerUtil;
import com.xr.limiter.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-06 10:46
 * @Description:
 */
@Controller
@RequestMapping(BaseController.BASE_PATH)
public class BaseController {
  
  public static final String BASE_PATH = "/limiter";

  protected FreemarkerUtil freemarkerUtil;
  
  private HttpServletResponse response;
  private HttpServletRequest request;

  @RequestMapping("/index")
  public void index() throws Exception {
    response.setContentType("text/html");
    parseView(freemarkerUtil.parse("index", Utils.newHashMap()));
  }
  
  protected void parseView(String content) throws IOException {
    ServletOutputStream out = response.getOutputStream();
    out.write(content.getBytes("UTF-8"));
    out.flush();
    out.close();
  }

  @Autowired
  public void setFreemarkerUtil() throws IOException {
    this.freemarkerUtil = new FreemarkerUtil();
  }

  @ModelAttribute
  private final void init(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }
}
