package de.zebrajaeger.panowrapper.pano;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PanoInfo {
  private final String from;
  private final String to;
  private final int count;
  private final String ext;

  public PanoInfo(String pFrom, String pTo, int pCount, String pExt) {
    from = pFrom;
    to = pTo;
    count = pCount;
    ext = pExt;
  }


  public String getFrom() {
    return from;
  }


  public String getTo() {
    return to;
  }


  public int getCount() {
    return count;
  }

  public String getExt() {
    return ext;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
