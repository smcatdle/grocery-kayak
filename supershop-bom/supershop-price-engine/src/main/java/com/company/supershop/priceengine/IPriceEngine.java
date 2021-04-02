/** Scanshop Price Engine   **/


package com.company.supershop.priceengine;

import com.company.supershop.model.IProduct;
import com.company.supershop.model.ProductElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;
import java.util.Set;


public interface IPriceEngine {

    public void persist(IProduct product);
    
    public String retrieveInitialHTMLPage(String s);

    public String retrieveHTMLPage(String s);

    public Set findPageLinks(String s, List list);

    public void findProducts(String s, List list);

    public String findProductsOnNextScreen(HtmlPage page, String s1, int i);

    public boolean hasNextPage(String s, int pageIndex);

    public List findProductElements(HtmlPage htmlPage, String s, String s1);

    public IProduct extractProductElement(ProductElement productelement);

    public void writeProductInfo(IProduct iproduct);

    public void start(List list);

    public void createOutputFile();

    public void closeOutputFile();

    public String replaceSpecialHtmlChars(String s);

    public String getGroceryPage();
    
    public int getPageIndexIncrement();
    
    public boolean findRecursiveLinks();
    

}
