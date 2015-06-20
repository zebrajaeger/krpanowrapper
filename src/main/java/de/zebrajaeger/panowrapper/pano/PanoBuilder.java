package de.zebrajaeger.panowrapper.pano;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class PanoBuilder {

  private final static Pattern FILE_PATTERN;
  static {
    final StringBuilder sb = new StringBuilder();
    sb.append("^\\(").append("([^-]+)").append("-").append("([^-]+)").append("-").append("(\\d+)").append("\\)");
    sb.append("((?:-\\{[^\\}]*\\})*)");
    sb.append("\\.(.{3})");
    sb.append("$");

    // System.out.println(sb);
    FILE_PATTERN = Pattern.compile(sb.toString());
  }

  public static Matcher getMatcherFor(String txt) {
    return FILE_PATTERN.matcher(txt);
  }

  public static boolean acceptFile(String txt) {
    return FILE_PATTERN.matcher(txt).matches();
  }

  /**
   * returns a string with extension otherwise null
   */
  public static String getExtensionIfMatches(String txt) {
    final Matcher m = FILE_PATTERN.matcher(txt);
    if (m.matches()) {
      return m.group(5);
    }
    return null;
  }

  // final String name = "(IMG_0833-IMG_0836-4)-{v=S-80.84x53.86(-8.27)}-{x=IMG_0833_IMG_0836-4 (2009-08-04)}.psd";
  public static Pano build(File img) {

    final Matcher m = getMatcherFor(img.getName());

    if (!m.matches()) {
      throw new IllegalArgumentException();
    }

    final Pano pano = new Pano(img);

    final PanoInfo panoInfo = new PanoInfo(m.group(1), m.group(1), Integer.parseInt(m.group(3)), m.group(5));
    pano.setInfo(panoInfo);

    // parse additional data
    String additional = m.group(4);
    if (!StringUtils.isEmpty(additional)) {
      additional = additional.substring(2, additional.length() - 1);
      final String[] parts = additional.split("\\}-\\{");

      for (final String part : parts) {
        if (part.startsWith("d=")) {
          pano.setView(new PanoView(part.substring(2)));
        } else if (part.startsWith("p=")) {
          pano.setDescription(new PanoDescription(part.substring(2)));
        }
      }
    }
    return pano;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  public static void main(String[] args) {
    final Pano pano =
        build(new File("(IMG_0833-IMG_0836-4)-{d=S-80.84x53.86(-8.27)}-{p=IMG_0833_IMG_0836-4 (2009-08-04)}.psd"));
    System.out.println(pano);
  }
}
