package com.example.rurocker.vertex.first;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.rurocker.vertex.first.dto.ProductDto;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

	private Vertx vertx;
	private Integer port;

	@Before
	public void setUp(TestContext context) throws IOException {

		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();

		vertx = Vertx.vertx();
		
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));
		vertx.deployVerticle(MainVerticle.class.getName(), options, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testMainVerticle(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(port, "localhost", "/", response -> {
			context.assertEquals(response.statusCode(), 200);
		    context.assertEquals(response.headers().get("content-type"), "text/plain");
			response.handler(body -> {
				context.assertTrue(body.toString().contains("Hello"));
				async.complete();
			});
		});
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetProdcuts(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(port, "localhost", "/api/products", response -> {
			context.assertEquals(response.statusCode(), 200);
			context.assertTrue(response.headers().get("content-type").contains("application/json"));
			response.handler(body -> {
				JsonArray jsonArray = body.toJsonArray();
				
				List<Map<String,ProductDto>> list = jsonArray.getList();
				context.assertEquals(list.size(), 2);
				
				Map<String, ProductDto> map1 = list.get(0);
				context.assertEquals(map1.get("productId"), 1);
				context.assertEquals(map1.get("productCode"), "001");
				context.assertEquals(map1.get("productName"), "Product 1");
				
				Map<String, ProductDto> map2 = list.get(1);
				context.assertEquals(map2.get("productId"), 2);
				context.assertEquals(map2.get("productCode"), "002");
				context.assertEquals(map2.get("productName"), "Product 2");
				
				async.complete();
			});
		});
	}
	
	@Test
	public void testThatWeCanAddProduct(TestContext context) {
	  Async async = context.async();
	  final String json = Json.encodePrettily(new ProductDto("100", "Product 100"));
	  final String length = Integer.toString(json.length());
	  vertx.createHttpClient().post(port, "localhost", "/api/products")
	      .putHeader("content-type", "application/json")
	      .putHeader("content-length", length)
	      .handler(response -> {
	        context.assertEquals(response.statusCode(), 201);
	        context.assertTrue(response.headers().get("content-type").contains("application/json"));
	        response.bodyHandler(body -> {
	          final ProductDto dto = Json.decodeValue(body.toString(), ProductDto.class);
	          context.assertEquals(dto.getProductCode(), "100");
	          context.assertEquals(dto.getProductName(), "Product 100");
	          context.assertNotNull(dto.getProductId());
	          async.complete();
	        });
	      })
	      .write(json)
	      .end();
	}
}
