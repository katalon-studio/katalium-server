package com.katalon.kata.utils;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class ImageUtil {

  public static BufferedImage base64ToImage(String base64) throws IOException {
    byte[] imageByte;
    BASE64Decoder decoder = new BASE64Decoder();
    imageByte = decoder.decodeBuffer(base64);
    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
    BufferedImage image = ImageIO.read(bis);
    bis.close();
    return image;
  }

  public static String imageToBase64(File imageFile) {
    return LockUtil.read(imageFile.getAbsolutePath(), () -> {
      String base64Image = "";
      try (FileInputStream imageInFile = new FileInputStream(imageFile)) {
        byte imageData[] = new byte[(int) imageFile.length()];
        imageInFile.read(imageData);
        base64Image = Base64.getEncoder().encodeToString(imageData);
      } catch (FileNotFoundException e) {
        System.out.println("Image not found" + e);
      } catch (IOException ioe) {
        System.out.println("Exception while reading the Image " + ioe);
      }
      return base64Image;
    });
  }

  public static void write(File imageFile, BufferedImage image) {
    LockUtil.write(imageFile.getAbsolutePath(), () -> {
      try {
        ImageIO.write(image, "png", imageFile);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
