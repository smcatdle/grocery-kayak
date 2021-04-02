/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.DealzPriceEngine;

/**
 * @author smcardle
 *
 */
public class DealzPriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testDealzPriceEngine() {
		
		DealzPriceEngine engine = new DealzPriceEngine();
		engine.start(new ArrayList());
		
	}

}
