package com.company.supershop.dao;

import java.util.List;

import com.company.supershop.model.ProductMVO;


public interface ProductDAO {

	public void indexProducts();
	
    public void add(ProductMVO productMVO);
	
	public void update(ProductMVO productMVO);
	
	public List<ProductMVO> findByName(String name);
	
	public List<ProductMVO> findAllByChain(String chain);

	public List<ProductMVO> findCurrentByChain(String chain);
	
	public List<ProductMVO> findByNameAndChain(String name, String chain);
	
	public List<ProductMVO> findById(int id);
	
	public List<String> getDepartments(String chain);

	public List<ProductMVO> getAvailableProducts(List<String> ids);
}
