package com.company.supershop.dao;

import java.util.List;

import com.company.supershop.model.Account;


public interface AccountDAO {

    public void attachDirty(Account account);
	
	public void update(Account account);
	
	public List<Account> findAccountByName(String name);
}
