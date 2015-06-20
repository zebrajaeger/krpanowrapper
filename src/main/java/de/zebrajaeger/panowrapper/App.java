package de.zebrajaeger.panowrapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zebrajaeger.panowrapper.pano.Pano;
import de.zebrajaeger.panowrapper.pano.PanoBuilder;
import de.zebrajaeger.panowrapper.pano.PanoFileNameFilter;
import de.zebrajaeger.panowrapper.wd.Watchdog;
import de.zebrajaeger.panowrapper.wd.WatchdogListener;

public class App {
  private final static Logger LOG;
  static {
    // INIT LOGGING
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
    System.setProperty(org.slf4j.impl.SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_THREAD_NAME_KEY, "true");
    System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
    System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_DATE_TIME_KEY, "true");
    LOG = LoggerFactory.getLogger(App.class);
  }

  public static void main(String[] args) throws IOException, InterruptedException {

    // APP PROPERTIES
    final AppProperties props = AppProperties.get();

    // ARGS VALID?
    if (args.length == 0) {
      printUsage();
      System.exit(1);
    }

    // CHECK IF FILE EXISTS
    final File file = new File(args[0]);
    if (!file.exists()) {
      throw new IllegalArgumentException(String.format("File/Folder '%s' does not exist", file.getAbsolutePath()));
    }

    // FILE? process it otherwise search in directory and subdirectories and process all pano images
    if (file.isFile()) {
      processPano(file, props);
    } else if (file.isDirectory()) {
      final Collection<File> images = findPanoFiles(file);
      for (final File img : images) {
        processPano(img, props);
      }
    }

    // THATS IT
    LOG.info("Exit App");
    System.exit(0);
  }

  private static void processPano(File img, final AppProperties props) throws IOException, InterruptedException {
    LOG.info("Process pano image '%s'", img.getAbsolutePath());

    // MAKE FILENAME TO PANO
    final Pano pano = PanoBuilder.build(img);
    LOG.info("Pano loaded");
    LOG.info(pano.toString());

    // RESULT ALREADY EXISTS ?
    final File resultDir = resultDir(pano);
    System.out.println(resultDir.getAbsolutePath());
    if (resultDir.exists()) {
      LOG.warn(String.format("the result directory '%s' already exists", resultDir.getAbsolutePath()));
    }

    // GET EXE AND CONFIG AND PREPARE BUILDER
    final File exe = new File(props.get(AppProperties.PROPERTY.krpano_windows_exec32));
    final File cfg = new File(props.get(AppProperties.PROPERTY.krpano_config));

    LOG.info(String.format("Start process '%s' with config '%s", exe.getAbsolutePath(), cfg.getAbsolutePath()));
    final KrPanoProcessBuilder b = new KrPanoProcessBuilder(exe, cfg);

    // CREATE PROCESS
    final Process p = b.createProcess(pano, true);

    // WATCHDOG
    Watchdog wd = null;
    final Long timeout = props.getLong(AppProperties.PROPERTY.krpano_exec_timeout);
    if (timeout == null) {
      LOG.info("got no timeout -> no Watchdog");
    } else {
      LOG.info(String.format("timeout is '%s' -> start watchdog", timeout));
      wd = new Watchdog(timeout);
      wd.addListener(new WatchdogListener() {
        @Override
        public void timeout() {
          LOG.error("TIMEOUT -> kill process");
          p.destroy();
        }
      });
    }

    // CREATE STREAM PUMPS
    final StreamPump out = new StreamPump("IN", p.getInputStream(), System.out, wd);
    final StreamPump err = new StreamPump("ERR", p.getErrorStream(), System.out, wd);

    // WAIT FOR TERMINATION
    p.waitFor();
    LOG.debug("Process finished");
    out.join();
    LOG.debug("Outthread finished");
    err.join();
    LOG.debug("Errthread finished");
  }

  public static void printUsage() {
    System.out.println("<exec> <img>");
  }

  public static void print(String[] x) {
    for (final String s : x) {
      System.out.print(s);
      System.out.print(" ");
    }
    System.out.println();
  }

  public static File resultDir(Pano pano) {
    String name = pano.getFile().getName();
    final int pos = name.lastIndexOf('.');
    name = name.substring(0, pos);
    name = name.replace('=', '_');
    name = name.replace('.', '_');
    name = name.replace(' ', '_');
    final String file = "result-" + name;
    return new File(pano.getFile().getParent(), file);
  }

  protected static Collection<File> findPanoFiles(File root) {
    final PanoFileNameFilter f = new PanoFileNameFilter();
    f.addAllowedExtension("psd");
    f.addAllowedExtension("psb");
    f.addAllowedExtension("jpg");
    f.addAllowedExtension("jpeg");
    f.addAllowedExtension("tiff");

    return FileUtils.listFiles(root, f, TrueFileFilter.INSTANCE);
  }
}
