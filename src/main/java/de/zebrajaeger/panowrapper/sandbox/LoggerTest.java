package de.zebrajaeger.panowrapper.sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {
  public static void main(String[] args) {

    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
    System.setProperty(org.slf4j.impl.SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_THREAD_NAME_KEY, "true");
    System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
    System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_DATE_TIME_KEY, "true");


    final Logger LOG = LoggerFactory.getLogger(LoggerTest.class);

    System.out.println(LoggerFactory.getILoggerFactory().getClass());

    LOG.trace("trace");
    LOG.debug("debug");
    LOG.info("info");
    LOG.warn("warn");
    LOG.error("err");
  }

}
