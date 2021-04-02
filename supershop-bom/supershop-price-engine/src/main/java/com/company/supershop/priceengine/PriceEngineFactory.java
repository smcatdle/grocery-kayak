/** Scanshop Price Engine   **/


package com.company.supershop.priceengine;


public class PriceEngineFactory
    implements IPriceEngineFactory {

	
    public static final String TESCO = "tesco";
    public static final String LIDL = "lidl";
    public static final String ALDI = "aldi";
    public static final String SUPERVALU = "supervalu";
    public static final String DEALZ = "dealz";
    public static final String EUROSPAR = "eurospar";
    public static final String MACE = "mace";
    public static final String SUPERQUINN = "superquinn";
    private static PriceEngineFactory instance = null;
    
    
    private PriceEngineFactory() {
    	
    }

    public static PriceEngineFactory getInstance() {
        if(instance == null) {
            instance = new PriceEngineFactory();
        }
        
        return instance;
    }

    public IPriceEngine create(String type) {
    	
        IPriceEngine priceEngine = null;
        
        if(TESCO.equals(type)) {
            priceEngine = new TescoPriceEngine();
        } else if(SUPERVALU.equals(type)) {
            priceEngine = new SuperValuPriceEngine();
        } else if(SUPERQUINN.equals(type)) {
            priceEngine = new SuperquinnPriceEngine();
        } else if(LIDL.equals(type)) {
            priceEngine = new LidlPriceEngine();
        } else if(ALDI.equals(type)) {
            priceEngine = new AldiPriceEngine();
        } else if(DEALZ.equals(type)) {
            priceEngine = new DealzPriceEngine();
        } else if(EUROSPAR.equals(type)) {
            priceEngine = new EuroSparPriceEngine();
        } else if(MACE.equals(type)) {
            priceEngine = new MacePriceEngine();
        }
        
        return priceEngine;
    }


}
