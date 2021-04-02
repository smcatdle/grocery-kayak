package com.company.supershop.services;


import com.company.supershop.model.Account;


public interface AccountService {

	public Account findByAccountNameAndPassword(String name, String password);
	
	public Account createAccount(Account account);
	
}
