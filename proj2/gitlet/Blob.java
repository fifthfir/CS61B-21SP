package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Repository.OBJECTS_DIR;

public class Blob implements Serializable {
    private final byte[] content;
    private final String id;

    public Blob(File file) {
        this.content = readContents(file);
        this.id = sha1(serialize(this));
    }

    public void save() {
        saveObjectToFile(OBJECTS_DIR, this);
    }
}
