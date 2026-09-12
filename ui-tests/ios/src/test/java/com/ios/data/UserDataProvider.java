package com.ios.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

public class UserDataProvider {

    private static final String RESOURCE = "testdata/users.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static User byType(String type) {
        return all().stream()
                .filter(u -> u.type().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No user of type " + type));
    }

    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        return all().stream()
                .filter(u -> !"valid".equals(u.type()))
                .map(u -> new Object[]{u})
                .toArray(Object[][]::new);
    }

    private static List<User> all() {
        try (InputStream in = UserDataProvider.class.getClassLoader()
                .getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Missing " + RESOURCE);
            }
            return MAPPER.readValue(in, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
