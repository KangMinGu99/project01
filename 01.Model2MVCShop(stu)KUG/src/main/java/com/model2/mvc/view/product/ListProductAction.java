package com.model2.mvc.view.product;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

//상품 목록 조회 요청
public class ListProductAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String role = "";
		
		Search search=new Search();
		
		HttpSession session = request.getSession();
		
		
		//요청 파라미터에서 페이지 번호를 가져와서 page 변수에 저장, 기본값은 1.
		int currentPage = 1;
		System.out.println(request.getParameter("currentPage"));
		//"menu" 파라미터가 있는 경우, 그 값에 따라 role을 "manage" 또는 "search"로 설정하고 세션에 저장
		if(request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
			if(request.getParameter("menu")==null){
				session.getAttribute("menu");
			}
		} else {
			if(request.getParameter("menu")!=null){
				if(request.getParameter("menu").equals("manage")){
					role = "manage";
					System.out.println("상품 관리");
					
				} else {
					role = "search";
					System.out.println("상품 검색");
				}
				session.setAttribute("menu", role);
			} else {
				//"menu" 파라미터가 없는 경우, "상품메뉴 parameter : null"을 콘솔에 출력
				System.out.println("상품메뉴 parameter : null");
			}
			
		}
		
		/*
		int currentPage=1;

		if(request.getParameter("currentPage") != null){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		*/
		
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
		
		// web.xml  meta-data 로 부터 상수 추출 
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
		search.setPageSize(pageSize);
		
		// Business logic 수행
		ProductService productService=new ProductServiceImpl();
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage	= 
					new Page( currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("ListProductAction ::"+resultPage);
		
		// Model 과 View 연결
		request.setAttribute("list", map.get("list"));
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
}