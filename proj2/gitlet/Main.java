package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

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
    public static void main(String[] args) {
        if (args.length == 0) {
            exitWString("Please enter a command.");
        }
        String firstArg = args[0];

        // TODO: all of other commands

        switch(firstArg) {
            case "init":
                checkArgsNums(args, 1);
                Repository.init();
//                Repository.checkMap();
                break;

            case "add":
                checkArgsNums(args, 2);
                Repository.add(args[1]);
//                Repository.checkMap();
                break;

            case "commit":
                if (args.length == 1) {
                    exitWString("Please enter a commit message.");
                }
                checkArgsNums(args, 2);
                Repository.commit(args[1]);
//                Repository.checkMap();
                break;

            case "rm":
                checkArgsNums(args, 2);
                Repository.rm(args[1]);
//                Repository.checkMap();
                break;

            case "log":
                checkArgsNums(args, 1);
                Repository.log();
                break;

            case "global-log":
                checkArgsNums(args, 1);
                Repository.gLog();
                break;

            case "find":
                checkArgsNums(args, 2);
                Repository.find(args[1]);
                break;

            case "status": // TODO
                checkArgsNums(args, 1);
                Repository.status();
                break;

            case "checkout":
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                } else if (args.length == 3 && Objects.equals(args[1], "--")) {
                    Repository.checkoutFile(args[2]);
                } else if (args.length == 4 && Objects.equals(args[2], "--")) {
                    Repository.checkoutCommit(args[1], args[3]);
                } else {
                    exitWString("Incorrect operands.");
                }
//                Repository.checkMap();
                break;

            case "branch":
                checkArgsNums(args, 2);
                Repository.branch(args[1]);
                break;

            case "rm-branch":
                checkArgsNums(args, 2);
                Repository.rmBranch(args[1]);
                break;

            case "reset":
                checkArgsNums(args, 2);
                Repository.reset(args[1]);
//                Repository.checkMap();
                break;

            case "merge":
                // TODO: handle the `merge [branch name]` command
                checkArgsNums(args, 2);
                Repository.merge(args[1]);
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
