package com.example.cofredesenhas;

public class CreditCard {
    protected int id;
    protected int idUser;
    protected String surname;
    protected String cardholerName;
    protected long  cardNumber;
    protected String expirationDate;
    protected String cvcCode;
    protected int idFolder;
    protected String type;
    Boolean active;


    public CreditCard( int id, int idUser, String surname, String cardholerName, long  cardNumber, String expirationDate, String cvcCode, int idFolder, String type,Boolean active){
        this.id = id;
        this.idUser = idUser;
        this.surname = surname;
        this.cardholerName = cardholerName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvcCode = cvcCode;
        this.idFolder = idFolder;
        this.type =type;
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public int getIdFolder() {
        return idFolder;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public String getSurname() {
        return surname;
    }

    public int getId() {
        return id;
    }

    public String getCardholerName() {
        return cardholerName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvcCode() {
        return cvcCode;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
