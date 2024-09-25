package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;

//상품 정보 수정을 위한 화면요청
public class UpdateProductViewAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String prodNo = (String)request.getParameter("prodNo");
		
		ProductService service = new ProductServiceImpl();
		Product prod = service.getProduct(Integer.parseInt(prodNo));
		
		//.setAttribute("속성값 이름", 속성값);
		request.setAttribute("product", prod);
		
		return "forward:/product/updateProductView.jsp";
	}
}