package com.ios.config;

/**
 * Immutable per-session configuration. TestNG parameters override the values
 * loaded from the properties file via the {@code with*} methods.
 */
public record TestConfig(
        String serverUrl,
        String platformVersion,
        String deviceName,
        String appPath,
        boolean realDevice,
        String udid,
        int wdaLocalPort,
        int wdaLaunchTimeoutSeconds,
        int newCommandTimeoutSeconds,
        boolean noReset,
        int explicitWaitSeconds
) {

    public TestConfig withDeviceName(String value) {
        return new TestConfig(serverUrl, platformVersion, value, appPath, realDevice,
                udid, wdaLocalPort, wdaLaunchTimeoutSeconds, newCommandTimeoutSeconds,
                noReset, explicitWaitSeconds);
    }

    public TestConfig withPlatformVersion(String value) {
        return new TestConfig(serverUrl, value, deviceName, appPath, realDevice,
                udid, wdaLocalPort, wdaLaunchTimeoutSeconds, newCommandTimeoutSeconds,
                noReset, explicitWaitSeconds);
    }

    public TestConfig withWdaLocalPort(int value) {
        return new TestConfig(serverUrl, platformVersion, deviceName, appPath, realDevice,
                udid, value, wdaLaunchTimeoutSeconds, newCommandTimeoutSeconds,
                noReset, explicitWaitSeconds);
    }
}