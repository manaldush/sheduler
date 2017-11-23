package com.manaldush.scheduler;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ConfigurationTest {

    private static final Configuration CONFIGURATION = new Configuration();

    @Test
    public void setExecutorBuilder() throws Exception {
        IExecutorBuilder builder = mock(IExecutorBuilder.class);
        CONFIGURATION.setExecutorBuilder(builder);
        Assert.assertTrue(CONFIGURATION.getExecutorBuilder() == builder);
    }

    @Test
    public void setModuleName() throws Exception {
        CONFIGURATION.setModuleName("test");
        Assert.assertTrue(CONFIGURATION.getModuleName() == "test");
    }

    @Test
    public void setThreadsNumber() throws Exception {
        CONFIGURATION.setThreadsNumber(10);
        Assert.assertTrue(CONFIGURATION.getThreadsNumber() == 10);
    }

    @Test
    public void cloneTest() {
        Configuration conf = new Configuration();
        IExecutorBuilder builder = mock(IExecutorBuilder.class);
        conf.setThreadsNumber(10).setModuleName("test").setExecutorBuilder(builder);
        Configuration conf2 = (Configuration) conf.clone();
        assertTrue(conf2.getThreadsNumber() == 10);
        assertTrue(conf2.getModuleName() == "test");
        assertTrue(conf2.getExecutorBuilder() == builder);
        assertFalse(conf == conf2);
    }

}