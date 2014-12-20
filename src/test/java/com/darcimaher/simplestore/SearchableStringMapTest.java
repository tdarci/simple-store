package com.darcimaher.simplestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Set;

public class SearchableStringMapTest {

    private SearchableStringMap m;

    @Before
    public void setUp() throws Exception {
        m = new SearchableStringMap();
    }

    @Test
    public void testPut() throws Exception {
        String keyA = "a";
        String valA = "apple";
        m.put(keyA, valA);
        Assert.assertEquals(1, m.size());
        Set<String> keySet =  m.keysWithValue(valA);
        Assert.assertTrue(keySet.contains(keyA));

        String keyB = "b";
        String valB = "apple";
        m.put(keyA, valA);
        m.put(keyB, valB);
        Assert.assertEquals(2, m.size());
        keySet =  m.keysWithValue(valA);
        Assert.assertTrue(keySet.contains(keyA));
        Assert.assertTrue(keySet.contains(keyB));

        valB = "banana";
        m.put(keyB, valB);
        Assert.assertEquals(2, m.size());
        keySet =  m.keysWithValue(valA);
        Assert.assertTrue(keySet.contains(keyA));
        Assert.assertFalse(keySet.contains(keyB));
        keySet =  m.keysWithValue(valB);
        Assert.assertFalse(keySet.contains(keyA));
        Assert.assertTrue(keySet.contains(keyB));
    }

    @Test
    public void testRemove() throws Exception {
        String keyA = "a";
        String valA = "apple";
        String keyB = "b";
        String valB = "apple";
        m.put(keyA, valA);
        m.put(keyB, valB);

        m.remove(keyA);
        Set<String> keySet =  m.keysWithValue(valA);
        Assert.assertFalse(keySet.contains(keyA));
        Assert.assertTrue(keySet.contains(keyB));
    }

    @Test
    public void testPutAll() throws Exception {

        m.put("a", "aardvark");
        m.put("z", "zebra");

        HashMap funMap = new HashMap();
        funMap.put("a", "apple");
        funMap.put("b", "banana");
        funMap.put("c", "cabbage");
        funMap.put("d", "cabbage");

        m.putAll(funMap);

        Assert.assertEquals(5, m.size());
        Assert.assertEquals(0, m.keysWithValue("aardvark").size());
        Assert.assertTrue(m.keysWithValue("apple").contains("a"));
        Assert.assertEquals(2, m.keysWithValue("cabbage").size());
        Assert.assertTrue(m.keysWithValue("cabbage").contains("c"));
        Assert.assertTrue(m.keysWithValue("cabbage").contains("d"));

    }

}