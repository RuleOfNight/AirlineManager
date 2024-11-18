package AirlineManager;

import java.sql.Date;

public class Flight {
    private String ID = "";
    private String startPoint = "";
    private String endPoint = "";
    private Date dateStart = null;
    private int numberOfSeat = 0;
    private String status = "";
    private int price = 0;


    public Flight() {
    }

    public Flight(String ID, String startPoint, String endPoint, Date dateStart, int numberOfSeat, String status, int price) {

        this.ID = ID;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.dateStart = dateStart;
        this.setNumberOfSeat(numberOfSeat);
        this.status = status;
        this.price = price;
    }


    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberOfSeat() {
        return numberOfSeat;
    }

    public void setNumberOfSeat(int numberOfSeat) {
        this.numberOfSeat = numberOfSeat;
    }
}
