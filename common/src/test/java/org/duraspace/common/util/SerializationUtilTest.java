package org.duraspace.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Tests the Serialization Utilities.
 *
 * @author Bill Branan
 */
public class SerializationUtilTest {

    protected static final Logger log =
            Logger.getLogger(SerializationUtilTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSerializeDeserialize() throws Exception {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("testName", "testValue");
        testMap.put("foo", "bar");
        testMap.put("dura", "cloud.org");

        String serialized =
            SerializationUtil.serializeMap(testMap);
        Map<String, String> resultMap =
            SerializationUtil.deserializeMap(serialized);

        assertTrue(testMap.equals(resultMap));
    }

    public void testSerializeList() throws Exception
    {
        List<String> testList = new ArrayList<String>();
        testList.add("testName");
        testList.add("foo");
        testList.add("dura");

        String serialized =
            SerializationUtil.serializeList(testList);
        List<Object> resultList =
            SerializationUtil.deserializeList(serialized);

        assertTrue(testList.equals(resultList));
    }

}
