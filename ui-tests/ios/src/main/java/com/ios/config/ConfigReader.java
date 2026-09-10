package com.ios.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Loads {@code config/&lt;env&gt;.properties} from the classpath.
 * Any key can be overridden on the command line, e.g.
 * {@code mvn test -Dios.device.name="iPhone SE (3rd generation)"}.
 */
public final class ConfigReader {

    private static final String DEFAULT_ENV = "ios";

    private ConfigReader() {
    }

    public static TestConfig load() {
        return load(System.getProperty("env", DEFAULT_ENV));
    }

    public static TestConfig load(String env) {
        String resource = "config/" + env + ".properties";
        Properties props = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Config file not found on classpath: " + resource);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + resource, e);
        }

        return new TestConfig(
                get(props, "appium.server.url"),
                get(props, "ios.platform.version"),
                get(props, "ios.device.name"),
                get(props, "ios.app.path"),
                Boolean.parseBoolean(get(props, "ios.real.device")),
                get(props, "ios.udid"),
                Integer.parseInt(get(props, "wda.local.port")),
                Integer.parseInt(get(props, "wda.launch.timeout.seconds")),
                Integer.parseInt(get(props, "appium.new.command.timeout.seconds")),
                Boolean.parseBoolean(get(props, "ios.no.reset")),
                Integer.parseInt(get(props, "wait.explicit.seconds"))
        );
    }

    /** System properties win over the file, so CI can override anything. */
    private static String get(Properties props, String key) {
        String fromCli = System.getProperty(key);
        if (fromCli != null && !fromCli.isBlank()) {
            return fromCli.trim();
        }
        String value = props.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        return value.trim();
    }
}
