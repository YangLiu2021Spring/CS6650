package clientpart2.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileUtils {
    public static void createFileIfNotExist(String pathname) {
        try {
            File file = new File(pathname);
            file.getAbsoluteFile().createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to create file <%s>", pathname), e);
        }
    }

    public static synchronized void appendToFile(String pathname, String str) {
        try {
            File file = new File(pathname);
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(str);

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to append <%s> to file <%s>", str, pathname), e);
        }
    }

    private FileUtils() {
        throw new UnsupportedOperationException();
    }
}
