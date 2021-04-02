package com.company.supershop.services;

import java.util.List;

import com.company.supershop.model.ProductMVO;

public interface ProductService {

	public void indexProducts();
	
	public List<ProductMVO> searchProductEntries(String searchString);
	
	public List<ProductMVO> getAlternativeProducts(String productString);
	
	public List<String> getDepartments(String chain);
	
	public List<String> getDepartmentShelves(String department, String chain);
	
	public List<ProductMVO> getShelfItems(String department, String shelf);
	
	public List<ProductMVO> getDefaultProducts(String chain);

	public List<ProductMVO> getAllProducts(String chain);
}
