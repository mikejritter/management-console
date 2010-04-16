package org.duracloud.sync.endpoint;

import org.junit.Test;

/**
 * @author: Bill Branan
 * Date: Apr 9, 2010
 */
public class TestDuraStoreSyncEndpoint extends DuraStoreSyncTestBase {

    @Test
    public void testDuraStoreSyncEndpoint() throws Exception {
        DuraStoreSyncEndpoint endpoint =
            new DuraStoreSyncEndpoint(host,
                                      Integer.parseInt(port),
                                      context,
                                      rootCredential.getUsername(),
                                      rootCredential.getPassword(),
                                      spaceId);
        testSync(endpoint);
    }
    
}