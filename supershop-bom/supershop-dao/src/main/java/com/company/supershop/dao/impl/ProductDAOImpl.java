package com.company.supershop.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/*import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;*/
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.company.supershop.dao.ProductDAO;
import com.company.supershop.model.ProductMVO;

public class ProductDAOImpl implements ProductDAO {

	private static final Log logger = LogFactory.getLog(ProductDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	
	private ProductDAOImpl() {
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void add(ProductMVO instance) {

		logger.debug("attaching dirty DaftProductMVO instance");
		Transaction t = null;
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
		} finally {

			session.clear();
			session.close();
			session = null;
			t = null;
		}
	}

	public void update(ProductMVO instance) {
		logger.debug("updating clean ProductMVO instance");
		Transaction t = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			t = session.beginTransaction();
			session.saveOrUpdate(instance);
			t.commit();

			logger.debug("update successful");
		} catch (RuntimeException re) {
			logger.error("update failed", re);

			if (t.isParticipating()) {
				t.rollback();
			}

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			t = null;
		}
	}

	public List<ProductMVO> findByName(String name) {
		logger.debug("finding Product instance by name");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getProductsByName")
					.setString("name", "%" + name + "%");
			List<ProductMVO> products = query.list();

			logger.debug("find by name successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("find by name failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<ProductMVO> findCurrentByChain(String chain) {
		logger.debug("finding current temp products instance by chain");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getCurrentProductsByChain")
					.setString("chain", chain);
			query.setMaxResults(200);
			List<ProductMVO> products = query.list();

			logger.debug("find by chain successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("find by chain failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	

	public List<ProductMVO> findAllByChain(String chain) {
		logger.debug("finding all products instance by chain");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getAllProductsByChain")
					.setString("chain", chain);
			query.setMaxResults(200);
			List<ProductMVO> products = query.list();

			logger.debug("find by chain successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("find by chain failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}


	public List<ProductMVO> findOffersByChain(String chain) {
		logger.debug("finding products for chain [" + chain + "] ordered by most recently updated.");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getProductOffersByChain")
					.setString("chain", chain);
			query.setMaxResults(200);
			List<ProductMVO> products = query.list();

			logger.debug("find by chain/offers successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("find by chain failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<ProductMVO> findByNameAndChain(String name, String chain) {
		logger.debug("finding Product instance by name and chain");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session
					.getNamedQuery("ProductMVO.getProductsByNameAndChain")
					.setString("name", name).setString("chain", chain);
			List<ProductMVO> products = query.list();

			logger.debug("find by name successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("find by name failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<ProductMVO> findById(int id) {
		logger.debug("finding Product instance by id");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getProductById")
					.setInteger("id", id);
			List<ProductMVO> products = query.list();

			logger.debug("find by id successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("find by id failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<String> getDepartments(String chain) {
		logger.debug("finding departments instance by chain");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getDepartments")
					.setString("chain", chain);
			List<String> departments = query.list();

			logger.debug("find by chain successful, result size: "
					+ departments.size());
			return departments;
		} catch (RuntimeException re) {
			logger.error("find by chain failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<String> getDepartmentShelves(String department, String chain) {
		logger.debug("finding shelves instance by chain and department");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getDepartmentShelves")
					.setString("department", department)
					.setString("chain", chain);
			List<String> shelves = query.list();

			logger.debug("find by shelf successful, result size: "
					+ shelves.size());
			return shelves;
		} catch (RuntimeException re) {
			logger.error("finding shelves failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	
	public List<ProductMVO> getShelfItems(String chain, String shelf) {
		logger.debug("finding products instance by chain and shelf");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getShelfItems")
					.setString("chain", chain)
					.setString("shelf", shelf);
			List<ProductMVO> products = query.list();

			logger.debug("find by shelf successful, result size: "
					+ products.size());
			return products;
		} catch (RuntimeException re) {
			logger.error("finding products failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}
	
	public List<ProductMVO> getAvailableProducts(List<String> names) {

		logger.debug("get Available Products");
		Query query = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			query = session.getNamedQuery("ProductMVO.getAvailableProducts")
					.setParameterList("names", names);
			List<ProductMVO> availableIds = query.list();

			logger.debug("get Available Products, result size: "
					+ availableIds.size());
			return availableIds;
		} catch (RuntimeException re) {
			logger.error("get Available Products failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
	}

	public List<ProductMVO> getProductsBySearchStrings(List<String> searchStrings) {
		logger.debug("finding Product instance by searchStrings");
		Query query = null;
		Session session = null;
		List<ProductMVO> products = null;

		try {
			session = sessionFactory.openSession();
			logger.info("searchStrings : [" + searchStrings.size() + "]");

			Criteria criteria = session.createCriteria(ProductMVO.class);
			
			for (String searchString : searchStrings) {
				criteria.add( Restrictions.like("name", "%" + searchString + "%"));
			}
			
			
			products = criteria.addOrder( org.hibernate.criterion.Order.asc("price") )
					.setMaxResults(200)
				    .list();
 
			
		} catch (RuntimeException re) {
			logger.error("find by searchStrings failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
		return (products.size() > 0) ? products : null;	
	}

	public void indexProducts() {
		
		logger.debug("Indexing Products...");
		/*Session session = null;

		try {
			session = sessionFactory.openSession();
			
			FullTextSession fullTextSession = Search.getFullTextSession(session);
			fullTextSession.createIndexer().startAndWait();
			
		} catch (RuntimeException re) {
			logger.error("Error indexing products : ", re);

			throw re;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			session.clear();
			session.close();
			session = null;
			//query = null;
		}*/
	}
		
	public List<ProductMVO> getAlternativeProductsFast(String productName) {
		logger.debug("finding alternatives by product string");
		Query query = null;
		Session session = null;
		List<ProductMVO> products = null;

		try {
			session = sessionFactory.openSession();
			logger.info("productName : [" + productName + "]");

			products = session.getNamedQuery("getAlternativesFast")
				    .setString("productName", productName)
				    .list();
			
		} catch (RuntimeException re) {
			logger.error("find by alternatives failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
		return (products.size() > 0) ? products : null;	
	}
	
	public List<ProductMVO> getAlternativeProducts(List<String> nameStrings) {
		logger.debug("finding Product instance by nameStrings");
		//Query query = null;
		Session session = null;
		List<ProductMVO> products = null;
		List<Criterion> criterionList = new ArrayList<Criterion>();

		try {
			session = sessionFactory.openSession();
			
			//FullTextSession fullTextSession = Search.getFullTextSession(session);
			
			if (nameStrings != null) {
	
				Criteria criteria = session.createCriteria(ProductMVO.class);
				Criterion criterion = null;
				
				// Add the name restriction criteria
				for (String nameString : nameStrings) {
					if (criterion != null) {
						criterion = Restrictions.or(criterion, Restrictions.like("name", "%" + nameString + "%"));
					} else {
						criterion = Restrictions.like("name", "%" + nameString + "%");
					}
				}
				
				// Add the shelf_name restriction criteria
				for (String nameString : nameStrings) {
					if (criterion != null) {
						criterion = Restrictions.or(criterion, Restrictions.like("shelfName", "%" + nameString + "%"));
					} else {
						criterion = Restrictions.like("shelfName", "%" + nameString + "%");
					}
				}
				
				criteria.add(criterion);
				
				products = criteria.addOrder( Order.asc("price") )
						.setMaxResults(50)
					    .list();
				
				return products;
				
				/*QueryBuilder qb = fullTextSession.getSearchFactory()
		           .buildQueryBuilder().forEntity(ProductMVO.class).get();
		         org.apache.lucene.search.Query query = qb
		           .keyword().onFields("name", "shelfName")
		           .matching(productString)
		           .createQuery();
		         
		         
		         org.hibernate.Query hibQuery = 
		            fullTextSession.createFullTextQuery(query, ProductMVO.class);

		         List<ProductMVO> results = hibQuery.setMaxResults(20).list();
		         return results;*/
			}
 
			
		} catch (RuntimeException re) {
			logger.error("find by nameStrings failed", re);

			throw re;

		} finally {

			session.clear();
			session.close();
			session = null;
			//query = null;
		}
		
		return (products.size() > 0) ? products : null;	
	}

}