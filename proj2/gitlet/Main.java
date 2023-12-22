package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.exitWString;
import static java.lang.System.exit;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Ruotian Zhang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    private static void checkArgsNums(String[] args, int targetNum) {
        if (args.length != targetNum) {
            exitWString("Incorrect operands.");
        }
    }
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if (args.length == 0) {
            exitWString("Please enter a command.");
        }
        String firstArg = args[0];
        // TODO: all of other commands
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                checkArgsNums(args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                checkArgsNums(args, 2);
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                // TODO: handle the `commit [message]` command
                checkArgsNums(args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                // TODO: handle the `rm [filename]` command
                break;
            case "log":
                // TODO: handle the `rm [filename]` command
                break;
            case "global-log":
                // TODO: handle the `global-log` command
                break;
            case "find":
                // TODO: handle the `find [commit message]` command
                break;
            case "status":
                // TODO: handle the `status` command
                break;
            case "checkout":
                // TODO: handle the `checkout` command
                break;
            case "branch":
                // TODO: handle the `branch [branch name]` command
                break;
            case "rm-branch":
                // TODO: handle the `rm-branch [branch name]` command
                break;
            case "reset":
                // TODO: handle the `reset [commit id]` command
                break;
            case "merge":
                // TODO: handle the `merge [branch name]` command
                break;
            case "add-remote":
                // TODO: handle the `add-remote [remote name]` command
                break;
            case "rm-remote":
                // TODO: handle the `rm-remote [rm-remote name]` command
                break;
            case "push":
                // TODO: handle the `push [remote name] [remote branch name]` command
                break;
            case "fetch":
                // TODO: handle the `fetch [remote name] [remote branch name]` command
                break;
            case "pull":
                // TODO: handle the `pull [remote name] [remote branch name]` command
                break;
            default:
                exitWString("No command with that name exists.");
        }
    }
}
