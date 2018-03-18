package com.example.rurocker.vertex.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
              req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
            }).listen(8080, result -> {
            		if(result.succeeded()) {
            			fut.complete();
            		} else {
            			fut.fail(fut.cause());
            		}
            });
        System.out.println("HTTP server started on port 8080");
    }
}
