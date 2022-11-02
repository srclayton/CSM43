package com.example.cofredesenhas;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    protected Boolean su;
    protected int userId;
    protected String name;
    protected String email;
    protected int  team;
    protected String registerDate;
    //protected Folder[] folder;
    protected ArrayList<Folder> folder = new ArrayList<>();

    public User (int userId,Boolean  su,String  name,String  email,int  team,String  registerDate){
        this.userId = userId;
        this.su = su;
        this.name = name;
        this.email = email;
        this.team = team;
        this.registerDate = registerDate;
    }
    public Boolean getSu(){ return this.su;}
    public int getUserId(){return this.userId;}
    public String getName(){ return this.name;}
    public String getEmail(){return this.email;}
    public int getTeam(){ return this.team;}
    public String getRegisterDate(){return this.registerDate;}
    public ArrayList<Folder>  getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder.add(folder);
    }

    public void setName(String name){this.name = name;}

}
