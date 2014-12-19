package com.darcimaher.simplestore;

public class CmdResponse {
    protected final String message;
    protected final String value;
    protected final String errMessage;

    public CmdResponse(String value, String message, String errMessage) {
        this.value = value;
        this.message = message;
        this.errMessage = errMessage;
    }
}
