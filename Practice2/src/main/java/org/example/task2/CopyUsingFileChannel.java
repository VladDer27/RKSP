package org.example.task2;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class CopyUsingFileChannel {
    public static void copyUsingFileChannel(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = FileChannel.open(source.toPath(), StandardOpenOption.READ);
             FileChannel destChannel = FileChannel.open(dest.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        }
    }
}
