package com.example.cofredesenhas;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Folder implements Serializable {
    protected int id;
    protected String folderName;
    protected int idUser;
    protected int idTeam;
    protected String registerDate;
    protected ArrayList<Credential> credential = new ArrayList<>();
    protected ArrayList<CreditCard> creditCard = new ArrayList<>();


    public Folder (int id, String folderName, int idUser, int idTeam, String registerDate){
        this.id = id;
        this.idUser = idUser;
        this.idTeam = idTeam;
        this.folderName = folderName;
        this.registerDate = registerDate;
    }

    public ArrayList<Credential> getCredential() {
        return credential;
    }

    public ArrayList<CreditCard> getCreditCard() {
        return creditCard;
    }

    public void setCredential(Credential credential) {
        this.credential.add(credential);
    }
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard.add(creditCard);
    }


    public String getFolderName() {
        return folderName;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getId() {
        return id;
    }
}

