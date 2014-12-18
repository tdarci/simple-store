package com.darcimaher.simplestore;

import java.util.Scanner;

public class SessionManager {

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

    private void processCommand( Cmd cmd) {
        System.out.println("Processing command...: "  + cmd);
    }

}
