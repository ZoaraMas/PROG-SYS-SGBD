package ProgSys;

import java.io.Serializable;

public class MyPacket implements Serializable{
    private String message;

    public MyPacket(String message) {
        setMessage(message);    
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
