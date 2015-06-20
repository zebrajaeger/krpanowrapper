package de.zebrajaeger.panowrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppProperties extends Properties {
  private static final String APP_PROPERTIES = "app.properties";
  private static final long serialVersionUID = -4570940279928424187L;

  private static Logger LOG = LoggerFactory.getLogger(AppProperties.class);

  private static AppProperties p = null;

  public static AppProperties get() {
    if (p == null) {
      p = new AppProperties(findPropertyFile());
    }
    return p;
  }

  private static File findPropertyFile() {

    // try to load from working directory
    File p = new File(APP_PROPERTIES);
    if (!p.exists()) {
      final String msg1 = String.format("couldnt found '%s'", p.getAbsolutePath());
      LOG.info(msg1);

      // try to load from sourcedir
      final String codeDir = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      p = new File(codeDir);
      p = new File(p.getParent(), APP_PROPERTIES);
      if (!p.exists()) {
        final String msg2 = String.format("couldnt found '%s'", p.getAbsolutePath());
        LOG.info(msg2);
        final String msg3 = String.format("can not load '%s'", APP_PROPERTIES);
        LOG.error(msg3);
        throw new RuntimeException(msg3);
      }
    }
    LOG.info(String.format("%s loaded from '%s'", APP_PROPERTIES, p.getAbsolutePath()));
    return p;
  }

  private AppProperties(File f) {
    try (FileInputStream fis = new FileInputStream(f)) {
      load(fis);
    } catch (final IOException e) {
      throw new RuntimeException("can not load " + f, e);
    }
  }

  public String get(PROPERTY p) {
    return getProperty(p.getId());
  }

  public Long getLong(PROPERTY p) {
    final String s = get(p);
    try {
      return Long.parseLong(s);
    } catch (final NumberFormatException e) {
      LOG.error(String.format("Can not parse '%s' cause of wron format '%s' ", p, s), e);
      return null;
    }
  }

  public static enum PROPERTY {
    krpano_windows_exec32("krpano.windows.exec32"), krpano_windows_exec64("krpano.windows.exec64"), krpano_config(
        "krpano.config"), krpano_exec_timeout("krpano.exec.timeout"), ;

    private String id;

    PROPERTY(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }
}
