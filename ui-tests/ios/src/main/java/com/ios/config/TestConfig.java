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
        String bundleId,
        boolean realDevice,
        String udid,
        String teamId,
        int wdaLocalPort,
        int wdaLaunchTimeoutSeconds,
        int newCommandTimeoutSeconds,
        boolean noReset,
        int explicitWaitSeconds
) {

    public TestConfig withDeviceName(String value) {
        return new TestConfig(serverUrl, platformVersion, value, appPath, bundleId, realDevice,
                udid, teamId, wdaLocalPort, wdaLaunchTimeoutSeconds, newCommandTimeoutSeconds,
                noReset, explicitWaitSeconds);
    }

    public TestConfig withPlatformVersion(String value) {
        return new TestConfig(serverUrl, value, deviceName, appPath, bundleId, realDevice,
                udid, teamId, wdaLocalPort, wdaLaunchTimeoutSeconds, newCommandTimeoutSeconds,
                noReset, explicitWaitSeconds);
    }

    public TestConfig withWdaLocalPort(int value) {
        return new TestConfig(serverUrl, platformVersion, deviceName, appPath, bundleId, realDevice,
                udid, teamId, value, wdaLaunchTimeoutSeconds, newCommandTimeoutSeconds,
                noReset, explicitWaitSeconds);
    }
}