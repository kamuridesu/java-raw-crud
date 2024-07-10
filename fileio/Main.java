import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, FileReadOnlyException {
        var builder = new StringBuilder();
        var file = new FileIO("./file.txt", 'w');
        file.read(x -> builder.append(x.toString() + "\n"));
        var items = builder.toString();
        System.out.println(items);
    }
}
