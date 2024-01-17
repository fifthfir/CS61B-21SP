package gitlet;

import org.junit.Test;

import java.util.HashMap;

public class TestCommit {
    @Test
    public void findAncestorTest() {
        HashMap<String, String> defaultMap = new HashMap<>();
        String defaultMsg = "Hello";

        Commit commit1 = new Commit(defaultMsg, defaultMap, "");
        Commit commit2 = new Commit(defaultMsg, defaultMap, "");
        Commit commit3 = new Commit(defaultMsg, defaultMap, "");
        Commit commit4 = new Commit(defaultMsg, defaultMap, "");
        Commit commit5 = new Commit(defaultMsg, defaultMap, "");
        Commit commit6 = new Commit(defaultMsg, defaultMap, "");
    }
}
