package com.company.supershop.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;


import com.company.supershop.dao.PromotionDAO;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.model.Promotion;
import com.company.supershop.model.PromotionString;


public class PromotionDAOImpl implements PromotionDAO {

	private static final Log logger = LogFactory.getLog(PromotionDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	private PromotionDAOImpl() {
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



	public List<Promotion> findAll() {
		logger.debug("finding Promotions");
		Query query = null;
		Session session = null;
		List<Promotion> promotions = null;
		
		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("Promotion.findAll");
			promotions = query.list();

			logger.debug("find by name successful, result size: "
					+ promotions.size());

		} catch (RuntimeException re) {
			logger.error("find all failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
		return promotions;
	}

	public ProductMVO getPromotionProduct(List<PromotionString> promotionStrings) {
		logger.debug("finding Product instance by string");
		Query query = null;
		Session session = null;
		List<ProductMVO> products = null;

		try {
			session = sessionFactory.openSession();
			logger.info("string : [" + promotionStrings.size() + "]");

			Criteria criteria = session.createCriteria(ProductMVO.class);
			
			for (PromotionString promotionString : promotionStrings) {
				criteria.add( Restrictions.like("name", "%" + promotionString.getString() + "%"));
			}
			
			
			products = criteria.addOrder( org.hibernate.criterion.Order.asc("price") )
				    .setMaxResults(1)
				    .list();
 
			
		} catch (RuntimeException re) {
			logger.error("find by string failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
		return (products.size() > 0) ? products.get(0) : null;	
	}
}