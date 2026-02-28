package me.bobulo.mine.pdam.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for reading resources from the classpath.
 */
public final class ResourceUtil {

    private static final Logger log = LogManager.getLogger(ResourceUtil.class);

    /**
     * Reads the content of a resource located at the specified path and returns it as a byte array.
     *
     * @param path The path to the resource.
     * @return The content of the resource as a byte array, or an empty array if the resource could not be read.
     */
    public static byte[] readBytes(@NotNull String path) {
        try (InputStream is = ResourceUtil.class.getResourceAsStream(path)) {
            if (is == null) {
                log.warn("Resource not found: {}", path);
                return new byte[0];
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            log.warn("Failed to read resource: {}", path, e);
            return new byte[0];
        }
    }

    private ResourceUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}