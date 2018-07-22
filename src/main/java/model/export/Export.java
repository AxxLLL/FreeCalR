package model.export;

import java.io.IOException;
import java.nio.file.Path;

public interface Export {
    void exportDataToFile(Path path) throws IOException;
}
