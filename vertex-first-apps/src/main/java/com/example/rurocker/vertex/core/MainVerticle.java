package com.example.rurocker.vertex.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> fut) throws Exception {

		// get port from configuration.
		Integer port = config().getInteger("http.port", 8080);

		// Create a router object.
		Router router = Router.router(vertx);
		router.route("/").handler(routingContext -> {
			routingContext.response().putHeader("content-type", "text/plain").end("Hello from Vert.x!");
		});

		// create HTTP server.
		vertx.createHttpServer().requestHandler(router::accept).listen(port, result -> {
			if (result.succeeded()) {
				fut.complete();
			} else {
				fut.fail(fut.cause());
			}
		});
		System.out.printf("HTTP server started on port %d\n", port);
	}
}
