package com.db.edu.chat.common;

import com.db.edu.chat.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class MyProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyProperties.class);
    private static String host;
    static InputStream propertiesStream;

    private MyProperties() {
    }

    public static String getHost() {
        return host;
    }


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
           LOGGER.error("Properties not found: ", e);
       } finally {
           try {
               if (null!=propertiesStream)
                   propertiesStream.close();
           } catch (IOException e) {
               LOGGER.error("Error while closing properties stream: ",e);
           }
       }
   }
}
