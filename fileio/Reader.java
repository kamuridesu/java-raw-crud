import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;

public class Reader {
    public static void main(String[] args) throws IOException {
        var file = new File("file.txt");
        readFileBuffered(file, (x) -> print(x));
    }

    public static Object print(Object any) {
        System.out.println(any);
        return null;
    }

    static String readFileMemoryInefficient(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    static String readFileBuffered(File file) throws IOException {
        return readFileBuffered(file, '\n', null);
    }

    static <T, G extends Object> String readFileBuffered(File file, Function<T, G> callback) throws IOException {
        return readFileBuffered(file, '\n', callback);
    }

    /**
     * When callback is not null, it does not returns a String but null as the raw line is used in the callback;
     * @param <G> Generic param for callback
     * @param file File to be read
     * @param delimiter Delimiter to break line
     * @param callback Callback
     * @return String or null
     * @throws IOException
     */
    static <T, G extends Object> String readFileBuffered(File file, char delimiter, Function<T, G> callback) throws IOException {
        var lineBuffer = new StringBuffer(1024);
        var fis = new FileInputStream(file);
        var bis = new BufferedInputStream(fis);
        var iDelim = (int)delimiter;
        int cChar;
        String result = "";

        do {
            cChar = bis.read();
            if (cChar == iDelim) {
                if (callback == null) {
                    result += lineBuffer.toString();
                } else {
                    callback.apply((T)lineBuffer);
                }
                lineBuffer.setLength(0);
                continue;
            }
            if (cChar == -1) {
                continue;
            }
            lineBuffer.append((char)cChar);
        } while (cChar != -1);
        if (lineBuffer.length() > 0) {
            if (callback == null) {
                result += lineBuffer.toString();
            } else {
                callback.apply((T)lineBuffer);
            }
        }

        bis.close();
        fis.close();
        return result;
    }
}
