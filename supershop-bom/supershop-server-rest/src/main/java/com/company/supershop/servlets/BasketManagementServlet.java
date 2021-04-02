/**
 * 
 */
package com.company.supershop.servlets;

/**
 * @author smcardle
 *
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.supershop.model.Product;

/**
 * Servlet implementation class LivePriceCheckerServlet
 */
public class BasketManagementServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private static final String SERVICE = "service";
	
	private static final String SERVICE_ADD_BASKET = "addBasket";
	
	private static final String SERVICE_PARAM_NAME = "name";
	
	private static final String UTF_8 = "UTF-8";
	
	private final static Logger logger = Logger.getLogger(BasketManagementServlet.class .getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BasketManagementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String result = null;
		String service = null;
		PrintWriter writer = null;
		String name = null;
		String success = null;
		
		service = request.getParameter(BasketManagementServlet.SERVICE);
		
		writer = response.getWriter();
		
		// A simple service to add two numbers.
		if (BasketManagementServlet.SERVICE_ADD_BASKET.equals(service)) {
			name = request.getParameter(BasketManagementServlet.SERVICE_ADD_BASKET);
			
			if (name != null) {	
				success = "SUCCESS";
				result = success;
			} else {
				result = "Invalid arguments to the service : " + BasketManagementServlet.SERVICE_ADD_BASKET;
			}
		} else {
			result = "service : " + service + " is not recognised";
		}
		
		logger.info("response : [" + result + "] from service : " + service);
		
		writer.print(result);
		writer.close();
		response.setContentType(BasketManagementServlet.UTF_8);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

