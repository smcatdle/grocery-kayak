/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.LidlPriceEngine;

/**
 * @author smcardle
 *
 */
public class LidlPriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testLidlPriceEngine() {
		
		LidlPriceEngine engine = new LidlPriceEngine();
		engine.start(new ArrayList());
		
	}

}
