package gitlet;

import java.io.File;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;

import static gitlet.Repository.STAGE;
import static gitlet.Utils.*;

public class Stage implements Serializable {
    /**
     * HashMap<fileName, blobId>
     *        <search  , compare content>
     */
    private HashMap<String, String> newlySavedMap;
    private HashMap<String, String> committedMap;
    private String parent;  // Only hash id
    public Stage() {
        this.newlySavedMap = new HashMap<>();
        this.committedMap = new HashMap<>();
        this.parent = "";
        saveStageFile();
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
     * @param file
     */
    public void add(File file) {
        String filePath = file.getPath();  // blobFile's full name

        Blob blob = new Blob(file);
        String blobId = getId(blob);

        String committedId = committedMap.get(filePath);
        String addedId = newlySavedMap.get(filePath);

        if (committedId != null && committedId.equals(blobId)) {
            if (addedId != null) {
                newlySavedMap.remove(filePath);
            }
            return;
        }

        this.newlySavedMap.put(filePath, blobId);
        blob.save();
    }

    public void rmStaging(String filePath) {
        printHashMap(newlySavedMap);

        String stagingId = newlySavedMap.get(filePath);
        String committedId = committedMap.get(filePath);

        System.out.print("stagingId: ");
        System.out.println(stagingId);
        System.out.print("committedId: ");
        System.out.println(committedId);

        if (stagingId == null && committedId == null) {
            exitWString("No reason to remove the file.");
        }

        if (committedId != null) {
            restrictedDelete(filePath);
            committedMap.remove(filePath);
        }

        if (stagingId != null) {
            newlySavedMap.remove(filePath);
        }
    }

    public HashMap<String, String> getChangedMap() {
        return this.newlySavedMap;
    }

    public HashMap<String, String> getCommittedMap() {
        return this.committedMap;
    }

    public HashMap<String, String> getBoundMap() {
        HashMap<String, String> boundMap = new HashMap<>(this.committedMap);
        boundMap.putAll(this.newlySavedMap);
        return boundMap;
    }

    public void updateMaps() {
        this.committedMap = getBoundMap();
        this.newlySavedMap = new HashMap<>();
    }

    public String getParent() {
        return this.parent;
    }

    public void updateParent(String newParent) {
        this.parent = newParent;
    }
}
