package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;

public class ProductDao {

	public ProductDao() {
	}
	
	public void insertProduct(Product prod) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT "+
							"INTO product "+
							"VALUES(seq_product_prod_no.nextval,?,?,?,?,?,SYSDATE)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setString(1, prod.getProdName());
		pStmt.setString(2, prod.getProdDetail());
		pStmt.setString(3, prod.getManuDate());
		pStmt.setInt(4, prod.getPrice());
		pStmt.setString(5, prod.getFileName());
		
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
		
	}
	
	public Product findProduct(int prodNo) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT "+
							" prod_no , prod_name , prod_detail , manufacture_day , price , image_file , reg_date "+
							" FROM product WHERE prod_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setInt(1, prodNo);
		
		ResultSet rs = pStmt.executeQuery();
		
		Product prod = null;
		while(rs.next()) {
			prod = new Product();
			prod.setProdNo(rs.getInt("prod_no"));
			prod.setProdName(rs.getString("prod_name"));
			prod.setProdDetail(rs.getString("prod_detail"));
			prod.setManuDate(rs.getString("manufacture_day"));
			prod.setPrice(rs.getInt("price"));
			prod.setFileName(rs.getString("image_file"));
			prod.setRegDate(rs.getDate("reg_date"));	
		}
		
		rs.close();
		pStmt.close();
		con.close();					
		
		return prod;	
	}
	
	public Map<String, Object> getProductList(Search search) throws Exception{
		
		Map<String , Object> map = new HashMap<String , Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM product ";
		
		System.out.println(search);
		
		if(search.getSearchCondition() != null) {
			if( search.getSearchCondition().equals("0") && !search.getSearchKeyword().equals("")){
				sql += " WHERE prod_no = " + search.getSearchKeyword();
			}else if( search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE prod_name LIKE '" + search.getSearchKeyword() + "%'";
			}else if( search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE price = " + search.getSearchKeyword();
			}
		}
		sql += " ORDER BY prod_no";
		
		System.out.println("ProductDAO::Original SQL :: "+sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount :: "+totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);
		
		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()) {
			Product prod = new Product();
			prod.setProdNo(rs.getInt("prod_no"));
			prod.setProdName(rs.getString("prod_name"));
			prod.setPrice(rs.getInt("price"));
			prod.setRegDate(rs.getDate("reg_date"));
			list.add(prod);
		}
		
		map.put("totalCount", new Integer(totalCount));
		
		map.put("list", list);
		
		rs.close();
		pStmt.close();
		con.close();
		
		return map;
	}
	
	public void updateProduct(Product vo) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE product "+
								" SET prod_name = ?, prod_detail = ?, price =?, manufacture_day = ?, image_file = ? " +
								" WHERE prod_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, vo.getProdName());
		pStmt.setString(2, vo.getProdDetail());
		pStmt.setInt(3, vo.getPrice());
		pStmt.setString(4, vo.getManuDate());
		pStmt.setString(5, vo.getFileName());
		pStmt.setInt(6, vo.getProdNo());
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}

	private int getTotalCount(String sql) throws Exception{
		
		sql = "SELECT COUNT(*) "+
					"FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if(rs.next()) {
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	private String makeCurrentPageSql(String sql, Search search) {
		
		sql = 	"SELECT * "+ 
				" FROM (		 SELECT  inner_table. * ,  ROWNUM  AS  row_seq " +
								"  FROM  (	"+sql+" )  inner_table "+
								" WHERE  ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
	
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
	
		return sql;	
	}
	
}
