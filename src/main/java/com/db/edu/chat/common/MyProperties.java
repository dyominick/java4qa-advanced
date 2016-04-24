package com.db.edu.chat.common;

import com.db.edu.chat.server.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by apple on 24.04.16.
 */
public class MyProperties {
    public static String getHost() {
        return host;
    }

    private static String host;

    static InputStream propertiesStream;
   static{
       try {
           Properties prop = new Properties();
           String propFileName = "runtime.properties";
           propertiesStream = Server.class.getClassLoader().getResourceAsStream(propFileName);

           if (propertiesStream != null) {
               prop.load(propertiesStream);
           } else {
               throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
           }
           host = prop.getProperty("host");


       } catch (Exception e) {
           System.out.println("Exception: " + e);
       } finally {
           try {
               propertiesStream.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
}
