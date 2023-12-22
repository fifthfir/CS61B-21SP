package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import static gitlet.Stage.*;
import static gitlet.Utils.*;
import static gitlet.Utils.exitWString;
import static java.lang.System.exit;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Ruotian Zhang
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commit directory. */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File STAGE = join(GITLET_DIR, "stage");

    /* TODO: fill in the rest of this class. */
    /**
     * Directory Structure:
     * - .gitlet
     *      - objects: all objects
     *      - stage: a file of staging area, storing all added yet not committed files
     */

    /**
     * TODO: init
     * TODO: branch
     */
    public static void init() {

        boolean initialExists = OBJECTS_DIR.exists();

        if (!initialExists) {
            GITLET_DIR.mkdir();
            OBJECTS_DIR.mkdir();

            Commit initialCommit = new Commit();
            saveObjectToFile(OBJECTS_DIR, initialCommit);

        } else {
            exitWString("A Gitlet version-control system already exists in the current directory.");
        }
    }

    /**
     * TODO: add
     * TODO: Adds a copy of a file as it currently exists to the staging area.
     *          - Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     *          - if identical to the last commit, remove from stage
     *              -- commit, change, add, change back, then should be removed from stage
     */
    public static void add(String fileName) throws IOException {

        // Check the file exists
        File thisFile = join(CWD, fileName);
        boolean fileExists = thisFile.exists();
        boolean stageExists = STAGE.exists();

        if (!fileExists) {
            exitWString("File does not exist.");
        }

        // file exists, so let stage deal with it
        Stage stage;
        if (!stageExists) {
            STAGE.createNewFile();
            stage = new Stage();
        } else {
            stage = readStage();
        }

        stage.add(thisFile);
    }
    public static void commit(String message) {
        Stage stage = readStage();

        // Read from computer [the head commit object] and [the staging area]
        HashMap<String, String> stagingMap = stage.getChangedMap();
        if (stagingMap == null) {
            exitWString("No changes added to the commit.");
        }

        // Newest Map of alllll changes
        HashMap<String, String> newestMap = stage.getBoundMap();
        ArrayList<String> newestParents = stage.getParents();

        // Clone the HEAD commit
        // Modify its message and timestamp according to user input
            // How to get the parent commits?
        Commit newCommit = new Commit(message, newestMap, newestParents);
        // Use the staging area in order to modify the files tracked by the new commit
        // Write back new objects made or any modified objects created earlier

        saveObjectToFile(OBJECTS_DIR, newCommit);

        stage.updateCommittedMap();
        stage.updateParents(getId(newCommit));
        stage.saveStageFile();
    }
}
