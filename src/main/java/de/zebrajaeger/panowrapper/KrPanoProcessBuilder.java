package de.zebrajaeger.panowrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zebrajaeger.panowrapper.pano.Pano;

public class KrPanoProcessBuilder {

  private final Logger LOG = LoggerFactory.getLogger(KrPanoProcessBuilder.class);
  private final File krpano;
  private final File krConfig;


  public String[] buildArgs(Pano pano, boolean allowOverrideXml) {
    // final Pano pano = PanoBuilder.build(img);

    final List<String> parms = new ArrayList<String>();
    parms.add(asCmdArg(krpano));
    // parms.add("tool");
    parms.add("makepano");
    parms.add(asCmdArg(krConfig));
    parms.add(asCmdArg(pano.getFile().getAbsolutePath()));


    if (pano.getView() == null) {
      throw new IllegalArgumentException("image name needs view");
    }

    // TYPE
    final String type = pano.getView().getType();
    if ("S".equalsIgnoreCase(type)) {
      parms.add("-panotype=sphere");
    } else if ("F".equalsIgnoreCase(type)) {
      parms.add("-panotype=planar");
    } else {
      throw new IllegalArgumentException("unknown pano type: " + type);
    }

    // FOV
    parms.add("-hfov=" + pano.getView().getWidth());

    // overwrite
    if (allowOverrideXml) {
      LOG.info("result xml override enabled");
      parms.add("-askforxmloverwrite=false");
    }

    final String[] array = parms.toArray(new String[parms.size()]);

    return array;
  }

  public KrPanoProcessBuilder(File pKrpano, File pKrConfig) {
    super();
    krpano = pKrpano;
    krConfig = pKrConfig;
  }

  public Process createProcess(Pano pano, boolean allowOverrideXml) throws IOException {
    final String[] args = buildArgs(pano, allowOverrideXml);
    return Runtime.getRuntime().exec(args);
  }

  public Process createProcess(String[] args) throws IOException {
    return Runtime.getRuntime().exec(args);
  }

  protected static String asCmdArg(File f) {
    return "\"" + f.getAbsolutePath() + "\"";
  }

  protected static String asCmdArg(String str) {
    return "\"" + str + "\"";
  }

  public File getKrpano() {
    return krpano;
  }

  public File getKrConfig() {
    return krConfig;
  }

}
