// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LivePriceLookup.java

package com.company.supershop.priceengine;


public class LivePriceLookup
{

/*    private LivePriceLookup()
    {
        products = null;
        promotions = null;
        orderedPromotions = null;
        chains = null;
        gson = null;
        batchComplete = false;
        missingBarcodes = null;
        logger.log(Level.INFO, "PriceEngine Constructor ");
        gson = new Gson();
        products = new Hashtable();
        promotions = new TreeMap();
        orderedPromotions = new ArrayList();
        chains = new ArrayList();
        missingBarcodes = new ArrayList();
    }

    public static LivePriceLookup getInstance()
    {
        if(instance == null)
            instance = new LivePriceLookup();
        return instance;
    }

    public synchronized String getMatchingProducts(String barcode)
    {
        List foundProducts = null;
        String result = null;
        foundProducts = new ArrayList();
        if(barcode != null)
        {
            logger.log(Level.INFO, (new StringBuilder()).append("getMatchingProducts : barcode [").append(barcode).append("]").toString());
            Iterator i$ = chains.iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                String chain = (String)i$.next();
                String key = (new StringBuilder()).append(barcode).append(":").append(chain).toString();
                logger.log(Level.INFO, (new StringBuilder()).append("finding product : key [").append(key).append("] chain [").append(chain).append("]").toString());
                IProduct foundProduct = (IProduct)products.get(key);
                if(foundProduct != null)
                {
                    logger.log(Level.INFO, (new StringBuilder()).append("found product : name [").append(foundProduct.getName()).append("]").toString());
                    foundProducts.add(foundProduct);
                }
            } while(true);
        }
        result = gson.toJson(foundProducts);
        logger.log(Level.INFO, (new StringBuilder()).append("found products : result [").append(result).append("]").toString());
        return result;
    }

    public boolean updateProduct(IProduct product)
    {
        boolean ret = false;
        if(product != null && products != null)
        {
            if(product.isPromotion())
            {
                String offerOrder = new String(product.getPromotionDiscount());
                promotions.put((new StringBuilder()).append(offerOrder).append(":").append(product.getName()).append(":").append(product.getChain()).toString(), (new StringBuilder()).append(product.getBarcode()).append(":").append(product.getChain()).toString());
            }
            products.put((new StringBuilder()).append(product.getBarcode()).append(":").append(product.getChain()).toString(), product);
            checkForMissingBarcode(product);
            logger.log(Level.INFO, (new StringBuilder()).append("updateProduct : product [").append(product.getName()).append("] key : [").append(product.getBarcode()).append(":").append(product.getChain()).append("]").toString());
            ret = true;
        }
        return ret;
    }

    public synchronized String getPromotions(String shelfNameString, int ranking)
    {
        String promotionsResult = null;
        List promotionList = null;
        IProduct product = null;
        OrderedPromotion orderedPromotion = null;
        int foundPromotions = 0;
        promotionList = new ArrayList();
        String shelfNames[] = shelfNameString == null || shelfNameString.equals("") ? null : shelfNameString.split("\\|");
        logger.log(Level.INFO, (new StringBuilder()).append("getPromotions... shelfNames [").append(shelfNames[0]).append("] ranking [").append(ranking).append("]").toString());
        try
        {
            int index = ranking;
            do
            {
                if(foundPromotions >= 20)
                    break;
                orderedPromotion = (OrderedPromotion)orderedPromotions.get(index++);
                if(shelfNames != null)
                    logger.log(Level.INFO, (new StringBuilder()).append("searching orderedPromotions is : ShelfName[").append(orderedPromotion.getShelfName()).append("] : Key [").append(orderedPromotion.getPromotionsKey()).append("]").toString());
                if(isPreferredShelfName(shelfNames, orderedPromotion) || shelfNames == null || shelfNames.length == 0)
                {
                    logger.log(Level.INFO, (new StringBuilder()).append("preferredShelfName is : ShelfName[").append(orderedPromotion.getShelfName()).append("] : Key [").append(orderedPromotion.getPromotionsKey()).append("]").toString());
                    String productsKey = (String)promotions.get(orderedPromotion.getPromotionsKey());
                    logger.log(Level.INFO, (new StringBuilder()).append("productsKey is : [").append(productsKey).append("]").toString());
                    product = (IProduct)products.get(productsKey);
                    logger.log(Level.INFO, (new StringBuilder()).append("Adding Product : [").append(product.getName()).append("]").toString());
                    promotionList.add(product);
                    foundPromotions++;
                }
            } while(true);
        }
        catch(Exception ex)
        {
            promotionsResult = (new StringBuilder()).append("Error : ").append(ex).toString();
        }
        promotionsResult = gson.toJson(promotionList);
        promotionList = null;
        return promotionsResult;
    }

    public boolean isBatchComplete()
    {
        return batchComplete;
    }

    public void setBatchComplete(boolean batchComplete)
    {
        this.batchComplete = batchComplete;
        if(batchComplete)
            createOrderedPromotions();
    }

    private void createOrderedPromotions()
    {
        logger.log(Level.INFO, "createOrderedPromotions");
        OrderedPromotion orderedPromotion = null;
        IProduct product = null;
        String productsKey = null;
        String key = null;
        int promotionRanking = 0;
        for(Iterator i$ = promotions.entrySet().iterator(); i$.hasNext(); orderedPromotions.add(orderedPromotion))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
            productsKey = (String)entry.getValue();
            key = (String)entry.getKey();
            product = (IProduct)products.get(productsKey);
            product.setPromotionRanking(promotionRanking++);
            orderedPromotion = new OrderedPromotion();
            orderedPromotion.setPromotionsKey(key);
            orderedPromotion.setShelfName(product.getShelfName());
            logger.log(Level.INFO, (new StringBuilder()).append("Adding to orderedPromotions : key [").append(key).append("] getShelfName [").append(product.getShelfName()).append("]").toString());
        }

        logger.log(Level.WARNING, "createOrderedPromotions() complete.");
    }

    private boolean isPreferredShelfName(String shelfNames[], OrderedPromotion orderedPromotion)
    {
        boolean found = false;
        String arr$[] = shelfNames;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String preferredShelfName = arr$[i$];
            if(orderedPromotion != null && orderedPromotion.getShelfName().equals(preferredShelfName))
                return true;
        }

        return found;
    }

    public void addChain(String chain)
    {
        chains.add(chain);
    }

    public void checkForMissingBarcode(IProduct product)
    {
        if(product.getBarcode() == null || product.getBarcode().equals("") || product.getBarcode().equals("null"))
            missingBarcodes.add((new StringBuilder()).append(product.getBarcode()).append(":").append(product.getChain()).toString());
    }

    public void findMissingBarcodes()
    {
        IProduct product = null;
        boolean foundMatchingBarcode = false;
        for(Iterator i$ = missingBarcodes.iterator(); i$.hasNext();)
        {
            String productKey = (String)i$.next();
            product = (IProduct)products.get(productKey);
            List matchingProducts = findMatchingProductsByName(product);
            Iterator i = matchingProducts.iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                IProduct matchingProduct = (IProduct)i$.next();
                if(matchingProduct.getBarcode() != null && !matchingProduct.getBarcode().equals("null"))
                {
                    product.setBarcode(matchingProduct.getBarcode());
                    foundMatchingBarcode = true;
                }
            } while(true);
            if(foundMatchingBarcode);
        }

    }

    public List findMatchingProductsByName(IProduct product)
    {
        List matchingProducts = null;
        String registeredName = null;
        matchingProducts = new ArrayList();
        Iterator i$ = products.entrySet().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            java.util.Map.Entry registeredProduct = (java.util.Map.Entry)i$.next();
            registeredName = ((IProduct)registeredProduct.getValue()).getName();
            if(!registeredName.trim().equals(product.getName().trim()))
                continue;
            matchingProducts.add(registeredProduct.getValue());
            break;
        } while(true);
        return matchingProducts;
    }

    private static final Logger logger = Logger.getLogger(LivePriceLookup.class.getName());
    private static final int PROMOTION_RANKING_ITEMS_PER_PAGE = 20;
    private Hashtable products;
    private TreeMap promotions;
    private List orderedPromotions;
    private List chains;
    private Gson gson;
    private boolean batchComplete;
    private ArrayList missingBarcodes;
    private static LivePriceLookup instance = null;*/

}
