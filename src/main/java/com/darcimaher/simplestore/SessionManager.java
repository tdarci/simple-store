package com.darcimaher.simplestore;

import java.util.Scanner;

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
                        processCommand(cmd);
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

        System.out.println("Processing command...: "  + cmd);

        switch (cmd.cmdType) {
            case SET:
                this.data.put(cmd.paramA, cmd.paramB);
                return new CmdResponse(null, "Set " + cmd.paramA + " to " + cmd.paramB, null);
            case GET:
                String val = this.data.get(cmd.paramA);
                return new CmdResponse(val, null, null);
            default:
                return null;

        }

    }

}
