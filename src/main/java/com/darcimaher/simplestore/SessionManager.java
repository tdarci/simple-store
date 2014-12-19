package com.darcimaher.simplestore;

import java.util.Scanner;
import java.util.Set;

public class SessionManager {

    protected final SearchableStringMap data;

    public SessionManager() {
        data = new SearchableStringMap();
    }

    public void run() {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("SIMPLE STORE: ");
                String inputLine = scanner.nextLine();
                if (inputLine.trim().toLowerCase().equals("end")) {
                    System.out.println("GOODBYE.");
                    break;
                } else {
                    try {
                        Cmd cmd = CmdType.parseInput(inputLine);
                        CmdResponse rsp = processCommand(cmd);
                        if (rsp.errMessage != null) {
                            System.out.println("  * ERROR: " + rsp.errMessage);
                        } else {
                            System.out.println("  * SUCCESS: " + rsp.message);
                        }
                        if (rsp.value != null) {
                            System.out.println("  * GOT VALUE: " + rsp.value);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        }   catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private CmdResponse processCommand( Cmd cmd) {

//        System.out.println("Processing command...: "  + cmd);

        switch (cmd.cmdType) {
            case SET:
                this.data.put(cmd.paramA, cmd.paramB);
                return new CmdResponse(null, "Set " + cmd.paramA + " to " + cmd.paramB, null);
            case GET:
                String val = this.data.get(cmd.paramA);
                String msg = null;
                if (val != null) {
                    msg = "Found value '" + val + "' for key " + cmd.paramA;
                } else {
                    msg = "No value found for key " + cmd.paramA;
                }
                return new CmdResponse(val, msg, null);
            case UNSET:
                this.data.remove(cmd.paramA);
                return new CmdResponse(null, "Removed key: " + cmd.paramA, null);
            case NUMEQUALTO:
                Set<String> matchingKeys = this.data.keysWithValue(cmd.paramA);
                long matchCount = matchingKeys.size();
                return new CmdResponse(matchCount + "", "Found " + matchCount + " keys with value '" + cmd.paramA + "'", null);
            default:
                return null;

        }

    }

}
