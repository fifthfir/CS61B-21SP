package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Repository.OBJECTS_DIR;

public class Blob implements Serializable {
    private final byte[] content;

    public Blob(File file) {
        this.content = readContents(file);
    }
    public byte[] getContent() {
        return this.content;
    }

    public void save() {
        saveObjectToFile(OBJECTS_DIR, this);
    }
}
