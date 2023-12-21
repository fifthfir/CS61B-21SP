package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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

    /* TODO: fill in the rest of this class. */

    /**
     * TODO: create other things we need in .gitlet directory
     * .gitlet
     *     initialCommit
     * TODO: if fail
     */
    public static void init() {
        GITLET_DIR.mkdir();
        Commit initialCommit = new Commit();
        File initialCommitFile = join(GITLET_DIR, "initialCommit");
        writeObject(initialCommitFile, initialCommit);
        // Branches: master, and point it to initial commit
        // what is UID?
    }
    public void commit() {
        // Read from computer [the head commit object] and [the staging area]

        // Clone the HEAD commit
        // Modify its message and timestamp according to user input
        // Use the staging area in order to modify the files tracked by the new commit

        // Write back new objects made or any modified objects created earlier
    }
}
