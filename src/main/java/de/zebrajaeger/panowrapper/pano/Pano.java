package de.zebrajaeger.panowrapper.pano;

import java.io.File;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Pano {
  private final File file;
  private PanoInfo info;
  private PanoView view;
  private PanoDescription description;


  public Pano(File pFile) {
    file = pFile;
  }

  public PanoInfo getInfo() {
    return info;
  }

  public PanoView getView() {
    return view;
  }

  public PanoDescription getDescription() {
    return description;
  }

  protected void setInfo(PanoInfo pInfo) {
    info = pInfo;
  }

  protected void setView(PanoView pView) {
    view = pView;
  }

  protected void setDescription(PanoDescription pDescription) {
    description = pDescription;
  }

  public File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
