package de.zebrajaeger.panowrapper.sandbox;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import de.zebrajaeger.panowrapper.pano.PanoFileNameFilter;

public class ListFilesTest {

  public static void main(String[] args) {
    final PanoFileNameFilter f = new PanoFileNameFilter();
    f.addAllowedExtension("psd");
    f.addAllowedExtension("psb");
    f.addAllowedExtension("jpg");
    f.addAllowedExtension("jpeg");
    f.addAllowedExtension("tiff");

    final File root = new File("r:/!pano");
    final Collection<File> files = FileUtils.listFiles(root, f, TrueFileFilter.INSTANCE);
    for (final File file : files) {
      System.out.println(file);
    }
  }
}
