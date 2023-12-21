package gitlet;

import java.io.File;

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
            System.out.println("Incorrect operands.");
            exit(0);
        }
    }
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                checkArgsNums(args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                checkArgsNums(args, 2);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                // TODO: handle the `commit [message]` command
                checkArgsNums(args, 2);
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
                System.out.println("No command with that name exists.");
                exit(0);
        }
    }
}
