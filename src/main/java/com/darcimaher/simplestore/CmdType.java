package com.darcimaher.simplestore;

public enum CmdType {

    SET (2),
    UNSET (1),
    NUMEQUALTO (1),
    BEGIN (0),
    COMMIT (0),
    LOAD (1);

    protected final int paramCount;

    CmdType(int paramCount) {
        this.paramCount = paramCount;
    }

    /**
     * Parses the users input into a command that can be processed.
     *
     * @param input user's input
     * @return Cmd object representation of user's input string.
     * @throws IllegalArgumentException
     */
    static public Cmd parseInput(String input) throws IllegalArgumentException {
        if (input == null || input.isEmpty()){
            throw new IllegalArgumentException("Empty input.") ;
        }
        String[] vals = input.trim().split("\\s+");
        if (vals.length < 1) {
            throw new IllegalArgumentException("Empty input.") ;
        }
        String cmdName = vals[0].toUpperCase();

        CmdType t;
        try {
            t = CmdType.valueOf(cmdName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown command: " + cmdName);
        }

        if ( vals.length != t.paramCount + 1) {
            throw new IllegalArgumentException("Wrong number of parameters for command " + t.name() + ". Expecting " + t.paramCount + " parameters.");
        }
        String paramA = null;
        String paramB = null;
        if (t.paramCount > 0) {
            paramA = vals[1];
        }
        if (t.paramCount > 1) {
            paramB = vals[2];
        }
        return new Cmd(t, paramA, paramB);
    }

}
