package com.example.vertx_starter.workers;


import com.example.vertx_starter.verticles.MainVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Vertx provides a worker thread pool to execute blocking operations
 * There are two ways to execute blocking operations, either by callaing vertx.executeBloacking or by deploying a worker verticle
 * This class demonstrates the use of executeBlocking
 */
public class WorkerExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // example for demonstrating worker verticle
    // worker verticle is similar to normal verticle except few configuration
    // if we forget below configuration then worker verticle will also execute on event loop instead of worker thread
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true))
    ;

    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    // the first part in eventBlocking, 'event' is a handler which is executing on worker thread 0
    vertx.executeBlocking(event -> {
      LOG.debug("Executing blocking code");
      try {
        Thread.sleep(5000);
        event.complete();
      } catch (InterruptedException e) {
        LOG.error("Failed : ",  e);
        event.fail(e);
      }
      // the second part in eventBlocking, the 'result' is executing on eventloop thread 0
      // second parameter is needed so that the blocking code should return to correct event loop
    }, result->{
      if(result.succeeded()){
        LOG.debug("Blocking call done.");
      } else {
        LOG.debug("Blocking call failed due to:", result.cause());
      }
    });
  }
}
