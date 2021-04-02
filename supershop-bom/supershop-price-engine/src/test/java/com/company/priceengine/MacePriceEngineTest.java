/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.MacePriceEngine;

/**
 * @author smcardle
 *
 */
public class MacePriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testMacePriceEngine() {
		
		MacePriceEngine engine = new MacePriceEngine();
		engine.start(new ArrayList());
		
	}

}
