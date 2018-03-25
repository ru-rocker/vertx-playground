package com.example.rurocker.vertex.first;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.rurocker.vertex.first.dto.ProductDto;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

	private Map<Integer, ProductDto> data = new LinkedHashMap<>();

	@Override
	public void start(Future<Void> fut) throws Exception {

		// populate products
		populateProducts();

		// get port from configuration.
		Integer port = config().getInteger("http.port", 8080);

		// Create a router object.
		Router router = Router.router(vertx);
		router.route("/").handler(routingContext -> {
			routingContext.response().putHeader("content-type", "text/plain").end("Hello from Vert.x! " + Thread.currentThread().getName());
		});

		router.route("/api/products*").handler(BodyHandler.create());
		router.get("/api/products").handler(this::getAll);
		router.get("/api/products/:id").handler(this::getOne);
		router.post("/api/products").handler(this::createOne);

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

	private void createOne(RoutingContext routingContext) {
		final ProductDto dto = Json.decodeValue(routingContext.getBodyAsString(), ProductDto.class);
		data.put(dto.getProductId(), dto);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(dto));

	}

	private void getAll(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(data.values()));
	}

	private void getOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		Integer idAsInteger = Integer.valueOf(id);
		ProductDto dto = data.get(idAsInteger);
		if (dto == null) {
			routingContext.response().end();
		} else {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(dto));
		}
	}

	private void populateProducts() {
		ProductDto dto1 = new ProductDto("001", "Product 1");
		data.put(dto1.getProductId(), dto1);

		ProductDto dto2 = new ProductDto("002", "Product 2");
		data.put(dto2.getProductId(), dto2);
	}
}
