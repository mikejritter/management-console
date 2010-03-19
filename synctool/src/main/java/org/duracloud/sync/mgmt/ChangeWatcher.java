package org.duracloud.sync.mgmt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: Bill Branan
 * Date: Mar 17, 2010
 */
public class ChangeWatcher implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ChangeWatcher.class);    

    private boolean continueWatch;
    private ChangedList changedList;
    private ChangeHandler handler;
    private long watchFrequency;

    public ChangeWatcher(ChangedList changedList,
                         ChangeHandler handler,
                         long watchFrequency) {
        this.changedList = changedList;
        this.handler = handler;
        this.watchFrequency = watchFrequency;
        continueWatch = true;
    }

    public void run() {
        while(continueWatch) {
            File changedFile = changedList.getChangedFile();
            if(changedFile != null) {
                handler.fileChanged(changedFile);
            } else {
                // List is empty, wait before next check
                try {
                    Thread.sleep(watchFrequency);
                } catch (InterruptedException e) {
                    logger.warn("ChangeWatcher thread interrupted");
                }
            }
        }
    }

    public void endWatch() {
        continueWatch = false;
    }
}