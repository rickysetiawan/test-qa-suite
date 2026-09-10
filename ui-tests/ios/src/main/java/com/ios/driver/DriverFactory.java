package com.ios.driver;

import com.ios.config.TestConfig;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    /** Each parallel session needs its own WebDriverAgent port on real devices. */
    private static final AtomicInteger PORT_OFFSET = new AtomicInteger(0);

    private DriverFactory() {
    }

    public static IOSDriver create(TestConfig cfg) {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformVersion(cfg.platformVersion())
                .setDeviceName(cfg.deviceName())
                .setAutomationName("XCUITest")
                .setWdaLaunchTimeout(Duration.ofSeconds(cfg.wdaLaunchTimeoutSeconds()))
                .setNewCommandTimeout(Duration.ofSeconds(cfg.newCommandTimeoutSeconds()))
                .setNoReset(cfg.noReset());

        // Prefer a local build; fall back to launching an app already on the device.
        if (!cfg.appPath().isBlank()) {
            options.setApp(resolveAppPath(cfg.appPath()));
        } else {
            throw new IllegalStateException("Set either ios.app.path or ios.bundle.id");
        }

        if (cfg.realDevice()) {
            int port = cfg.wdaLocalPort() + (PORT_OFFSET.getAndIncrement() % 20);
            options.setUdid(requireValue(cfg.udid(), "ios.udid"))
                    .setWdaLocalPort(port)
                    .setUsePrebuiltWda(true);
            LOG.info("Real device session on udid={} wdaLocalPort={}", cfg.udid(), port);
        }

        IOSDriver driver = new IOSDriver(toUrl(cfg.serverUrl()), options);

        // Explicit waits only. Mixing implicit and explicit waits produces
        // unpredictable timeouts, and on iOS it is a common source of flakiness.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        LOG.info("Session {} started on {} ({})",
                driver.getSessionId(), cfg.deviceName(), cfg.platformVersion());
        return driver;
    }

    private static String resolveAppPath(String configured) {
        Path path = Path.of(configured).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new IllegalStateException(
                    "App not found at " + path + ". Build your .app/.ipa or update ios.app.path.");
        }
        return path.toString();
    }

    private static URL toUrl(String serverUrl) {
        try {
            return new URI(serverUrl).toURL();
        } catch (URISyntaxException | java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Bad Appium server URL: " + serverUrl, e);
        }
    }

    private static String requireValue(String value, String key) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Real device runs require " + key);
        }
        return value;
    }
}
