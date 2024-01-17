package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;

import static gitlet.Stage.*;
import static gitlet.Utils.*;
import static gitlet.Utils.restrictedDelete;
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
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");

    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    public static final File STAGE = join(GITLET_DIR, "stage");
    private static final File BRANCH_DIR = join(GITLET_DIR, "branches");
    private static final File MASTER = join(BRANCH_DIR, "master");
    private static final File HEAD = join(GITLET_DIR, "HEAD");

    private static String getHEADBranchName() {
        return readObject(HEAD, String.class);
    }
    public static Commit getHEADCommit() {
        return readObject(join(BRANCH_DIR, getHEADBranchName()), Commit.class);
    }


    public static void init() throws IOException {

        boolean initialExists = OBJECTS_DIR.exists();

        if (!initialExists) {
            GITLET_DIR.mkdir();
            OBJECTS_DIR.mkdir();
            COMMITS_DIR.mkdir();
            BLOBS_DIR.mkdir();
            BRANCH_DIR.mkdir();
            MASTER.createNewFile();

            Commit initialCommit = new Commit();
            initialCommit.save();

            writeObject(MASTER, initialCommit);
            writeObject(HEAD, "master");

            Stage stage = new Stage();
            stage.saveStageFile();

        } else {
            exitWString("A Gitlet version-control system already exists in the current directory.");
        }
    }

    public static void add(String fileName) {

//        checkMap();

        // Check the file exists
        File thisFile = join(CWD, fileName);;

        if (!thisFile.exists() || !STAGE.exists()) {
            exitWString("File does not exist.");
        }

        // file exists, so let stage deal with it
        Stage stage;
        stage = readStage();

        stage.add(fileName);
        stage.saveStageFile();
    }

    public static void commit(String message) {
        Stage stage = readStage();

        // Read from computer [the head commit object] and [the staging area]
        HashMap<String, String> stagingMap = stage.getStagingMap();

        if (stagingMap.isEmpty()) {
            exitWString("No changes added to the commit.");
        }

        // Newest Map of alllll changes
        HashMap<String, String> newestMap = stage.getBoundMap();
        String parentId = getHEADCommit().getCommitId();

        Commit newCommit = new Commit(message, newestMap, parentId);
        // Use the staging area in order to modify the files tracked by the new commit
        // Write back new objects made or any modified objects created earlier
        newCommit.save();

        String curBranch = readObject(HEAD, String.class);

        writeObject(join(BRANCH_DIR, curBranch), newCommit);
        writeObject(HEAD, curBranch);

        stage.clearSMap();
        stage.saveStageFile();
    }

    // TODO
    private static void mergeCommit(String givenBranchName) {
        String curBranchName = readObject(HEAD, String.class);
        String msg = "Merged " + givenBranchName + " into " + curBranchName;

        commit(msg);
        // TODO: Deal with multi parents
    }

    public static void log() {
        Commit curCommit = getHEADCommit();
        printCommitFrom(curCommit.getCommitId());
    }

    public static void gLog() {
        List<String> commitIds = plainFilenamesIn(COMMITS_DIR);
        for (String commitId : commitIds) {
            printCommit(commitId);
        }
    }

    private static void printCommitFrom(String commitId) {
        Commit thisCommit = getCommitFromName(COMMITS_DIR, commitId);

        printCommit(commitId);

        if (!Objects.equals(thisCommit.getParent(), "")) {
            System.out.println();
            printCommitFrom(thisCommit.getParent());
        }
    }

    private static void printCommit(String commitId) {

        Commit thisCommit = getCommitFromName(COMMITS_DIR, commitId);

        System.out.println("===");
        System.out.print("commit ");
        System.out.println(commitId);
        System.out.print("Date: ");
        System.out.println(thisCommit.getTimestampFormatted());
        System.out.println(thisCommit.getMessage());
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

        String curCommitId = getHEADCommit().getCommitId();

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
        Commit thisCommit = getCommitFromName(COMMITS_DIR, commitId);

        if (Objects.equals(thisCommit.getMessage(), message)) {
            return 1;
        }
        return 0;
    }

    private static String findParentFromId(String commitId) {
        return getCommitFromName(COMMITS_DIR, commitId).getParent();
    }
    
    public static void rm(String fileName) {
        Stage stage = readStage();
        Commit HEADCommit = getHEADCommit();

        boolean fileStaged = stage.staged(fileName);
        String fileCurCommitted = HEADCommit.getMap().get(fileName);


        if (!fileStaged && fileCurCommitted == null) {
            exitWString("No reason to remove the file.");
        }

        if (fileStaged) {
            stage.rmStaging(fileName);
            stage.saveStageFile();
        }

        if (fileCurCommitted != null) {
            stage.remove(fileName);
            restrictedDelete(join(CWD, fileName));
        }

    }

    public static void checkMap() {
        Stage stage = readStage();
        if (stage != null) {
            System.out.println("Staging Map: ");
            printHashMap(stage.getStagingMap());
            System.out.println("HEAD Map: ");
            printHashMap(stage.getHEADMap());
        }
    }

    /**
     * fileName -> filePath -> committedMap -> blob id -> blob file
     * -> blob object -> content
     */
    public static void checkoutFile(String fileName) throws IOException {
        Commit headCommit = getHEADCommit();
        checkoutCommitObj(headCommit, fileName);
    }

    public static void checkoutCommitObj(Commit commit, String fileName) throws IOException {
        HashMap<String, String> comMap = commit.getMap();
        writeAFileFromMap(comMap, fileName);
    }

    public static void checkoutCommit(String commitId, String fileName) throws IOException {
        checkCommitId(commitId);

        HashMap<String, String> comMap = getMapFromCommitId(commitId);
        writeAFileFromMap(comMap, fileName);
    }

    /**
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist.
     *
     */
    public static void checkoutBranch(String branchName) throws IOException {
        File branchFile = join(BRANCH_DIR, branchName);
        Stage stage = readStage();

        // Checks
        if (!branchFile.exists()) {
            exitWString("No such branch exists.");
        }

        if (Objects.equals(getHEADBranchName(), branchName)) {
            exitWString("No need to checkout the current branch.");
        }

        Commit brCommit = readObject(branchFile, Commit.class);
        ifAllTracked();
        removeCurCommit();

        // then write all files from the given commit id
        HashMap<String, String> newMap = brCommit.getMap();
        writeAllFilesFromMap(newMap);

        if (getHEADCommit() != brCommit) {
            stage.clearSMap();
        }

        // last, write this new branch file into HEAD
        writeObject(HEAD, branchName);
        stage.saveStageFile();

    }

    private static void writeAFileFromMap(HashMap<String, String> map, String fileName) throws IOException {
        checkFileCommitted(map, fileName);
        String blobId = map.get(fileName);
        writeFileFromFB(fileName, blobId);
    }

    private static void writeAllFilesFromMap(HashMap<String, String> map) throws IOException {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();

            writeFileFromFB(fileName, blobId);
        }
    }

    private static void checkFileCommitted(HashMap<String, String> map, String fileName) {
        if (map.get(fileName) == null) {
            exitWString("File does not exist in that commit.");
        }
    }

    private static void checkCommitId(String commitId) {
        List<String> commits = plainFilenamesIn(COMMITS_DIR);
        assert commits != null;
        for (String committedId : commits) {
            if (Objects.equals(committedId, commitId)) {
                return;
            }
        }
        exitWString("No commit with that id exists.");
    }

    public static void reset(String commitId) throws IOException {
        checkCommitId(commitId);

        Stage stage = readStage();
        assert stage != null;

        ifAllTracked();
        removeCurCommit();

        HashMap<String, String> committedFileMap = getMapFromCommitId(commitId);
        writeAllFilesFromMap(committedFileMap);
        stage.clearSMap();

        String curBranchName = readObject(HEAD, String.class);
        writeObject(join(BRANCH_DIR, curBranchName), join(COMMITS_DIR, commitId));

        stage.saveStageFile();
    }

    private static void ifAllTracked() {
        Commit curCommit = getHEADCommit();
        HashMap<String, String> trackedMap = curCommit.getMap();
        List<String> fileNames = plainFilenamesIn(CWD);
        for (String fileName : fileNames) {
            if (trackedMap.get(fileName) == null) {
                exitWString("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }
    }

    private static void removeCurCommit() {
        Commit curCommit = getHEADCommit();
        HashMap<String, String> MapToDelete = curCommit.getMap();
        for (Map.Entry<String, String> entry : MapToDelete.entrySet()) {
            String fileName = entry.getKey();
            restrictedDelete(join(CWD, fileName));
        }
    }

    private static void writeFileFromFB(String fileName, String blobId) throws IOException {
        File file = join(CWD, fileName);

        File blobFile = join(BLOBS_DIR, blobId);
        Blob blob = readObject(blobFile, Blob.class);

        if (file.exists()) {
            restrictedDelete(file);
            file.createNewFile();
        } else {
            file.createNewFile();
        }

        byte[] content = blob.getContent();
        writeContents(file, content);
    }

    public static void branch(String branchName) throws IOException {
        File branchFile = join(BRANCH_DIR, branchName);
        if (branchFile.exists()) {
            exitWString("A branch with that name already exists.");
        }

        branchFile.createNewFile();
        writeObject(branchFile, getHEADCommit());
    }

    // TODO
    public static void status() {
        // Branches
        printStatusTitle("Branches");

        List<String> branchNames = plainFilenamesIn(BRANCH_DIR);
        String HEADBranchName = getHEADBranchName();

        for (String branchName : branchNames) {
            if (Objects.equals(HEADBranchName, branchName)) {
                System.out.print("*");
            }
            System.out.println("branchName");
        }


        // Staged Files
        System.out.println();
        printStatusTitle("Staged Files");

        Stage stage = readStage();
        HashMap<String, String> stagingMap = stage.getStagingMap();

        for (Map.Entry<String, String> entry : stagingMap.entrySet()) {
            String fileName = entry.getKey();
            System.out.println(fileName);
        }

        // Removed Files
        System.out.println();
        printStatusTitle("Removed Files");

        // Modifications Not Staged For Commit
        System.out.println();
        printStatusTitle("Modifications Not Staged For Commit");

        // Untracked Files
        System.out.println();
        printStatusTitle("Untracked Files");


        System.out.println();

    }

    private static void printStatusTitle(String title) {
        System.out.println("=== " + title + " ===");
    }

    public static void rmBranch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);

        // Check
        if (!branchFile.exists()) {
            exitWString("A branch with that name does not exist.");
        }

        String curBranchName = getHEADBranchName();
        if (Objects.equals(branchName, curBranchName)) {
            exitWString("Cannot remove the current branch.");
        }

        restrictedDelete(branchFile);
    }

    // TODO
    public static void merge(String branchName) throws IOException {
        Stage stage = readStage();

        if (!stage.getStagingMap().isEmpty()) {
            exitWString("You have uncommitted changes.");
        }

        File branchFile = join(BRANCH_DIR, branchName);
        String curBranchName = readObject(HEAD, String.class);

        if (!branchFile.exists()) {
            exitWString("A branch with that name does not exist.");
        }

        if (Objects.equals(branchName, curBranchName)) {
            exitWString("Cannot merge a branch with itself.");
        }

        Commit curBranch = getHEADCommit();
        Commit givenBranch = getCommitFromName(BRANCH_DIR, branchName);
        Commit splitPoint = Commit.findAncestor(curBranch, givenBranch);

        if (Objects.equals(splitPoint, givenBranch)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        } else if (Objects.equals(splitPoint, curBranch)) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        boolean conflict = false;

        // for every file:
        HashMap<String, String> CBMap = curBranch.getMap();
        assert splitPoint != null;
        HashMap<String, String> SPMap = splitPoint.getMap();
        HashMap<String, String> GBMap = givenBranch.getMap();

        List<String> fileNames = plainFilenamesIn(CWD);

        assert fileNames != null;
        for (String fileName : fileNames) {
            String CBId = CBMap.get(fileName);
            String SPId = SPMap.get(fileName);
            String GBId = GBMap.get(fileName);

            if (CBId == null) {
                exitWString("There is an untracked file in the way; delete it, or add and commit it first.");
            }

            // no one absent
            if (SPId != null && CBId != null && GBId != null) {

                // 1.
                // modified in the given branch since the split point
                // but not modified in the current branch

                if (Objects.equals(SPId, CBId) && !Objects.equals(SPId, GBId)) {
                    checkoutCommitObj(givenBranch, fileName);
                    String blobId = GBMap.get(fileName);
                    stage.add(fileName, blobId);
                    break;
                }

                // 2.
                // Any files that have been modified in the current branch
                // but not in the given branch
                if (Objects.equals(SPId, GBId) && !Objects.equals(SPId, CBId)) {
                    break;
                }

                // 3.1.
                // modified in both the current and given branch in the same way
                if (Objects.equals(GBId, CBId)) {
                    break;
                }
            }

            // 3.2.
            // removed from both the current and given branch
            // but a file of the same name is present in the working directory
            if (CBId == null && GBId == null) {
                break;
            }

            // 4.
            // not present at the split point
            // present only in the current branch
            if (SPId == null && CBId != null && GBId == null) {
                break;
            }

            // 5.
            // not present at the split point
            // present only in the given branch
            if (SPId == null && GBId != null && CBId == null) {
                checkoutCommitObj(givenBranch, fileName);
                String blobId = GBMap.get(fileName);
                stage.add(fileName, blobId);
                break;
            }

            // 6.
            // present at the split point
            // unmodified in the current branch
            // absent in the given branch
            if (SPId != null && stringEqual(SPId, CBId) && GBId == null) {
                rm(fileName);
                break;
            }

            // 7.
            // present at the split point
            // unmodified in the given branch
            // absent in the current branch
            if (CBId == null && SPId != null && stringEqual(SPId, GBId)) {
                break;
            }

            if (!stringEqual(GBId, CBId) && !stringEqual(GBId, SPId) && !stringEqual(CBId, SPId)) {
                // 8.
                // in conflict
                conflict = true;
                String begin = "<<<<<<< HEAD\n";
                byte[] CBContent = getContentFromId(CBId);
                String splitLine = "=======\n";
                byte[] GBContent = getContentFromId(GBId);
                String end = ">>>>>>>";

                writeContents(join(CWD, fileName), begin, CBContent, splitLine, GBContent, end);
                break;
            }
        }

        mergeCommit(branchName);

        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static byte[] getContentFromId(String blobId) {
        Blob blob = readObject(join(BLOBS_DIR, blobId), Blob.class);
        return blob.getContent();
    }
}
