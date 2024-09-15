package org.example.task2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyUsingApacheCommonsIO {
    public static void copyUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }
}
