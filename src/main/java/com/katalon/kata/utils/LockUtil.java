package com.katalon.kata.utils;

import java.util.concurrent.Callable;

public class LockUtil {

  public static <T> T read(String filePath, Callable<T> callable) {
    ReadWriteLockManager.getInstance().lockRead(filePath);
    try {
      return callable.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      ReadWriteLockManager.getInstance().unlockRead(filePath);
    }
  }

  public static void write(String filePath, Runnable runnable) {
    ReadWriteLockManager.getInstance().lockWrite(filePath);
    try {
      runnable.run();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      ReadWriteLockManager.getInstance().unlockWrite(filePath);
    }
  }

  public static <V> V write(String filePath, Callable<V> callable) {
    ReadWriteLockManager.getInstance().lockWrite(filePath);
    try {
      return callable.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      ReadWriteLockManager.getInstance().unlockWrite(filePath);
    }
  }
}
