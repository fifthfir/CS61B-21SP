package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Repository.BLOBS_DIR;

public class Blob implements Serializable {
    private final byte[] content;
    private final String id;

    public Blob(File file) {
        this.content = readContents(file);
        this.id = getId(this);
    }
    public byte[] getContent() {
        return this.content;
    }

    public String getBlobId() {
        return this.id;
    }

    public void save() {
        File objFile = join(BLOBS_DIR, this.id);
        writeObject(objFile, this);
    }
}
