package com.example.cofredesenhas;

public class CreditCard {
    protected int id;
    protected int idUser;
    protected String surname;
    protected String cardholerName;
    protected long  cardNumber;
    protected String expirationDate;
    protected String cvcCode;
    protected String company;
    protected int idFolder;
    protected String note;
    protected String type;
    Boolean active;


    public CreditCard( int id, int idUser, String surname, String cardholerName, long  cardNumber, String expirationDate, String cvcCode, String company, int idFolder, String note, String type,Boolean active){
        this.id = id;
        this.idUser = idUser;
        this.surname = surname;
        this.cardholerName = cardholerName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvcCode = cvcCode;
        this.company = company;
        this.idFolder = idFolder;
        this.note = note;
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
}
