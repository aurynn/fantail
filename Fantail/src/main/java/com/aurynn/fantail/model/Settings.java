package com.aurynn.fantail.model;

/**
 * Created by aurynn on 26/10/13.
 */
public class Settings {

    private String columnId = null;
    private String clientid = null;

    public void setClientID(String clientID) {
        this.clientid = clientID;
    }

    public String getClientId() {
        return clientid;
    }

    public String getColumnId() { return columnId; }
    public void setColumnId(String columnId) { this.columnId = columnId; }
}
