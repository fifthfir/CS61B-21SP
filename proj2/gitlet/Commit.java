package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.getId;

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
    private final ArrayList<String> parent;

    /* TODO: fill in the rest of this class. */

    // Construct
    public Commit() {
        this.committedMap = new HashMap<>();
        this.parent = new ArrayList<>();
        this.message = "initial commit";
        this.timestamp = Instant.ofEpochSecond(0);
    }
    public Commit(String message, HashMap<String, String> newMap, ArrayList<String> parents) {
        this.committedMap = newMap;
        this.parent = parents;
        this.message = message;
        this.timestamp = Instant.now();
    }
    public String getMessage() {
        return this.message;
    }
    public Instant getTimestamp() {
        return this.timestamp;
    }
    public ArrayList<String> getParent() {
        return this.parent;
    }
}
