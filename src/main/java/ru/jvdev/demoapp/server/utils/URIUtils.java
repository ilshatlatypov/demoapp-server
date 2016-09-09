package ru.jvdev.demoapp.server.utils;

/**
 * Created by ilshat on 31.08.16.
 */
public class URIUtils {

    private URIUtils() {}

    public static int getIdFromURI(String uri) {
        int lastSlashIndex = uri.lastIndexOf(StringConstants.SLASH);
        return Integer.parseInt(uri.substring(lastSlashIndex + 1));
    }

    public static String buildURIFromId(int id) {
        // Full URIs are
        // http://localhost:8080/users/{id} for User
        // http://localhost:8080/tasks/{id} for Task
        // but /{id} also works
        return StringConstants.SLASH + id;
    }
}
