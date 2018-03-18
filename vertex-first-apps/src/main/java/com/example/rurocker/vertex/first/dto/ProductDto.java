package com.example.rurocker.vertex.first.dto;

import java.util.concurrent.atomic.AtomicInteger;

public class ProductDto {

	private static final AtomicInteger COUNTER = new AtomicInteger();
	
	private Integer productId;
	private String productCode;
	private String productName;

	public ProductDto() {
		this(null, null);
	}
	
	public ProductDto(String productCode, String productName) {
		this.productId = COUNTER.incrementAndGet();
		this.productCode = productCode;
		this.productName = productName;
	}



	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
