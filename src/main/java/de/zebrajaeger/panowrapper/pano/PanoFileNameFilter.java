package de.zebrajaeger.panowrapper.pano;

import java.io.File;
import java.util.HashSet;

import org.apache.commons.io.filefilter.AbstractFileFilter;

public class PanoFileNameFilter extends AbstractFileFilter {
  private final HashSet<String> allowed = new HashSet<String>();

  public void addAllowedExtension(String pExt) {
    allowed.add(pExt.toLowerCase());
  }

  String getExt(File f) {
    final String n = f.getName();
    final int pos = n.lastIndexOf('.');
    if (pos == -1) {
      return null;
    }
    return n.substring(pos + 1);
  }

  @Override
  public boolean accept(File pFile) {

    // System.out.println(pFile);
    // / check extension
    final String e = getExt(pFile);
    if (e == null) {
      return false;
    }

    if (!allowed.contains(e.toLowerCase())) {
      return false;
    }

    // check file name structure
    return PanoBuilder.acceptFile(pFile.getName());
  }
}
