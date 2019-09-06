package com.xr.limiter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-06 11:02
 * @Description:
 */
public final class Utils {
  
  private Utils(){}
  
  public static String toString(InputStream is) throws IOException {
    StringBuilder content = new StringBuilder();
    
    if(is == null){
      return content.toString();
    }

    String line = null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    try {
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
    } finally {
      is.close();
    }
    
    return content.toString();
  }
  
  public static <K, V> Map<K, V> newHashMap(Object ...args){
    Map map = new HashMap();
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        map.put(args[i], args[++i]);
      }
    }
    return map;
  }
  
}
