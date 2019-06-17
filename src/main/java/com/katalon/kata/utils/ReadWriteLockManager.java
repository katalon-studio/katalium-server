package com.katalon.kata.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

class ReadWriteLockManager {

  private Cache<String, Object> cache = CacheBuilder.newBuilder()
      .expireAfterAccess(1, TimeUnit.HOURS)
      .build();

  private ReadWriteLockManager(){

  }

  private static class ReadWriteFileLockHelper{
    private static final ReadWriteLockManager INSTANCE = new ReadWriteLockManager();
  }

  static ReadWriteLockManager getInstance(){
    return ReadWriteFileLockHelper.INSTANCE;
  }

  void lockRead(String key) {
    getLock(key).readLock().lock();
  }

  void unlockRead(String key) {
    getLock(key).readLock().unlock();
  }

  void lockWrite(String key) {
    getLock(key).writeLock().lock();
  }

  void unlockWrite(String key) {
    getLock(key).writeLock().unlock();
  }

  private ReentrantReadWriteLock getLock(String key) {
    ReentrantReadWriteLock lock = (ReentrantReadWriteLock) cache.getIfPresent(key);
    if (lock == null) {
      lock = createLock(key);
    }
    return lock;
  }

  private synchronized ReentrantReadWriteLock createLock(String key) {
    ReentrantReadWriteLock lock = (ReentrantReadWriteLock) cache.getIfPresent(key);
    if (lock == null) {
      lock = new ReentrantReadWriteLock();
      cache.put(key, lock);
    }
    return lock;
  }
}
