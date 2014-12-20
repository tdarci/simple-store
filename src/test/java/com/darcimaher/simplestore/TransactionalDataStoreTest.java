package com.darcimaher.simplestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransactionalDataStoreTest {

    private TransactionalDataStore store;

    @Before
    public void setUp() throws Exception {
        store = new TransactionalDataStore();
    }

    @Test
    public void testBeginTransaction() throws Exception {
        Assert.assertFalse(store.inTransaction());
        store.beginTransaction();
        Assert.assertTrue(store.inTransaction());
    }

    @Test
    public void testCommitAllTransactions() throws Exception {
        store.set("a", "apple");
        store.beginTransaction();
        store.set("a", "apple2");
        store.commitAllTransactions();
        store.beginTransaction();
        store.set("a", "apple3");
        store.beginTransaction();
        store.set("a", "apple4");
        Assert.assertEquals("apple4", store.get("a"));
        store.rollbackAllTransactions();
        Assert.assertEquals("apple2", store.get("a"));
        Assert.assertFalse(store.inTransaction());
    }

    @Test
    public void testRollbackOneTransaction() throws Exception {
        store.set("a", "apple");
        store.beginTransaction();
        store.set("a", "apple2");
        store.beginTransaction();
        store.set("a", "apple3");
        Assert.assertEquals("apple3", store.get("a"));
        store.rollbackOneTransaction();
        Assert.assertEquals("apple2", store.get("a"));
        Assert.assertTrue(store.inTransaction());
    }

    @Test
    public void testRollbackAllTransactions() throws Exception {
        store.set("a", "apple");
        store.beginTransaction();
        store.set("a", "apple2");
        store.beginTransaction();
        store.set("a", "apple3");
        Assert.assertEquals("apple3", store.get("a"));
        store.rollbackAllTransactions();
        Assert.assertEquals("apple", store.get("a"));
        Assert.assertFalse(store.inTransaction());
    }

    @Test
    public void testCountWithValue() throws Exception {
        store.set("a", "cat");
        store.set("b", "cat");
        store.set("c", "cat");
        store.set("d", "dog");
        store.set("d", "cat");
        Assert.assertEquals(4, store.countWithValue("cat"));
        store.beginTransaction();
        store.set("e", "cat");
        store.set("f", "cat");
        Assert.assertEquals(6, store.countWithValue("cat"));
        store.beginTransaction();
        store.set("g", "cat");
        Assert.assertEquals(7, store.countWithValue("cat"));
        store.unset("b");
        Assert.assertEquals(6, store.countWithValue("cat"));
    }
}