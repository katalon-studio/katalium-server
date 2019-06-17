package com.katalon.kata.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorService {

  private int MAX_PARALLEL = 5;

  private ThreadPoolExecutor executorService;

  private ExecutorService(){
    executorService = new ThreadPoolExecutor(
        MAX_PARALLEL,
        Integer.MAX_VALUE,
        0L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>());
  }

  private static class ThreadSingletonHolder{
    private static final ExecutorService INSTANCE = new ExecutorService();
  }

  public static ExecutorService getInstance() {
    return ThreadSingletonHolder.INSTANCE;
  }

  public void execute(Runnable runnable) {
    executorService.execute(runnable);
  }

}
