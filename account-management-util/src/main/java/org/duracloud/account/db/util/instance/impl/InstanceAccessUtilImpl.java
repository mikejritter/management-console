/*
 * Copyright (c) 2009-2011 DuraSpace. All rights reserved.
 */
package org.duracloud.account.db.util.instance.impl;

import org.duracloud.account.db.util.instance.InstanceAccessUtil;
import org.duracloud.account.db.util.instance.InstanceUtil;
import org.duracloud.account.db.util.error.DuracloudInstanceUpdateException;
import org.duracloud.common.web.RestHttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Bill Branan
 * Date: 4/1/11
 */
public class InstanceAccessUtilImpl implements InstanceAccessUtil, InstanceUtil {

    private Logger log = LoggerFactory.getLogger(InstanceAccessUtilImpl.class);

    private static final String PROTOCOL = "https://";
    private static final String INIT_PATH = "/init";
    private static final int SLEEP_TIME = 10000;

    @Override
    public void waitInstanceAvailable(String hostname, long timeout) {
        log.debug("Waiting for instance at {} to become available", hostname);
        long start = System.currentTimeMillis();
        while(!instanceAvailable(hostname)) {
            long now = System.currentTimeMillis();
            if(now - start > timeout) {
                String error = "Instance at host " + hostname +
                   " was not available prior to wait timeout of " +
                   timeout + " milliseconds.";
                throw new DuracloudInstanceUpdateException(error);
            } else {
                sleep(SLEEP_TIME);
            }
        }
        log.debug("Instance at {} is now available.", hostname);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException e) {
        }
    }

    @Override
    public boolean instanceAvailable(String hostname) {
        RestHttpHelper restHelper = new RestHttpHelper();

        String duradminUrl =
            PROTOCOL + hostname + "/" + DURADMIN_CONTEXT + INIT_PATH;
        String durastoreUrl =
            PROTOCOL + hostname + "/" + DURASTORE_CONTEXT + INIT_PATH;
        String durareportUrl =
            PROTOCOL + hostname + "/" + DURABOSS_CONTEXT + INIT_PATH;

        try {
            if(!checkResponse(restHelper.get(duradminUrl)) ||
               !checkResponse(restHelper.get(durastoreUrl)) ||
               !checkResponse(restHelper.get(durareportUrl))) {
                return false;
            } else {
                return true;
            }
        } catch(Exception e) {
            return false;
        }
    }

    private boolean checkResponse(RestHttpHelper.HttpResponse response) {
        int statusCode = response.getStatusCode();
        if(statusCode == 200 || statusCode == 503) {
            return true;
        } else {
            return false;
        }
    }

}