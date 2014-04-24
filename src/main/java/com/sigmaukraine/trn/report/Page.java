package com.sigmaukraine.trn.report;

import com.sigmaukraine.trn.report.SourceProvider;
import org.openqa.selenium.WebDriver;

public class Page implements SourceProvider {
    
    // Current browser-object
    protected WebDriver driver;
    
    /**
     * @param d
     */
    public Page(WebDriver d) {
        this.driver = d;       
    }    
       
    /**
    * Get HTML-code of current HTML-document
     *
     * @return HTML-code
     */
    @Override
    public String getSource() {
        return "<base target=\"_blank\" href=\"" + "http://vk.com/" + "\"/>\n" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"/>\n" + getHtmlSource();
    }

    @Override
    public String getExtension() {
        return "html";
    }
    
    public String getHtmlSource() {
        return driver.getPageSource();
    }
}