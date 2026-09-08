package com.ios.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Use sparingly and always log it. Retries hide real flakiness if you stop
 * looking at how often they fire. Configure with -Dretry.count=2.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES =
            Integer.parseInt(System.getProperty("retry.count", "1"));

    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            return true;
        }
        return false;
    }
}
