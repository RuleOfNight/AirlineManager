package User;

import java.sql.Date;

public class Flight {
    private String ID = ""; // Mã số chuyến bay
    private String startPoint = ""; // Điểm khởi hành
    private String endPoint = ""; // Điểm đến
    private Date dateStart = null; // Ngày khởi hành
    private int numberOfSeat = 0; // Số lượng ghế
    private String status = ""; // Trạng thái
    private int price = 0; // Giá vé

    // Các hàm khởi tạo
    public Flight() {
        // TODO Auto-generated constructor stub
    }

    // Hàm khởi tạo với đầy đủ thông tin về chuyến bay
    public Flight(String ID, String startPoint, String endPoint, Date dateStart, int numberOfSeat, String status, int price) {
        this.ID = ID;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.dateStart = dateStart;
        this.setNumberOfSeat(numberOfSeat);
        this.status = status;
        this.price = price;
    }

    // Các hàm getter và setter
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
