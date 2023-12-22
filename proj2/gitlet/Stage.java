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
    private ArrayList<String> parents;
    public Stage() {
        this.newlySavedMap = new HashMap<>();
        this.committedMap = new HashMap<>();
        this.parents = new ArrayList<>();
        saveStageFile();
    }
    /**
     * Read the stage from STAGE file
     */
    public static Stage readStage() {
        return readObject(STAGE, Stage.class);
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
     * - TODO: Compare it with existing staging blobs & last commit blobs:
     *      - if exists in commit:
     *          - if same:
     *              dispose it
     *              dispose one in stage, if any
     *
     *      - else, save the new blob anyway (done)
     *
     * @param file
     */
    public void add(File file) {
        String filePath = file.getPath();  // blobFile's full name

        Blob blob = new Blob(file);
        String blobId = getId(blob);

        /**
         * now we have this.savedMap,
         * and a new blob waiting for comparison
         */

        this.newlySavedMap.put(filePath, blobId);
        blob.save();
    }
    public HashMap<String, String> getChangedMap() {
        return this.newlySavedMap;
    }
    public HashMap<String, String> getBoundMap() {
        HashMap<String, String> boundMap = new HashMap<>(this.committedMap);
        boundMap.putAll(this.newlySavedMap);
        return boundMap;
    }
    public void updateCommittedMap() {
        this.committedMap = getBoundMap();
    }
    public ArrayList<String> getParents() {
        return this.parents;
    }
    public void updateParents(String newParent) {
        this.parents.add(newParent);
    }
}
