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
}
