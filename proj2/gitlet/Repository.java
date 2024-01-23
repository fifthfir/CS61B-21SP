package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static gitlet.Stage.*;
import static gitlet.Utils.*;
import static gitlet.Utils.restrictedDelete;
import static gitlet.Utils.writeObject;


/** Represents a gitlet repository.
 *
 *  @author Ruotian Zhang
 */
public class Repository {

    /**
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

    private static final ArrayList<String> IGNORE_FILES =
        new ArrayList<>(Arrays.asList("Makefile", "gitlet-design.md", "pom.xml"));

    private static String getHeadBranchName() {
        return readObject(HEAD, String.class);
    }

    public static Commit getHEADCommit() {
        return readObject(join(BRANCH_DIR, getHeadBranchName()), Commit.class);
    }


    public static void init() {

        boolean initialExists = OBJECTS_DIR.exists();

        if (!initialExists) {
            GITLET_DIR.mkdir();
            OBJECTS_DIR.mkdir();
            COMMITS_DIR.mkdir();
            BLOBS_DIR.mkdir();
            BRANCH_DIR.mkdir();
            try {
                MASTER.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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
        // Check the file exists
        File thisFile = join(CWD, fileName);

        if (!thisFile.exists() || !STAGE.exists()) {
            exitWString("File does not exist.");
        }

        // file exists, so let stage deal with it
        Stage stage;
        stage = readStage();

        stage.add(fileName);
        stage.saveStageFile();
    }

    /**
     * @param message   to make a commit with the given message
     */
    public static void commit(String message) {
        Stage stage = readStage();

        // Read from computer [the head commit object] and [the staging area]
        HashMap<String, String> stagingMap = stage.getStagingMap();
        ArrayList<String> rmMap = stage.getRmMap();

        if (stagingMap.isEmpty() && rmMap.isEmpty()) {
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

        stage.clearMap();
        stage.saveStageFile();
    }

    /**
     * Print out the log of commits in current branch
     */
    public static void log() {
        Commit curCommit = getHEADCommit();
        printCommitFrom(curCommit.getCommitId());
    }


    /**
     * Print out the log of commits in all branches
     */
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
     * Go through every commit to check its message
     * [stage]:
     * [current parent (last commit) id -> curCommitFile
     * -> curCommitObj -> ?curCommitObj.getMessage() == message;]
     * -> curCommitObj.getParent id -> curCommitParentFile -> ...
     *
     * @param message   the message to find
     */
    public static void find(String message) {
        Stage stage = readStage();
        assert stage != null;

        String curCommitId = getHEADCommit().getCommitId();

        ArrayList<String> foundList = new ArrayList<>();
        findCommits(curCommitId, message, foundList);
        if (!foundList.isEmpty()) {
            for (String commitId : foundList) {
                System.out.println(commitId);
            }
        } else {
            System.out.println("Found no commit with that message.");
        }
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


    /**
     * @param fileName  the file to remove
     */
    public static void rm(String fileName) {
        Stage stage = readStage();
        Commit headCommit = getHEADCommit();

        boolean fileStaged = stage.staged(fileName);
        String fileCurCommitted = headCommit.getMap().get(fileName);


        if (!fileStaged && fileCurCommitted == null) {
            exitWString("No reason to remove the file.");
        }

        if (fileStaged) {
//            stage.rmStaging(fileName);
            stage.saveStageFile();
        }

        if (fileCurCommitted != null) {
//            stage.remove(fileName);
            restrictedDelete(join(CWD, fileName));
        }

        stage.saveStageFile();
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
     *
     * @param fileName  the file we want to set back to the version of the current commit
     */
    public static void checkoutFile(String fileName) {
        Commit headCommit = getHEADCommit();
        checkoutCommitObj(headCommit, fileName);
    }


    private static void checkoutCommitObj(Commit commit, String fileName) {
        HashMap<String, String> comMap = commit.getMap();
        writeAFileFromMap(comMap, fileName);
    }


    /**
     * @param fileName      Which file we want to recover from the current commit
     */
    public static void checkoutCommit(String commitId, String fileName) {
        checkCommitId(commitId);

        HashMap<String, String> comMap = getMapFromCommitId(commitId);
        writeAFileFromMap(comMap, fileName);
    }


    /**
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist.
     *
     * @param branchName    The branch we want to change to
     */
    public static void checkoutBranch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);
        Stage stage = readStage();

        // Checks
        if (!branchFile.exists()) {
            exitWString("No such branch exists.");
        }

        if (Objects.equals(getHeadBranchName(), branchName)) {
            exitWString("No need to checkout the current branch.");
        }

        Commit brCommit = readObject(branchFile, Commit.class);
        ifAllTracked();
        removeCurCommit();

        // then write all files from the given commit id
        HashMap<String, String> newMap = brCommit.getMap();
        writeAllFilesFromMap(newMap);

        if (getHEADCommit() != brCommit) {
            stage.clearMap();
        }

        // last, write this new branch file into HEAD
        writeObject(HEAD, branchName);
        stage.saveStageFile();

    }

    private static void writeAFileFromMap(HashMap<String, String> map, String fileName) {
        checkFileCommitted(map, fileName);
        String blobId = map.get(fileName);
        writeFileFromFB(fileName, blobId);
    }

    private static void writeAllFilesFromMap(HashMap<String, String> map) {
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

    public static void reset(String commitId) {
        checkCommitId(commitId);

        Stage stage = readStage();
        assert stage != null;

        ifAllTracked();
        removeCurCommit();

        HashMap<String, String> committedFileMap = getMapFromCommitId(commitId);
        writeAllFilesFromMap(committedFileMap);
        stage.clearMap();

        String curBranchName = readObject(HEAD, String.class);
        writeObject(join(BRANCH_DIR, curBranchName), getCommitFromName(COMMITS_DIR, commitId));

        stage.saveStageFile();
    }

    private static void ifAllTracked() {
        Commit curCommit = getHEADCommit();
        HashMap<String, String> trackedMap = curCommit.getMap();

        List<String> fileNames = plainFilenamesIn(CWD);
        for (String fileName : fileNames) {
            if (trackedMap.get(fileName) == null && !IGNORE_FILES.contains(fileName)) {
                exitWString("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            }
        }
    }

    private static void removeCurCommit() {
        Commit curCommit = getHEADCommit();
        HashMap<String, String> mapToDelete = curCommit.getMap();
        for (Map.Entry<String, String> entry : mapToDelete.entrySet()) {
            String fileName = entry.getKey();
            restrictedDelete(join(CWD, fileName));
        }
    }

    private static void writeFileFromFB(String fileName, String blobId) {
        File file = join(CWD, fileName);

        File blobFile = join(BLOBS_DIR, blobId);
        Blob blob = readObject(blobFile, Blob.class);

        if (file.exists()) {
            restrictedDelete(file);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        byte[] content = blob.getContent();
        writeContents(file, content);
    }

    /**
     *
     * @param branchName    The new branch's name
     */
    public static void branch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);
        if (branchFile.exists()) {
            exitWString("A branch with that name already exists.");
        }

        try {
            branchFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeObject(branchFile, getHEADCommit());
//        writeObject(HEAD, branchName);
    }


    public static void status() {
        if (!GITLET_DIR.exists()) {
            exitWString("Not in an initialized Gitlet directory.");
        }

        printStatusTitle("Branches");

        for (String branchName : plainFilenamesIn(BRANCH_DIR)) {
            if (Objects.equals(getHeadBranchName(), branchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }

        System.out.println();
        printStatusTitle("Staged Files");

        Stage stage = readStage();
        HashMap<String, String> stagingMap = stage.getStagingMap();

        for (Map.Entry<String, String> entry : stagingMap.entrySet()) {
            System.out.println(entry.getKey());
        }

        System.out.println();
        printStatusTitle("Removed Files");

        for (String fileName : stage.getRmMap()) {
            System.out.println(fileName);
        }

        System.out.println();
        printStatusTitle("Modifications Not Staged For Commit");

        for (Map.Entry<String, String> entry : stagingMap.entrySet()) {
            String fileName = entry.getKey();
            File thisFile = join(CWD, fileName);
            if (!thisFile.exists()) {  // 3
                System.out.println(fileName);
            } else {
                String curBlobId = new Blob(thisFile).getBlobId();
                if (!entry.getValue().equals(curBlobId)) {  // 2
                    System.out.println(fileName);
                }
            }
        }

        HashMap<String, String> committedMap = getHEADCommit().getMap();
        for (Map.Entry<String, String> entry : committedMap.entrySet()) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();

            File thisFile = join(CWD, fileName);

            if (!thisFile.exists() && !stage.getRmMap().contains(fileName)) {  // 4
                System.out.println(fileName);
            } else if (thisFile.exists()) {
                String curBlobId = new Blob(thisFile).getBlobId();
                if (!blobId.equals(curBlobId)
                    && !blobId.equals(stagingMap.get(fileName))) {  // 1
                    System.out.println(fileName);
                }
            }
        }

        System.out.println();
        printStatusTitle("Untracked Files");

        for (String fileName : plainFilenamesIn(CWD)) {
            if (IGNORE_FILES.contains(fileName)) {
                continue;
            }
            if (!stagingMap.containsKey(fileName) && !committedMap.containsKey(fileName)) {
                System.out.println(fileName);
            }
        }
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

        String curBranchName = getHeadBranchName();
        if (Objects.equals(branchName, curBranchName)) {
            exitWString("Cannot remove the current branch.");
        }

        try {
            Files.delete(branchFile.toPath());  // dangerous
        } catch (IOException e) {
            System.err.println("Unable to delete the file: " + e.getMessage());
        }
    }


    /**
     * Merges files from the given branch into the current branch.
     *
     * @param branchName    Branch to be merged into current branch
     */
    public static void merge(String branchName) {
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

        Commit splitPoint = findSplitPoint(getHeadBranchName(), branchName);

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
        HashMap<String, String> cbMap = curBranch.getMap();
        assert splitPoint != null;
        HashMap<String, String> spMap = splitPoint.getMap();
        HashMap<String, String> gbMap = givenBranch.getMap();

        List<String> fileNames = plainFilenamesIn(CWD);

        assert fileNames != null;
        for (String fileName : fileNames) {
            String cbId = cbMap.get(fileName);
            String spId = spMap.get(fileName);
            String gbId = gbMap.get(fileName);

            if (cbId == null) {
                exitWString(
                    "There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
            }

            // no one absent
            if (spId != null && cbId != null && gbId != null) {

                // 1.
                // modified in the given branch since the split point
                // but not modified in the current branch

                if (Objects.equals(spId, cbId) && !Objects.equals(spId, gbId)) {
                    checkoutCommitObj(givenBranch, fileName);
                    String blobId = gbMap.get(fileName);
                    stage.add(fileName, blobId);
                    break;
                }

                // 2.
                // Any files that have been modified in the current branch
                // but not in the given branch
                if (Objects.equals(spId, gbId) && !Objects.equals(spId, cbId)) {
                    break;
                }

                // 3.1.
                // modified in both the current and given branch in the same way
                if (Objects.equals(gbId, cbId)) {
                    break;
                }
            }

            // 3.2.
            // removed from both the current and given branch
            // but a file of the same name is present in the working directory
            if (cbId == null && gbId == null) {
                break;
            }

            // 4.
            // not present at the split point
            // present only in the current branch
            if (spId == null && cbId != null && gbId == null) {
                break;
            }

            // 5.
            // not present at the split point
            // present only in the given branch
            if (spId == null && gbId != null && cbId == null) {
                checkoutCommitObj(givenBranch, fileName);
                String blobId = gbMap.get(fileName);
                stage.add(fileName, blobId);
                break;
            }

            // 6.
            // present at the split point
            // unmodified in the current branch
            // absent in the given branch
            if (spId != null && stringEqual(spId, cbId) && gbId == null) {
                rm(fileName);
                break;
            }

            // 7.
            // present at the split point
            // unmodified in the given branch
            // absent in the current branch
            if (cbId == null && spId != null && stringEqual(spId, gbId)) {
                break;
            }

            if (!stringEqual(gbId, cbId) && !stringEqual(gbId, spId) && !stringEqual(cbId, spId)) {
                // 8.
                // in conflict
                conflict = true;
                String begin = "<<<<<<< HEAD\n";
                byte[] cbContent = getContentFromId(cbId);
                String splitLine = "=======\n";
                byte[] gbContent = getContentFromId(gbId);
                String end = ">>>>>>>";

                writeContents(join(CWD, fileName), begin, cbContent, splitLine, gbContent, end);
                break;
            }
        }

        mergeCommit(branchName);

        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }


    private static void mergeCommit(String givenBranchName) {
        String curBranchName = readObject(HEAD, String.class);
        String msg = "Merged " + givenBranchName + " into " + curBranchName;

        commit(msg);
    }


    private static Commit findSplitPoint(String curBranch, String branch2) {
        Commit commit1 = getHEADCommit();
        Commit commit2 = readObject(join(BRANCH_DIR, branch2), Commit.class);


        while (true) {
            if (Objects.equals(commit1.getParent(), commit2.getCommitId())) {
                return commit2;
            } else if (Objects.equals(commit2.getParent(), commit1.getCommitId())) {
                return commit1;
            } else {
                commit1 = getCommitFromName(COMMITS_DIR, commit1.getParent());
                commit2 = getCommitFromName(COMMITS_DIR, commit2.getParent());
            }
        }
    }


    private static byte[] getContentFromId(String blobId) {
        Blob blob = readObject(join(BLOBS_DIR, blobId), Blob.class);
        return blob.getContent();
    }
}
