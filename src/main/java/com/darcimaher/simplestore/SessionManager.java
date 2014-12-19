package com.darcimaher.simplestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SessionManager {

    protected final SearchableStringMap data;
    protected final List<SearchableStringMap> openTransactionsData;

    public SessionManager() {

        data = new SearchableStringMap();
        openTransactionsData = new ArrayList<SearchableStringMap>();
    }

    public void run() {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {

                System.out.print("SIMPLE STORE: ");

                String inputLine = scanner.nextLine();
                if (inputLine == null) {
                    continue;
                }

                inputLine = inputLine.trim();
                if (inputLine.isEmpty()) {
                    continue;
                }

                if (inputLine.equalsIgnoreCase("end") || inputLine.equalsIgnoreCase("quit") || inputLine.equalsIgnoreCase("exit") || inputLine.equalsIgnoreCase("bye")) {
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

        switch (cmd.cmdType) {
            case SET:
                this.data.put(cmd.paramA, cmd.paramB);
                return new CmdResponse(null, "Set " + cmd.paramA + " to " + cmd.paramB, null);
            case GET:
                String val = this.data.get(cmd.paramA);
                String msg;
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
