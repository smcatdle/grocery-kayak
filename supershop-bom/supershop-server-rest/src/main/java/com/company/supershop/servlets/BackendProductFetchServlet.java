/**
 *
 */
package com.company.supershop.servlets;

/**
 * @author smcardle
 *
 */


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.supershop.priceengine.IPriceEngine;
import com.company.supershop.priceengine.TescoPriceEngine;

/*import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.google.appengine.api.LifecycleManager;
import com.google.appengine.api.LifecycleManager.ShutdownHook;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;*/

/**
 * Servlet implementation class BackendProductFetchServlet
 *
 * http://localhost:8080/mymobilebasket/priceCheck?service=checkPrice&barcode=5036324111202
 *
 * http://localhost:8080/mymobilebasket/priceCheck?service=getPromotions
 *
 */
public class BackendProductFetchServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private static final String SERVICE = "service";
    
    private static final String SERVICE_CHECK_PRICE = "checkPrice";
    
    private static final String SERVICE_GET_PROMOTIONS = "getPromotions";
    
    private static final String SERVICE_PARAM_BARCODE = "barcode";
    
    private static final String SERVICE_PARAM_SHELFNAMES = "shelfNames";
    
    private static final String SERVICE_PARAM_PROMOTION_RANKING = "promotionRanking";
    
    private static final String UTF_8 = "UTF-8";
    
    private final static Logger logger = Logger.getLogger(BackendProductFetchServlet.class .getName());
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BackendProductFetchServlet() {
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
        
        try {
            // set trace logging
            System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
            
            
            /*if(GAEUtils.isGaeMode()) {
                
                logger.log(Level.SEVERE, "=================================================================");
                logger.log(Level.SEVERE, "============= GAE ===============================================");
                logger.log(Level.SEVERE, "=================================================================");
                
                // -------------------------------
                // GOOGLE APP ENGINE SPECIFIC CODE
                // -------------------------------
                // Create Task and push it into Task Queue
                Queue queue = QueueFactory.getQueue("PriceQueue");
                TaskOptions taskOptions = TaskOptions.Builder.withUrl("/startProductFetch")
                .param("param", "param")
                .header("Host", BackendServiceFactory.getBackendService().getBackendAddress("price-backend"))
                .method(Method.POST);
                queue.add(taskOptions);
                
                // Add the shutdown hook to handle backend shutdown gracefully.
                LifecycleManager.getInstance().setShutdownHook(new ShutdownHook() {
                    public void shutdown() {
                        logger.info("LifecycleManager : Shutdown Hook Called");
                        
                        // TODO: Remove the line below when backend no longer shutsdown suddenly
                        livePriceLookup.setBatchComplete(true);
                        
                        //LifecycleManager.getInstance().interruptAllRequests();
                    }
                });
                
                // -----------------------------------
                // END GOOGLE APP ENGINE SPECIFIC CODE
                // -----------------------------------
                
            } else {*/
                logger.log(Level.SEVERE, "=================================================================");
                logger.log(Level.SEVERE, "============= Tomcat! ===============================================");
                logger.log(Level.SEVERE, "=================================================================");
                
            //}
            
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }
    
}

