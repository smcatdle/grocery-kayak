package com.company.supershop.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.company.supershop.exception.AuthenticationException;
import com.company.supershop.model.Account;
import com.company.supershop.services.AccountService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
 
	@Autowired
    private AccountService accountService;
	
	
    @Override
    public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
 
        Account account = accountService.findByAccountNameAndPassword(name, password);
        
        // use the credentials to try to authenticate against the third party system
        if (account != null) {
        	
        	//TODO: Store the user principal in the session to prevent CRSF attack obtaining username/password
        	
        	// Add granted authorities here
        	Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
        	auths.add(new SimpleGrantedAuthority("USER"));
        	List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>(auths);
        	
            return new UsernamePasswordAuthenticationToken(account.getId(), account.getPassword(), grantedAuths);
        } else {
            throw new AuthenticationException("Unable to auth against third party systems");
        }
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}