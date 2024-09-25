package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;

//상품 상세 보기 요청
public class GetProductAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService productService = new ProductServiceImpl();
		Product prod = productService.getProduct(prodNo);
		
		request.setAttribute("prod", prod);

		
		return "forward:/product/getProduct.jsp";
	}
}