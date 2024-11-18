package User;

import java.util.Date;

public class FlightxPassenger {
    private String ID; // Mã số chuyến bay
    private String startPoint; // Điểm khởi hành
    private String endPoint; // Điểm đến
    private Date dateStart; // Ngày khởi hành
    private String classSeat; // Hạng ghế
    private String typeTicket; // Loại vé
    private String status; // Trạng thái

    // Hàm khởi tạo
    public FlightxPassenger(String ID, String startPoint, String endPoint, Date dateStart, String classSeat, String typeTicket, String status) {
        this.ID = ID;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.dateStart = dateStart;
        this.classSeat = classSeat;
        this.typeTicket = typeTicket;
        this.status = status;
    }

    // Các phương thức getter
    public String getID() {
        return ID;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public String getClassSeat() {
        return classSeat;
    }

    public String getTypeTicket() {
        return typeTicket;
    }

    public String getStatus() {
        return status;
    }

    // Các phương thức setter
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public void setClassSeat(String classSeat) {
        this.classSeat = classSeat;
    }

    public void setTypeTicket(String typeTicket) {
        this.typeTicket = typeTicket;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
