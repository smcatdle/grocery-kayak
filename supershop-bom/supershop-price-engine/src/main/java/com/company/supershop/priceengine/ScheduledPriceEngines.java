
package com.company.supershop.priceengine;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

@Resource
public class ScheduledPriceEngines {

	@Autowired
    private List<IPriceEngine> priceEngines;

    
    public void setPriceEngines(List<IPriceEngine> priceEngines) {
        this.priceEngines = priceEngines;
    }

    public List<IPriceEngine> getPriceEngines() {
        return this.priceEngines;
    }

}
