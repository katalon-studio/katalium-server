package com.katalon.kata.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

  private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  public static void createFolder(String path) {
    LockUtil.write(path, () -> {
      File sessionFolder = new File(path);
      if (!sessionFolder.exists() || !sessionFolder.isDirectory()) {
        sessionFolder.mkdirs();
      }
    });
  }

  public static <T> T read(String path, Class<T> clazz) throws IOException {
    return LockUtil.read(path, () -> {
      File sessionFile = new File(path);
      InputStream inputStream = FileUtils.openInputStream(sessionFile);
      T result = objectMapper.readValue(inputStream, clazz);
      return result;
    });
  }

  public static boolean createFile(File file) {
    return LockUtil.write(file.getAbsolutePath(), () -> {
      try {
        return file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
