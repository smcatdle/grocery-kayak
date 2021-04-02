/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.TescoPriceEngine;

/**
 * @author smcardle
 *
 */
public class TescoPriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testMacePriceEngine() {
		
		TescoPriceEngine engine = new TescoPriceEngine();
		engine.start(new ArrayList());
		
	}

}
