package com.example.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start {}", getClass().getName());

    // this is how we deploy verticle
    vertx.deployVerticle(new VerticleA());
    vertx.deployVerticle(new VerticleB());

    // here each instance of vertx will get the same configration
    // therefore the id and name field of verticleN will be same in all the instances
    // with that, we can ensure each verticle will get the same configuration
    // so, its not possible to pass different kind of configs if the verticle is deployed multiple times

    // if we want to deploy mutltiple instances of verticle, we pass class name instead of new instance
    vertx.deployVerticle(VerticleN.class.getName(),
      new DeploymentOptions()
        .setInstances(4)
        .setConfig(new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", VerticleN.class.getSimpleName())
        )
    );
    startPromise.complete();
  }
}
