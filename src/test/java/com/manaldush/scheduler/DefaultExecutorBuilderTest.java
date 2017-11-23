package com.manaldush.scheduler;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultExecutorBuilderTest {
    @Test
    public void build() throws Exception {
        IExecutor executor =new DefaultExecutorBuilder().build();
        Assert.assertTrue(executor != null);
        Assert.assertTrue(executor.getClass() == DefaultExecutor.class);
    }

}