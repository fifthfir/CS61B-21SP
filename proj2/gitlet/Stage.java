package gitlet;

import java.io.File;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Stage implements Serializable {
    /**
     * HashMap<fileName, blobId>
     */

    private HashMap<String, String> stagingMap;

    private ArrayList<String> removingMap;

    public Stage() {
        this.stagingMap = new HashMap<>();
        this.removingMap = new ArrayList<>();
    }

    /**
     * Read the stage from STAGE file
     */
    public static Stage readStage() {
        if (STAGE.exists()) {
            return readObject(STAGE, Stage.class);
        }
        return null;
    }

    /**
     * Save this stage into STAGE file
     */
    public void saveStageFile() {
        writeObject(STAGE, this);
    }

    /**
     * we now have an existing file, we want to do:
     * - Make a new blob from this file
     * - Compare it with existing staging blobs & last commit blobs:
     *      - if exists in commit and same:
     *              dispose this blob
     *              dispose one in stage, if any
     *      - else, save the new blob anyway
     *
     */
    public void add(String fileName) {
        File file = join(CWD, fileName);
        Blob blob = new Blob(file);

        add(fileName, blob);
    }

    public void add(String fileName, String blobId) {
        Blob blob = readObject(join(BLOBS_DIR, blobId), Blob.class);

        add(fileName, blob);
    }

    private void add(String fileName, Blob blob) {
        String blobId = blob.getBlobId();

        this.stagingMap.put(fileName, blobId);

        String committedId = getHEADMap().get(fileName);
        String addedId = stagingMap.get(fileName);

        if (committedId != null && committedId.equals(blobId)) {
            if (addedId != null) {
                stagingMap.remove(fileName);
            }
            return;
        }

        this.stagingMap.put(fileName, blobId);
        blob.save();
    }


    public void rmStaging(String fileName) {
        stagingMap.remove(fileName);
    }

    public void remove(String fileName) {
        if (!removingMap.contains(fileName)) {
            removingMap.add(fileName);
        }
    }

    public void unremove(String fileName) {
        if (removingMap.contains(fileName)) {
            removingMap.remove(fileName);
        }
    }

    public ArrayList<String> getRmMap() {
        return this.removingMap;
    }

    public HashMap<String, String> getStagingMap() {
        return this.stagingMap;
    }

    public HashMap<String, String> getHEADMap() {
        return getHEADCommit().getMap();
    }

    public HashMap<String, String> getBoundMap() { // a totally new one, not pointer
        HashMap<String, String> boundMap = new HashMap<>(getHEADMap());

        // Add
        boundMap.putAll(this.stagingMap);

        // Remove
        if (!removingMap.isEmpty()) {
            for (String fileName : removingMap) {
                boundMap.remove(fileName);
            }
        }

        return boundMap;
    }

    public void clearMap() {
        this.stagingMap = new HashMap<>();
        this.removingMap = new ArrayList<>();
    }
    public boolean staged(String fileName) {
        return this.stagingMap.get(fileName) != null;
    }
}
