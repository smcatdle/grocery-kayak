package com.company.supershop.rest;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.supershop.exception.ChainNotFoundException;
import com.company.supershop.exception.NotFoundException;
import com.company.supershop.exception.ShelfNotFoundException;
import com.company.supershop.model.Basket;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.services.BasketService;
import com.company.supershop.services.ProductService;
import com.company.supershop.services.exceptions.ProductNotFoundException;

// curl -X GET "http://localhost:49823/rest/products/search?searchString=wines"
// curl -X GET "http://localhost:49823/rest/products/departments?chain=m"
// curl -X GET "http://localhost:49823/rest/products/start"


@RestController
@RequestMapping("/rest/products")
public class StoreController {
	
    private final Logger logger = Logger.getLogger(StoreController.class.getName());

	@Autowired
    private BasketService basketService;
	
	@Autowired
    private ProductService productService;
	

    /*@RequestMapping(value="/{productId}",
        method = RequestMethod.GET)
    public ResponseEntity<ProductResource> getProduct(@PathVariable Long productId)
    {
        Product product = productService.findBlog(productId);
        if(product != null) {
            ProductResource res = new ProductResourceAsm().toResource(product);
            return new ResponseEntity<ProductResource>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<ProductResource>(HttpStatus.NOT_FOUND);
        }
    }*/

    //@Secured("ROLE_USER")
    @Cacheable ("searchResults")
    @RequestMapping(value="/search", method = RequestMethod.GET)
    public ResponseEntity<List<ProductMVO>> searchProductEntries(@RequestParam(value="searchString", defaultValue="") String searchString)
    {
        try {
        	// Dont process empty search strings
        	if (searchString == null || "".equals(searchString)) throw new ProductNotFoundException("");
        	
        	List<ProductMVO> list = productService.searchProductEntries(searchString);
            
            logger.log(Level.INFO, "Returning non cached results [" + list.size() + "] baskets ");
        		
            return new ResponseEntity<List<ProductMVO>>(list, HttpStatus.OK);
        } catch(ProductNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    //@Secured("ROLE_USER")
    @RequestMapping(value="/alternatives", method = RequestMethod.GET)
    public ResponseEntity<List<ProductMVO>> getAlternativeProducts(@RequestParam(value="productString", defaultValue="") String productString)
    {
        try {
        	// Don't process empty product strings
        	if (productString == null || "".equals(productString)) throw new ProductNotFoundException("");
        	
        	List<ProductMVO> list = productService.getAlternativeProducts(productString);
        		
            return new ResponseEntity<List<ProductMVO>>(list, HttpStatus.OK);
        } catch(ProductNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    //@Secured("ROLE_USER")
    @RequestMapping(value="/default", method = RequestMethod.GET)
    public ResponseEntity<List<ProductMVO>> getDefaultProducts(@RequestParam(value="chain", defaultValue="") String chain)
    {
        try {

        	List<ProductMVO> list = productService.getDefaultProducts(chain);
        		
            return new ResponseEntity<List<ProductMVO>>(list, HttpStatus.OK);
        } catch(ProductNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
    
    //@Secured("ROLE_USER")
    @RequestMapping(value="/all", method = RequestMethod.GET)
    public ResponseEntity<List<ProductMVO>> getAllProducts(@RequestParam(value="chain", defaultValue="") String chain)
    {
        try {

            List<ProductMVO> list = productService.getAllProducts(chain);
                
            return new ResponseEntity<List<ProductMVO>>(list, HttpStatus.OK);
        } catch(ProductNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    //@Secured("ROLE_USER")
    @RequestMapping(value="/start", method = RequestMethod.GET)
    public ResponseEntity start()
    {
        try {
        	
        	productService.indexProducts();
        		
            return new ResponseEntity(HttpStatus.OK);
        } catch(ProductNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
    
    //@Secured("ROLE_USER")
    @RequestMapping(value="/departments", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDepartments(@RequestParam(value="chain", defaultValue="") String chain)
    {
        try {
        	// Dont process empty search chain
        	if (chain == null || "".equals(chain)) throw new ChainNotFoundException("");
        	
        	List<String> departments = productService.getDepartments(chain);
        		
            return new ResponseEntity<List<String>>(departments, HttpStatus.OK);
        } catch(ChainNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    //@Secured("ROLE_USER")
    @RequestMapping(value="department/shelves", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDepartmentShelves(@RequestParam(value="department") String department, @RequestParam(value="chain") String chain)
    {
        try {
        	// Dont process empty search chain
        	if (chain == null || "".equals(chain)) throw new ChainNotFoundException("");
        	
        	List<String> shelves = productService.getDepartmentShelves(department, chain);
        		
            return new ResponseEntity<List<String>>(shelves, HttpStatus.OK);
        } catch(ChainNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
    
    //@Secured("ROLE_USER")
    @RequestMapping(value="department/shelves/items", method = RequestMethod.GET)
    public ResponseEntity<List<ProductMVO>> getShelfItems(@RequestParam(value="chain") String chain, @RequestParam(value="shelf") String shelf)
    {
        try {
        	// Dont process empty shelf
        	if (shelf == null || "".equals(shelf)) throw new ShelfNotFoundException("");
        	
        	List<ProductMVO> shelves = productService.getShelfItems(chain, shelf);
        		
            return new ResponseEntity<List<ProductMVO>>(shelves, HttpStatus.OK);
        } catch(ShelfNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
    
    /*@RequestMapping(value="/{departmentId}/product-entries")
    public ResponseEntity<ProductEntryListResource> findAllProductEntries(
            @PathVariable Long departmentId)
    {
        try {
            ProductEntryList list = productService.findAllProductEntries(departmentId);
            ProductEntryListResource res = new ProductEntryListResourceAsm().toResource(list);
            return new ResponseEntity<ProductEntryListResource>(res, HttpStatus.OK);
        } catch(ProductNotFoundException exception)
        {
            throw new NotFoundException(exception);
        }
    }*/

}
