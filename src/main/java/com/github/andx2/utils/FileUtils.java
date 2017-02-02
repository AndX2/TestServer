package com.github.andx2.utils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by savos on 30.10.2016.
 */
public class FileUtils {

    public static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static Path renameTo(Path oldName, String newNameString) {

        try {
            return Files.move(oldName, oldName.resolveSibling(newNameString), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
