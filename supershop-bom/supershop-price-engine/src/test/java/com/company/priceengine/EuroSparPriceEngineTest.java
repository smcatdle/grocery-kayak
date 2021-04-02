/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.EuroSparPriceEngine;

/**
 * @author smcardle
 *
 */
public class EuroSparPriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testEuroSparPriceEngine() {
		
		EuroSparPriceEngine engine = new EuroSparPriceEngine();
		engine.start(new ArrayList());
		
	}

}
