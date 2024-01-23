package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import static gitlet.Repository.BLOBS_DIR;
import static gitlet.Repository.COMMITS_DIR;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Ruotian Zhang
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    /**
     * How to save data in commit?
     */
    private final HashMap<String, String> committedMap;
    private final Instant timestamp;

    // Treating string as pointer, to avoid file too large or runtime too long
    private final String id;
    private final String parent;

    /* TODO: fill in the rest of this class. */

    // Construct
    public Commit() {
        this.committedMap = new HashMap<>();
        this.parent = "";
        this.message = "initial commit";
        this.timestamp = Instant.ofEpochSecond(0);
        this.id = getId(this);
    }
    public Commit(String message, HashMap<String, String> newMap, String parent) {
        this.committedMap = newMap;
        this.parent = parent;
        this.message = message;
        this.timestamp = Instant.now();
        this.id = getId(this);
    }

    public void save() {
        File objFile = join(COMMITS_DIR, this.id);
        writeObject(objFile, this);
    }
    public String getMessage() {
        return this.message;
    }
    public String getTimestampFormatted() {
        Date tsd = Date.from(this.timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        return sdf.format(tsd);
    }
    public String getParent() {
        return this.parent;
    }

    public HashMap<String, String> getMap() {
        return this.committedMap;
    }
    public String getCommitId() {
        return this.id;
    }

//    public static Commit findAncestor(Commit branch1, Commit branch2) {
//        Set<Commit> visitedByBranch1 = new HashSet<>();
//        Set<Commit> visitedByBranch2 = new HashSet<>();
//
//        dfs(branch1, visitedByBranch1);
//        dfs(branch2, visitedByBranch2);
//
//        Set<Commit> commonCommits = new HashSet<>(visitedByBranch1);
//        commonCommits.retainAll(visitedByBranch2);
//
//        if (!commonCommits.isEmpty()) {
//            return commonCommits.iterator().next();
//        }
//
//        return null;
//    }
//
//    private static void dfs(Commit commit, Set<Commit> visitedCommits) {
//        if (visitedCommits.contains(commit)) {
//            return;
//        }
//        visitedCommits.add(commit);
//
//        if (!Objects.equals(commit.getParent(), "")) {
//            dfs(getCommitFromName(COMMITS_DIR, commit.getParent()), visitedCommits);
//        }
//    }
}
