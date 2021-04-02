package com.company.supershop.dao.impl;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.company.supershop.dao.AccountDAO;
import com.company.supershop.model.Account;




public class AccountDAOImpl implements AccountDAO {

	private final Logger logger = Logger.getLogger(AccountDAOImpl.class.getName());

   
    @Autowired
    private SessionFactory sessionFactory;

	private AccountDAOImpl() {
    }

    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
   
    public void attachDirty(Account instance) {
	
	logger.log(Level.INFO,"attaching dirty Account instance");
	Transaction t =  null;
	Session session = null;
		
	try {
	    session = sessionFactory.openSession();
			 
	    t = session.beginTransaction();
	    session.save(instance);	
	    t.commit();
			
	    logger.log(Level.INFO,"attach successful");
			
	} catch (RuntimeException re) {
	    logger.log(Level.SEVERE,"attach failed", re);
			
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
	
	public void update(Account instance) {
		logger.log(Level.INFO,"updating clean Account instance");
		Transaction t =  null;
		Session session = null;
		
		try {
		    session = sessionFactory.openSession();
		    
			t = session.beginTransaction();
			session.saveOrUpdate(instance);
			t.commit();
			
			logger.log(Level.INFO,"update successful");
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"update failed", re);

			if (t.isParticipating()) {
				t.rollback();
			}
			
			throw re;
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			t = null;
		}
	}
	
	public List<Account> findAccountByName(String email) {
		logger.log(Level.INFO,"finding Account instance by email");
		Query query = null;
		Session session = null;
		
		try {	
			session = sessionFactory.openSession();

			query = session.getNamedQuery("Account.findAccountByName")
			.setString("email", email);
			List<Account> accounts = query.list();
			
			logger.log(Level.INFO,"find by email successful, result size: "
					+ accounts.size());
			return accounts;
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find by email failed", re);
			
		}  finally {
			
			session.clear();
			session.close();
			session = null;
			query = null;
		}
		
		return null;
	}
}