package com.alibaba;

import com.alibaba.utils.ResourceUtils;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception {
        System.out.println(ResourceUtils.getInputStream("velocity.properties"));
    }
}
