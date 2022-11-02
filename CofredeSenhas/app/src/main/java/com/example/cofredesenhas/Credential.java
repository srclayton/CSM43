package com.example.cofredesenhas;

public class Credential {
    protected int id;
    protected String credentialName;
    protected String username;
    protected String password;
    protected int idFolder;
    protected String note;
    protected int idUser;
    protected String type;
    protected Boolean active;


    public Credential(int id, String credentialName, String username, String password, int idFolder, String note, int idUser, String type,Boolean active){
        this.id = id;
        this.credentialName = credentialName;
        this.username = username;
        this.password = password;
        this.idFolder = idFolder;
        this.note = note;
        this.idUser = idUser;
        this.type = type;
        this.active = active;
    }


    public Boolean getActive() {
        return active;
    }

    public int getIdFolder() {
        return idFolder;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public String getNote() {
        return note;
    }

    public int getId() {
        return id;
    }
}
