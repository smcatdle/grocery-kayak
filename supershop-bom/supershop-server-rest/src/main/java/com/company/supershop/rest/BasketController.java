package com.company.supershop.rest;

import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.company.supershop.exception.BasketItemsFoundException;
import com.company.supershop.exception.NotFoundException;
import com.company.supershop.model.Basket;
import com.company.supershop.model.BasketItem;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.services.BasketService;
import com.company.supershop.services.exceptions.ProductNotFoundException;



@RestController	
@RequestMapping("/rest/baskets")
public class BasketController {
	
    private final Logger logger = Logger.getLogger(BasketController.class.getName());

    public BasketController() {

    }

    public BasketController(String name) {

    }

	@Autowired
    private BasketService basketService;
	
	
    //@Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Basket>> getBaskets(Principal principal)
    {
        try {
        		
        	List<Basket> baskets = basketService.getBaskets();
        	
        	logger.log(Level.INFO, "Retrieving " + baskets.size() + " baskets ");
        	
            return new ResponseEntity<List<Basket>>(baskets, HttpStatus.OK);
            
        } catch(ProductNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
    
    //@Secured ("USER")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/basketitems", method = RequestMethod.GET)
    public ResponseEntity<List<BasketItem>> getBasketItems(@RequestParam(value="basketId", defaultValue="") int basketId, Principal principal)
    {
        try {
        	
        	List<BasketItem> basketItems = basketService.getBasketItems(basketId, Integer.parseInt(principal.getName()));
        	logger.log(Level.INFO, "Fetched [" + basketItems.size() + "] basket items.");
        	
            return new ResponseEntity<List<BasketItem>>(basketItems, HttpStatus.OK);
            
        } catch(BasketItemsFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
    
    //@Secured("ROLE_USER")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/item", method = RequestMethod.POST)
    public ResponseEntity<List<BasketItem>> addBasketItem(@RequestBody ProductMVO item, Principal principal)
    {
        try {
        	logger.log(Level.INFO, "Adding product with id : [" + item.getId() + "]");
        		
        	List<BasketItem> basketItems = basketService.addBasketItem(item, Integer.parseInt(principal.getName()), true);
        	
        	return new ResponseEntity<List<BasketItem>>(basketItems, HttpStatus.OK);
            
        } catch(Exception exception) {
            throw new NotFoundException(exception);
        }
    }
    
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/item", method = RequestMethod.PATCH)
    public ResponseEntity<List<BasketItem>> updateBasketItem(@RequestBody BasketItem basketItem, @RequestParam(value="basketItemId") int basketItemId, Principal principal)
    {
        try {
        	logger.log(Level.INFO, "Updating basket item with id : [" + basketItemId + "]");
            basketItem.setId(basketItemId);
        		
        	List<BasketItem> basketItems = basketService.updateBasketItem(basketItem, Integer.parseInt(principal.getName()), false);
        	
        	return new ResponseEntity<List<BasketItem>>(basketItems, HttpStatus.OK);
            
        } catch(Exception exception) {
            throw new NotFoundException(exception);
        }
    }
    
    //@Secured("ROLE_USER")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/{basketId}/items/{basketItemId}", method = RequestMethod.DELETE)
    public ResponseEntity<List<BasketItem>> deleteBasketItem(@PathVariable(value="basketId") int basketId, @PathVariable(value="basketItemId") int basketItemId, Principal principal)
    {
        try {
        	logger.log(Level.INFO, "Deleting basket item with id : [" + basketItemId + "]");
        		
        	List<BasketItem> basketItems = basketService.deleteBasketItem(basketId, basketItemId, Integer.parseInt(principal.getName()), true);
        	
        	return new ResponseEntity<List<BasketItem>>(basketItems, HttpStatus.OK);

            
        } catch(Exception exception) {
            throw new NotFoundException(exception);
        }
    }
    
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/{basketId}/items/{basketItemId}", method = RequestMethod.PATCH)
    public ResponseEntity<List<BasketItem>> swapBasketItem(@RequestBody ProductMVO item, @PathVariable(value="basketId") int basketId, @PathVariable(value="basketItemId") int basketItemId, Principal principal)
    {
        try {
        	logger.log(Level.INFO, "Swapping basket item with id : [" + basketItemId + "]");
        		
        	List<BasketItem> basketItems = basketService.swapBasketItem(item, basketId, basketItemId, Integer.parseInt(principal.getName()), true);
        	
        	return new ResponseEntity<List<BasketItem>>(basketItems, HttpStatus.OK);

            
        } catch(Exception exception) {
            throw new NotFoundException(exception);
        }
    }
}
