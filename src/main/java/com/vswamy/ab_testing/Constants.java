package com.vswamy.ab_testing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants
{
    private static Logger logger = LoggerFactory.getLogger(Constants.class);

    public static String AB_TEST_SERVER_HOST;
    public static int AB_TEST_SERVER_PORT;

    public static String REDIS_SERVER_HOST;
    public static int REDIS_SERVER_PORT;

    public static void setConstants(String fileName) throws FileNotFoundException, IOException
    {
        logger.info("Loading properties/config file...");

        Properties p = new Properties();
        p.load(new FileInputStream(fileName));

        AB_TEST_SERVER_HOST = p.getProperty("AB_TEST_SERVER_HOST");
        AB_TEST_SERVER_PORT = Integer.parseInt(p.getProperty("AB_TEST_SERVER_PORT"));

        REDIS_SERVER_HOST = p.getProperty("REDIS_SERVER_HOST");
        REDIS_SERVER_PORT = Integer.parseInt(p.getProperty("REDIS_SERVER_PORT"));
        
        logger.info("Loding properties/config complete! ...");
    }
}
