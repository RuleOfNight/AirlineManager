package User;

import java.sql.Date;

public class Passenger {
    private String classSeat; 
    private String typeTicket;
    private String fullName; 
    private Date birthDate; 
    private String phoneNumber; 
    private String email; 
    private String ID;
    
    // Constructor
    public Passenger(String classSeat, String typeTicket, String fullName, Date birthDate, String phoneNumber, String email, String ID) {
        this.classSeat = classSeat;
        this.typeTicket = typeTicket;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ID = ID;
    }
    
    // Getter and setter methods
    public String getClassSeat() {
        return classSeat;
    }
    
    public void setClassSeat(String classSeat) {
        this.classSeat = classSeat;
    }
    
    public String getTypeTicket() {
        return typeTicket;
    }
    
    public void setTypeTicket(String typeTicket) {
        this.typeTicket = typeTicket;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getID() {
        return ID;
    }
    
    public void setID(String iD) {
        ID = iD;
    } 
}
