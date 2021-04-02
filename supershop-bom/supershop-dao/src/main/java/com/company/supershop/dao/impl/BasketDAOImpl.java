package com.company.supershop.dao.impl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.company.supershop.dao.BasketDAO;
import com.company.supershop.model.Basket;
import com.company.supershop.model.BasketItem;




public class BasketDAOImpl implements BasketDAO {

    private static final Log logger = LogFactory.getLog(BasketDAOImpl.class);

   
    @Autowired
    private SessionFactory sessionFactory;

	private BasketDAOImpl() {
    }

    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
   
    public void attachDirty(BasketItem instance) {
	
	logger.debug("attaching dirty BasketItem instance");
	Transaction t =  null;
	Session session = null;
		
	try {
	    session = sessionFactory.openSession();
			 
	    t = session.beginTransaction();
	    session.save(instance);	
	    t.commit();
			
	    logger.debug("attach successful");
			
	} catch (RuntimeException re) {
	    logger.error("attach failed", re);
			
	    if (t.isParticipating()) {
		t.rollback();
	    }
	}  finally {
			
	    session.clear();
	    session.close();
	    session = null;
	    t = null;
	}
    }
	
    public void update(BasketItem instance) {
    	
	logger.debug("updating BasketItem instance");
	Transaction t =  null;
	Session session = null;
		
	try {
	    session = sessionFactory.openSession();
			 
	    t = session.beginTransaction();
	    session.update(instance);	
	    t.commit();
			
	    logger.debug("update successful");
			
	} catch (RuntimeException re) {
	    logger.error("update failed", re);
			
	    if (t.isParticipating()) {
		t.rollback();
	    }
	}  finally {
			
	    session.clear();
	    session.close();
	    session = null;
	    t = null;
	}
    }
    
	public List<BasketItem> getBasketItems(int basketId, int accountId) {
		logger.debug("finding BasketItem instance by id and accountId");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("BasketItem.getBasketItems")
			.setLong("basketId", basketId)
			.setInteger("accountId", accountId);
			List<BasketItem> basketItems = query.list();
			
			logger.debug("find by name successful, result size: "
					+ basketItems.size());
			return basketItems;
		} catch (RuntimeException re) {
			logger.error("find by id failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	
	public List<BasketItem> getAllBasketItems(int accountId) {
		logger.debug("finding BasketItem by accountId");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("BasketItem.getAllBasketItems")
			.setInteger("accountId", accountId);
			List<BasketItem> basketItems = query.list();
			
			logger.debug("find by name successful, result size: "
					+ basketItems.size());
			return basketItems;
		} catch (RuntimeException re) {
			logger.error("find by id failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public BasketItem getBasketItem(int basketItemId, int accountId) {
		logger.debug("finding BasketItem instance by basketItemId and accountId");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("BasketItem.getBasketItem")
			.setLong("basketItemId", basketItemId)
			.setInteger("accountId", accountId);
			BasketItem basketItem = (BasketItem)query.list().get(0);

			return basketItem;
		} catch (RuntimeException re) {
			logger.error("find by id failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<Basket> getBaskets() {
		logger.debug("finding Baskets");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("Basket.getBaskets");
			List<Basket> baskets = query.list();
			
			logger.debug("find by name successful, result size: "
					+ baskets.size());
			return baskets;
		} catch (RuntimeException re) {
			logger.error("find by name failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}		
	}
	
	public List<Basket> findByChain(String chain) {
		logger.debug("finding Basket instance by chain");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("Basket.getBasketByChain")
			.setString("chain", chain);
			List<Basket> baskets = query.list();
			
			logger.debug("find by chain successful, result size: "
					+ baskets.size());
			return baskets;
		} catch (RuntimeException re) {
			logger.error("find by chain failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	
	public void deleteBasketItem(int basketItemId, int accountId) {
		logger.debug("deleting Basket item by accountId");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("Basket.deleteBasketItem")
			.setInteger("basketItemId", basketItemId).setInteger("accountId", accountId);
			query.executeUpdate();

			return;
			
		} catch (RuntimeException re) {
			logger.error("delete item by accountId failed", re);
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
}