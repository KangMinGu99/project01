package com.model2.mvc.service.product.impl;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.domain.Product;

public class ProductServiceImpl implements ProductService{
	
	private ProductDao productDao;
	
	public ProductServiceImpl() {
		productDao = new ProductDao();
	}
	
	public void addProduct(Product prod) throws Exception{
		productDao.insertProduct(prod);

	}
	
	public Product getProduct(int prodNo) throws Exception{
		return productDao.findProduct(prodNo);
	}
	
	public Map<String,Object> getProductList(Search search) throws Exception {
		return productDao.getProductList(search);
	}
	
	public void updateProduct(Product prod) throws Exception{
		productDao.updateProduct(prod);

	}
	
	
	
	

}