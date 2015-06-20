package de.zebrajaeger.panowrapper.pano;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PanoView {
  private final String type;
  private final String width;
  private final String height;
  private final String heightShift;

  public PanoView(String txt) {
    // System.out.println("txt: " + txt);

    final StringBuilder sb = new StringBuilder();
    final String nr = "-?\\d+(?:\\.\\d+)?";
    sb.append("^");
    sb.append("([a-zA-Z])");
    sb.append("-");
    sb.append("(").append(nr).append(")");
    sb.append("x");
    sb.append("(").append(nr).append(")");
    sb.append("(?:\\((").append(nr).append(")\\))?");
    sb.append(".*");
    sb.append("$");

    // System.out.println("pattern: " + sb);
    final Pattern p = Pattern.compile(sb.toString());
    final Matcher m = p.matcher(txt);

    if (!m.matches()) {
      throw new IllegalArgumentException();
    }

    type = m.group(1);
    width = m.group(2);
    height = m.group(3);
    heightShift = m.group(4);
    System.out.println(this);
  }


  public String getType() {
    return type;
  }


  public String getWidth() {
    return width;
  }


  public String getHeight() {
    return height;
  }


  public String getHeightShift() {
    return heightShift;
  }


  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
