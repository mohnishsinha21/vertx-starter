package com.example.vertx_starter.workers;

import com.example.vertx_starter.verticles.MainVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *  All code inside this verticle will be executed on worker thread
 */
public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Deployed as worker verticle");
    startPromise.complete();
    Thread.sleep(5000);
  }
}
