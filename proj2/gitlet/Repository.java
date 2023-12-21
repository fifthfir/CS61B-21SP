package gitlet;

import java.io.File;
import java.nio.file.Files;

import static gitlet.Utils.*;
import static java.lang.System.exit;

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
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");

    /* TODO: fill in the rest of this class. */

    /**
     * TODO: branch
     */
    public static void init() {
        boolean initialExists = COMMIT_DIR.exists();
        
        if (!initialExists) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            Commit initialCommit = new Commit();
            String id = sha1(serialize(initialCommit));

            File initialCommitFile = join(COMMIT_DIR, id);
            writeObject(initialCommitFile, initialCommit);

        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            exit(0);
        }
    }
    public static void add() {

    }
    public static void commit() {
        // Read from computer [the head commit object] and [the staging area]

        // Clone the HEAD commit
        // Modify its message and timestamp according to user input
        // Use the staging area in order to modify the files tracked by the new commit

        // Write back new objects made or any modified objects created earlier
    }
}
