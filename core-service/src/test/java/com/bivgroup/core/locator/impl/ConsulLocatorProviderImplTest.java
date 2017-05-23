package com.bivgroup.core.locator.impl;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;

/**
 * Created by bush on 17.08.2016.
 */
public class ConsulLocatorProviderImplTest {
    ConsulLocatorProviderImpl pro;

    @BeforeClass
    public void setUp() throws Exception {
        pro = new ConsulLocatorProviderImpl();
    }

    @Test(enabled = false)
    public void register() throws Exception {
        pro.registerService("test", new URL("http://mail.ru"));
    }

    @Test(enabled = false)
    public void getService() throws Exception {
        URL url = pro.getUrlServiceByName("test");
        Assert.assertEquals(url.getPort(), 8080);
    }


}