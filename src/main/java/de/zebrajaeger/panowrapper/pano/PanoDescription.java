package de.zebrajaeger.panowrapper.pano;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PanoDescription {
  private final String description;

  public PanoDescription(String pDescription) {
    description = pDescription;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
