package org.duracloud.duraservice.domain;

import junit.framework.TestCase;
import org.duracloud.duraservice.rest.RestTestHelper;
import org.duracloud.serviceconfig.ServiceInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Tests ServiceManager
 *
 * @author Bill Branan
 */
public class ServiceManagerTest
        extends TestCase {

    MockServiceManager serviceManager;

    @Override
    @Before
    protected void setUp() throws Exception {
        serviceManager = new MockServiceManager();
        String configXml = RestTestHelper.buildTestInitXml();
        ByteArrayInputStream configStream =
            new ByteArrayInputStream(configXml.getBytes("UTF-8"));
        serviceManager.configure(configStream);
    }

    @Override
    @After
    protected void tearDown() throws Exception {
    }

    @Test
    public void testNotConfigured() throws Exception {
        serviceManager = new MockServiceManager();
        String failMsg = "Should throw an exception if not initialized";

        try{
            serviceManager.getAvailableServices();
            fail(failMsg);
        } catch (RuntimeException re) {}

        try{
            serviceManager.getDeployedServices();
            fail(failMsg);
        } catch (RuntimeException re) {}        

        try{
            serviceManager.getService("testServiceId");
            fail(failMsg);
        } catch (RuntimeException re) {}

        try{
            serviceManager.getDeployedService("testServiceId", 0);
            fail(failMsg);
        } catch (RuntimeException re) {}

        try{
            serviceManager.deployService("testServiceId", "test", "1.0", null);
            fail(failMsg);
        } catch (RuntimeException re) {}

        try{
            serviceManager.updateServiceConfig("testServiceId", 0, null);
            fail(failMsg);
        } catch (RuntimeException re) {}

        try{
            serviceManager.undeployService("testServiceId", 0);
            fail(failMsg);
        } catch (RuntimeException expected) {}
    }

    @Test
    public void testGetAvailableServices() throws Exception {
        List<ServiceInfo> services = serviceManager.getAvailableServices();
        assertTrue(services.size() == 3);
        assertTrue(services.contains(MockServiceManager.SERVICE_PACKAGE_1));
        assertTrue(services.contains(MockServiceManager.SERVICE_PACKAGE_2));
        assertTrue(services.contains(MockServiceManager.SERVICE_PACKAGE_3));
    }

    @Test
    public void testGetDeployedServices() throws Exception {
        List<String> services = serviceManager.getDeployedServices();
        assertTrue(services.size() == 0);
    }

    @Test
    public void testDeployService() throws Exception {
        serviceManager.deployService(MockServiceManager.SERVICE_PACKAGE_1, "");
        List<String> services = serviceManager.getDeployedServices();
        assertTrue(services.size() == 1);
        assertTrue(services.contains(MockServiceManager.SERVICE_PACKAGE_1));
        assertFalse(services.contains(MockServiceManager.SERVICE_PACKAGE_2));
        assertFalse(services.contains(MockServiceManager.SERVICE_PACKAGE_3));

        try {
            serviceManager.deployService("invalid-service", null);
            fail("Should throw an exception trying to deploy invalid service");
        } catch (ServiceException expected) {
            assertNotNull(expected);
        }
    }

    @Test
    public void testConfigureService() throws Exception {
        String configXml = RestTestHelper.buildTestServiceConfigXml();
        ByteArrayInputStream configStream =
            new ByteArrayInputStream(configXml.getBytes("UTF-8"));

        try {
            serviceManager.configureService(MockServiceManager.SERVICE_PACKAGE_1,
                                            configStream);
            fail("Configure Service should fail if the service is not deployed");
        } catch(ServiceException expected){}

        serviceManager.deployService(MockServiceManager.SERVICE_PACKAGE_1, "");
        serviceManager.configureService(MockServiceManager.SERVICE_PACKAGE_1,
                                        configStream);
    }

    @Test
    public void testUnDeployService() throws Exception {
        serviceManager.deployService(MockServiceManager.SERVICE_PACKAGE_1, "");
        List<String> services = serviceManager.getDeployedServices();
        assertTrue(services.size() == 1);

        serviceManager.undeployService(MockServiceManager.SERVICE_PACKAGE_1);
        services = serviceManager.getDeployedServices();
        assertTrue(services.size() == 0);
    }

    @Test
    public void testGetServicesHosts() throws Exception {
        List<String> serviceHosts = serviceManager.getServiceHosts();
        assertNotNull(serviceHosts);
        assertTrue(serviceHosts.size() > 0);
        assertTrue(serviceHosts.contains("localhost"));
    }

    @Test
    public void testAddServicesInstance() throws Exception {
        serviceManager.addServicesInstance();
    }

}