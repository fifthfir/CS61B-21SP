package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        stage.saveStageFile();
    }

    public static void commit(String message) {
        Stage stage = readStage();

        // Read from computer [the head commit object] and [the staging area]
        HashMap<String, String> stagingMap = stage.getChangedMap();
        if (stagingMap.isEmpty()) {
            exitWString("No changes added to the commit.");
        }

        // Newest Map of alllll changes

        // When initialize a new commit,
        // should the Map and parents be the newest version, or the former version?
        HashMap<String, String> newestMap = stage.getBoundMap();
        String newestParent = stage.getParent();

        // Clone the HEAD commit
        // Modify its message and timestamp according to user input
            // How to get the parent commits?
        Commit newCommit = new Commit(message, newestMap, newestParent);
        // Use the staging area in order to modify the files tracked by the new commit
        // Write back new objects made or any modified objects created earlier

        saveObjectToFile(OBJECTS_DIR, newCommit);

        stage.updateMaps();
        stage.updateParent(getId(newCommit));
        stage.saveStageFile();
    }
    public static void log() {
        Stage stage = readStage();
        String curCommit = stage.getParent();
        printCommitFrom(curCommit);
    }

    private static void printCommitFrom(String commitId) {
        System.out.println("===");

        File thisCommitFile = join(OBJECTS_DIR, commitId);
        Commit thisCommit = readObject(thisCommitFile, Commit.class);

        System.out.print("commit ");
        System.out.println(commitId);
        System.out.print("Date: ");
        System.out.println(thisCommit.getTimestampFormatted());
        System.out.println(thisCommit.getMessage());

        if (!Objects.equals(thisCommit.getParent(), "")) {
//            System.out.println("Parent: " + thisCommit.getParent());
            System.out.println();
            printCommitFrom(thisCommit.getParent());
        }
    }

    /**
     * go through every commit to check its message
     * [stage]:
     * [current parent (last commit) id -> curCommitFile
     * -> curCommitObj -> ?curCommitObj.getMessage() == message;]
     * -> curCommitObj.getParent id -> curCommitParentFile -> ...
     */
    public static void find(String message) {
        Stage stage = readStage();
        assert stage != null;
        String curCommitId = stage.getParent();

        ArrayList<String> foundList = new ArrayList<>();
        findCommits(curCommitId, message, foundList);
        printArrayList(foundList);
    }
    private static void findCommits(String curCommitId, String message, ArrayList<String> aList) {
        if (Objects.equals(curCommitId, "")) {
            return;
        } else if (findThisCommit(curCommitId, message) == 1) {
            aList.add(curCommitId);
        }
        findCommits(findParentFromId(curCommitId), message, aList);
    }
    
    private static Integer findThisCommit(String commitId, String message) {
        File commitFile = getFileFromName(OBJECTS_DIR, commitId);
        Commit thisCommit = readObject(commitFile, Commit.class);

        if (Objects.equals(thisCommit.getMessage(), message)) {
            return 1;
        }
        return 0;
    }
    private static String findParentFromId(String commitId) {
        File commitFile = getFileFromName(OBJECTS_DIR, commitId);
        Commit thisCommit = readObject(commitFile, Commit.class);
        return thisCommit.getParent();
    }
    
    public static void remove(String fileName) {
        Stage stage = readStage();

        stage.rmStaging(getPathFromName(CWD, fileName));
        stage.saveStageFile();
    }

    public static void checkMap() {
        Stage stage = readStage();
        if (stage != null) {
            System.out.println("Staging Map: ");
            printHashMap(stage.getChangedMap());
            System.out.println("Committed Map: ");
            printHashMap(stage.getCommittedMap());
        }
    }

    /**
     * fileName -> filePath -> committedMap -> blob id -> blob file
     * -> blob object -> content
     * @param fileName
     */
    public static void checkoutFile(String fileName) {
        Stage stage = readStage();
        HashMap<String, String> comMap = stage.getCommittedMap();

        writeFileFromMap(comMap, fileName);
    }

    public static void checkoutCommit(String commitId, String fileName) {
        File commitFile = getFileFromName(OBJECTS_DIR, commitId);
        Commit thisCommit = readObject(commitFile, Commit.class);
        HashMap<String, String> comMap = thisCommit.getMap();

        writeFileFromMap(comMap, fileName);
    }

    public static void checkoutBranch(String branch) {
        
    }

    public static void writeFileFromMap(HashMap<String, String> map, String fileName) {
        String filePath = getPathFromName(CWD, fileName);
        File file = getFileFromName(CWD, fileName);

        String blobId = map.get(filePath);
        File blobFile = getFileFromName(OBJECTS_DIR, blobId);
        Blob fileBlob = readObject(blobFile, Blob.class);

        byte[] content = fileBlob.getContent();
        writeContents(file, content);
    }


}
