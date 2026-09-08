package com.ios.listeners;

import com.acme.mobile.utils.Screenshots;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("START  {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("PASS   {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = result.getMethod().getMethodName();
        LOG.error("FAIL   {}", name, result.getThrowable());

        byte[] png = Screenshots.capture();
        if (png != null) {
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), ".png");
            Path saved = Screenshots.saveToDisk(name, png);
            if (saved != null) {
                LOG.info("Screenshot: {}", saved.toAbsolutePath());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIP   {}", result.getMethod().getMethodName());
    }
}
