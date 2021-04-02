/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.AldiPriceEngine;

/**
 * @author smcardle
 *
 */
public class AldiPriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testAldiPriceEngine() {
		
		AldiPriceEngine engine = new AldiPriceEngine();
		engine.start(new ArrayList());
		
	}

}
