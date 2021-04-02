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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class LivePriceCheckerServlet
 * 
 * http://localhost:8080/mymobilebasket/priceCheck?service=checkPrice&barcode=5036324111202
 * 
 * http://localhost:8080/mymobilebasket/priceCheck?service=getPromotions
 * 
 */
public class LivePriceCheckerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private static final String SERVICE = "service";
	
	private static final String SERVICE_CHECK_PRICE = "checkPrice";
	
	private static final String SERVICE_GET_PROMOTIONS = "getPromotions";
	
	private static final String SERVICE_SEARCH = "search";
	
	private static final String SERVICE_PARAM_BARCODE = "barcode";
	
	private static final String SERVICE_PARAM_SHELFNAMES = "shelfNames";
	
	private static final String SERVICE_PARAM_PROMOTION_RANKING = "promotionRanking";
	
	private static final String SERVICE_PARAM_SEACH_STRING = "searchString";
	
	private static final String UTF_8 = "UTF-8";
	
	private final static Logger logger = Logger.getLogger(LivePriceCheckerServlet.class .getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LivePriceCheckerServlet() {
        super();
        
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

      }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String result = null;
		String service = null;
		PrintWriter writer = null;
		String barcode = null;
		String foundProducts = null;
		
		logger.info("LivePriceCheckerServlet : doGet");
		
		service = request.getParameter(LivePriceCheckerServlet.SERVICE);
		
		writer = response.getWriter();
		
		// check the service.
		if (LivePriceCheckerServlet.SERVICE_CHECK_PRICE.equals(service)) {
			barcode = request.getParameter(LivePriceCheckerServlet.SERVICE_PARAM_BARCODE);
			
			if (barcode != null) {	
				//foundProducts = livePriceLookup.getMatchingProducts(barcode);
				
				if (foundProducts != null) {
					result = foundProducts;
				} else {
					result = "No matching products found";
				}
				
			} else {
				result = "Invalid arguments to the service : " + LivePriceCheckerServlet.SERVICE_CHECK_PRICE;
			}
			
		} else if (LivePriceCheckerServlet.SERVICE_GET_PROMOTIONS.equals(service)) {
			
			String shelfNameString = request.getParameter(LivePriceCheckerServlet.SERVICE_PARAM_SHELFNAMES);
			String promotionRankingString = request.getParameter(LivePriceCheckerServlet.SERVICE_PARAM_PROMOTION_RANKING);
			
			logger.info("shelfNameString : [" + shelfNameString + "] from service : " + service);
			
			int promotionRanking = (promotionRankingString != null && !promotionRankingString.equals("")) ? Integer.parseInt(promotionRankingString) : 0;
			//result = livePriceLookup.getPromotions(shelfNameString, promotionRanking);
			
		} else if (LivePriceCheckerServlet.SERVICE_SEARCH.equals(service)) {
			
			String searchString = request.getParameter(LivePriceCheckerServlet.SERVICE_PARAM_SEACH_STRING);
			
			logger.info("searchString : [" + searchString + "] from service : " + service);

			//result = livePriceLookup.findMatchingProductsByName(searchString);
			
		} else {

			result = "service : " + service + " is not recognised";
		}
		
		logger.info("response : [" + result + "] from service : " + service);
		
		writer.print(result);
		writer.close();
		response.setContentType(LivePriceCheckerServlet.UTF_8);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
	}

}

