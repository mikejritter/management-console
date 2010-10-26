/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package org.duracloud.account.common.domain;

/**
 * @author Andrew Woods
 *         Date: Oct 8, 2010
 */
public class DuracloudInstance implements Identifiable {
    private String id;

    @Override
    public String getId() {
        return id;
    }
}