package name.ixr.nano.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by yuuji on 12/12/13.
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {

    private static JarFile getJarFile(URL url) {
        try {
            String urlStr = url.getFile();
            String jarPath = urlStr.substring(5, urlStr.indexOf("!/"));
            JarFile jarFile = new JarFile(jarPath);
            return jarFile;
        } catch (IOException ex) {
            return null;
        }
    }

    public static InputStream getInputStream(String resourceLocation) {
        try {
            URL url = getClassPathURL(resourceLocation);
            if (url == null) {
                return null;
            }
            if (isJarURL(url)) {
                JarFile jarFile = getJarFile(url);
                JarEntry jarEntry = jarFile.getJarEntry(resourceLocation);
                return jarFile.getInputStream(jarEntry);
            } else {
                File file = new File(url.getFile());
                return new FileInputStream(file);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static InputStream getInputStream(Resource resource) {
        return getInputStream(resource.getPath());
    }

    public static Resource getClassPathResource(String resourceLocation) {
        Resource resource = new Resource();
        resource.setPath(resourceLocation);
        URL url = getClassPathURL(resourceLocation);
        if (url == null) {
            return resource;
        }
        if (isJarURL(url)) {
            JarFile jarFile = getJarFile(url);
            JarEntry jarEntry = jarFile.getJarEntry(resourceLocation);
            if (jarEntry == null) {
                return null;
            }
            resource.setDirectory(jarEntry.isDirectory());
            resource.setExists(true);
            return resource;
        } else {
            File file = new File(url.getFile());
            resource.setDirectory(file.isDirectory());
            if (file.exists()) {
                resource.setExists(true);
            }
            return resource;
        }
    }

    public static URL getClassPathURL(String resourceLocation) {
        try {
            URL url;
            if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
                url = org.springframework.util.ResourceUtils.getURL(resourceLocation);
            } else {
                url = org.springframework.util.ResourceUtils.getURL(CLASSPATH_URL_PREFIX + resourceLocation);
            }
            return url;
        } catch (IOException ex) {
            return null;
        }
    }


    public static class Resource {
        private String path;
        private boolean isDirectory;
        private boolean exists;

        public void setExists(boolean exists) {
            this.exists = exists;
        }

        public boolean exists() {
            return exists;
        }

        public String getName() {
            if (isDirectory()) {
                return FilenameUtils.getName(path.substring(0, path.length() - 1));
            }
            return StringUtils.substringBefore(FilenameUtils.getName(path), ".");
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setDirectory(boolean isDirectory) {
            this.isDirectory = isDirectory;
        }

        public String getPath() {
            return path;
        }

        public boolean isDirectory() {
            return isDirectory;
        }
    }
}
