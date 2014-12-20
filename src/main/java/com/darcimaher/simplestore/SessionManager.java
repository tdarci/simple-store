package com.darcimaher.simplestore;

import java.util.Scanner;

public class SessionManager {

    protected final TransactionalDataStore data;

    public SessionManager() {
        data = new TransactionalDataStore();
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
                    // let's make sure we leave things clean.
                    data.commitAllTransactions();
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

        if (cmd.cmdType.getRequiresTransaction() && ! data.inTransaction()){
            return new CmdResponse(null, null, "No transaction. This command requires a transaction.");
        }

        switch (cmd.cmdType) {
            case SET:
                data.set(cmd.paramA, cmd.paramB);
                return new CmdResponse(null, "Set " + cmd.paramA + " to " + cmd.paramB, null);
            case GET:
                String val = data.get(cmd.paramA);
                String msg;
                if (val != null) {
                    msg = "Found value '" + val + "' for key " + cmd.paramA;
                } else {
                    msg = "No value found for key " + cmd.paramA;
                }
                return new CmdResponse(val, msg, null);
            case UNSET:
                data.unset(cmd.paramA);
                return new CmdResponse(null, "Removed key: " + cmd.paramA, null);
            case NUMEQUALTO:
                long matchCount = data.countWithValue(cmd.paramA);
                return new CmdResponse(matchCount + "", "Found " + matchCount + " keys with value '" + cmd.paramA + "'", null);
            case BEGIN:
                data.beginTransaction();
                return new CmdResponse(null, "Transaction begun.", null);
            case ROLLBACK:
                data.rollbackOneTransaction();
                return new CmdResponse(null, "Transaction rolled back.", null);
            case COMMIT:
                data.commitAllTransactions();
                return new CmdResponse(null, "Data saved.", null);
            default:
                return null;

        }

    }

}
