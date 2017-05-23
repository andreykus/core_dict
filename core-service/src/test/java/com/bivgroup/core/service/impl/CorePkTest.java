package com.bivgroup.core.service.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by andreykus on 19.08.2016.
 */
public class CorePkTest extends AbstractTest {

    @Test(threadPoolSize = 3)
    public void getNewId() throws Exception {
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");

        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");
        coreFacade.getNewId("d");

        Assert.assertEquals(coreFacade.getNewId("d"), (Long) 19L);
    }
}
