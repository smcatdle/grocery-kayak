package com.company.supershop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.security.Encrypt;
import com.company.supershop.dao.AccountDAO;
import com.company.supershop.exception.AccountExistsException;
import com.company.supershop.model.Account;
import com.company.supershop.services.AccountService;
import com.company.utils.SecurityUtils;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());
    private Encrypt encrypt;
    
    @Autowired
    private AccountDAO accountDAO;

    public AccountServiceImpl() {
		/* Create a cipher using the first 16 bytes of the passphrase */
    	encrypt = new Encrypt("RWER32SF359SFDSSDF1342SFD".getBytes());
    }
    
    @Override
    public Account createAccount(Account account) {
    		
    	logger.log(Level.INFO, "Creating account");
    	
    	// Does an account with this email address already exist
        List<Account> existingAccounts = accountDAO.findAccountByName(account.getEmail());
        
        if (existingAccounts != null && existingAccounts.size() > 0)
        {
            throw new AccountExistsException();
        }
    	
        logger.log(Level.INFO, "No existing account with this email found...");
        
        // Encrypt email using TEA algorithm
        account.setEmail(encrypt.encryptToHexString(account.getEmail().getBytes()));
        account.setRole("U");
        account.setPassword(SecurityUtils.hash(account.getPassword()));
    	account.setDateCreated(new Date());
    	account.setDateUpdated(new Date()); 
    	accountDAO.attachDirty(account);
    	
    	// Reset password and email field before returning to user
    	account.setPassword("");
    	account.setEmail("");
    	account.setResetCode("");
    	account.setId(0);
        return account;
    }

    
    /*@Override
    public Blog createBlog(Long accountId, Blog data) {
        Blog blogSameTitle = blogRepo.findBlogByTitle(data.getTitle());

        if(blogSameTitle != null)
        {
            throw new BlogExistsException();
        }

        Account account = accountRepo.findAccount(accountId);
        if(account == null)
        {
            throw new AccountDoesNotExistException();
        }

        Blog createdBlog = blogRepo.createBlog(data);

        createdBlog.setOwner(account);

        return createdBlog;
    }

    @Override
    public BlogList findBlogsByAccount(Long accountId) {
        Account account = accountRepo.findAccount(accountId);
        if(account == null)
        {
            throw new AccountDoesNotExistException();
        }
        return new BlogList(blogRepo.findBlogsByAccount(accountId));
    }*/

    @Override
    public Account findByAccountNameAndPassword(String name, String password) {
    	
    	Account account = null;
    	
    	try {
            // Encrypt email using Tiny algorithm
            String encryptedEmail = encrypt.encryptToHexString(name.getBytes());
            
    		account = accountDAO.findAccountByName(encryptedEmail).get(0);
    		
    		String hashedPassword = SecurityUtils.hash(password);
    		
    		// Confirm the password hash is correct
    		if (hashedPassword == null || !hashedPassword.equals(account.getPassword())) {
    			return null;
    		}
    		
    	} catch (Exception ex) {

            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
            return null;
    	}
    	
    	return account;
    }
}
