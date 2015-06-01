package com.if2c.harald.tools;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class BackupLog {

    private static final Logger logger = Logger.getLogger(BackupLog.class);

    public BackupLog() {
        super();
    }

    /**
     * logConfig writes a message related to configuration information. Default to use App logger.
     * 
     * @param String message
     * @exception throws Exception
     */
    public static void logDebug(String message) {
        logger.debug(message);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logError(String message, Exception e) {
        logger.error(message, e);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }

    public static void logFatal(String message) {
        logger.fatal(message);
    }

	// public static void errorLogOpt(String operator, String operation, Object
	// obj) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("[").append(operator).append("] ").append(operation).append(obj.getClass().getName()).append(JSON.toJSONString(obj));
	// logger.error(sb.toString());
	// }
}
