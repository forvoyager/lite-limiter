package com.xr.limiter.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-06 10:46
 * @Description: Freemarker操作工具类
 */
public class FreemarkerUtil {

  private Configuration configuration = null;
  private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  public FreemarkerUtil() throws IOException {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource[] coreResources = resolver.getResources("tpl/limiter/**/*.ftl");
    StringTemplateLoader stringTempLoader = new StringTemplateLoader();
    for (Resource r : coreResources) {
      stringTempLoader.putTemplate(r.getFilename(), Utils.toString(r.getInputStream()));
      logger.info("template {}, loaded.", r.getFilename());
    }
    configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
    configuration.setTemplateLoader(stringTempLoader);
    configuration.setDefaultEncoding("UTF-8");
    configuration.setClassicCompatible(true);
  }

  /**
   * 
   * @param template 模板路径和名称
   * @param param 替换参数
   * @return
   * @throws IOException
   * @throws TemplateException
   */
  public String parse(String template, Map<String, Object> param) throws IOException, TemplateException {
    StringWriter sw = new StringWriter();
    configuration.getTemplate(template + ".ftl").process(param, sw);
    return sw.toString();
  }

}
