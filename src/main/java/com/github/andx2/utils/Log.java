package com.github.andx2.utils;


import com.github.andx2.Application;

public class Log {
    private static org.slf4j.Logger logger;

    public static synchronized org.slf4j.Logger getLogger() {
        if (logger == null) {
            logger = org.slf4j.LoggerFactory.getLogger(Application.class);
        }
        return logger;
    }

    public static void d(String message) {
        getLogger().debug(message);
    }

    public static void i(String message) {
        getLogger().info(message);
    }
}
