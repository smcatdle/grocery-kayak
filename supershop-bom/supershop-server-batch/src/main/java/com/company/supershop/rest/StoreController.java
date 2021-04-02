package com.company.supershop.rest;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.company.supershop.exception.NotFoundException;
import com.company.supershop.exception.ScheduledPriceEngineNotFoundException;
import com.company.supershop.priceengine.IPriceEngine;
import com.company.supershop.priceengine.ScheduledPriceEngines;

// curl -X GET "http://localhost:49823/rest/products/start?engineCategory=standard"


@RestController
@RequestMapping("/rest/products")
public class StoreController {
	
    private final Logger logger = Logger.getLogger(StoreController.class.getName());


    @Autowired
    private ScheduledPriceEngines standardScheduledPriceEngines;
    
    @Autowired
    private ScheduledPriceEngines supervaluScheduledPriceEngines;

    @Autowired
    private ScheduledPriceEngines tescoScheduledPriceEngines;
    
    @Autowired
    private ScheduledPriceEngines eurosparScheduledPriceEngines;

    
    public void setStandardScheduledPriceEngines(ScheduledPriceEngines standardScheduledPriceEngines) {
        this.standardScheduledPriceEngines = standardScheduledPriceEngines;
    }
    
    public void setSupervaluScheduledPriceEngines(ScheduledPriceEngines supervaluScheduledPriceEngines) {
        this.supervaluScheduledPriceEngines = supervaluScheduledPriceEngines;
    }
    
    //@Secured("ROLE_ADMIN")
    @RequestMapping(value="/start", method = RequestMethod.GET)
    public void start(@RequestParam(value="engineCategory", defaultValue="") String engineCategory)
    {
    	ScheduledPriceEngines scheduledPriceEngines = null;
    	
        try {

        	// Fetch the engineCategory
        	if (engineCategory.equals("standard")) {
        		scheduledPriceEngines = standardScheduledPriceEngines;
        	} else if (engineCategory.equals("supervalu")) {
        		scheduledPriceEngines = supervaluScheduledPriceEngines;
        	} else if (engineCategory.equals("tesco")) {
                scheduledPriceEngines = tescoScheduledPriceEngines;
            } else if (engineCategory.equals("eurospar")) {
                scheduledPriceEngines = eurosparScheduledPriceEngines;
            } else {
        		throw new ScheduledPriceEngineNotFoundException();
        	}
        	
            for (IPriceEngine priceEngine : scheduledPriceEngines.getPriceEngines()) {
                logger.log(Level.INFO, "Starting engine [" + priceEngine.getClass().getName() + "]");

                priceEngine.start(new ArrayList());
            }

        } catch(Exception exception) {
            throw new NotFoundException(exception);
        }
    }


}
