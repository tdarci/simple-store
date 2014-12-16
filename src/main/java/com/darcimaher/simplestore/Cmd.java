package com.darcimaher.simplestore;

public class Cmd {

    protected final CmdType cmdType;
    protected final String paramA;
    protected final String paramB;

    public Cmd(CmdType cmdType, String paramA, String paramB) {
        this.cmdType = cmdType;
        this.paramA = paramA;
        this.paramB = paramB;
    }

    @Override
    public String toString() {
        return cmdType.name() + " parameter_A: " + paramA + " parameter_B: " + paramB;
    }
}
