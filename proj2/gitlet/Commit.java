package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.time.*;
import java.util.Date; // TODO: You'll likely use this in this class

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
    private String message;
    private Instant timestamp;
    // track id

    // Treating string as pointer, to avoid file too large or runtime to long
    private String parent;

    /* TODO: fill in the rest of this class. */

    /**
     * TODO: Construct
     * initialize: message, parent, timestamp
     */
    public Commit() {
        this.parent = null;
        this.message = "initial commit";
        this.timestamp = Instant.now();
    }
    public Commit(String message, String parent) {
        this.parent = parent;
        this.message = message;
        this.timestamp = Instant.ofEpochSecond(0);
    }
    public String getMessage() {
        return this.message;
    }
    public Instant getTimestamp() {
        return this.timestamp;
    }
    public String getParent() {
        return this.parent;
    }
}
