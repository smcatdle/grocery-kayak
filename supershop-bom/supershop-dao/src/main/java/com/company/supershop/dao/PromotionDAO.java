package com.company.supershop.dao;

import java.util.List;

import com.company.supershop.model.ProductMVO;
import com.company.supershop.model.Promotion;
import com.company.supershop.model.PromotionString;


public interface PromotionDAO {

    public List<Promotion> findAll();
    
    public ProductMVO getPromotionProduct(List<PromotionString> promotionStrings);

}
