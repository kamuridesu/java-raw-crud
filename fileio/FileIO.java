import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

class FileReadOnlyException extends Exception {
    FileReadOnlyException(String message) {
        super(message);
    }
    FileReadOnlyException() {
        super("Cannot write to file, open in read only mode.");
    }
}

public class FileIO {
    String filename;
    char mode;

    private File file;

    FileIO(String filename) {
        this(filename, 'a');
    }

    FileIO(String filename, char mode) {
        this.filename = filename;
        this.mode = mode;
        this.file = new File(filename);
    }

    String read() throws IOException {
        return Reader.readFileMemoryInefficient(this.file);
    }

    <T extends Object, G extends Object> void read(Function<T, G> callback) throws IOException {
        Reader.readFileBuffered(this.file, callback);
    }

    void write(String content) throws FileReadOnlyException, IOException {
        if (this.mode == 'r') {
            throw new FileReadOnlyException();
        }
        if (this.mode == 'w') {
            var file = new FileWriter(this.file);
            file.append(content);
            file.close();
            return;
        }
        if (this.mode == 'a') {
            var oldContent = this.read();
            oldContent += content;
            var file = new FileWriter(this.file);
            file.append(oldContent);
            file.close();
            return;
        }
    }

    public static void main(String[] args) {

    }
}
