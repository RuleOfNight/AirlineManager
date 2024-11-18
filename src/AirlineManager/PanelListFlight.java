package AirlineManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelListFlight extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableListFlight;
    private JTextField textFieldFlightCancellation;
    private String flightCancelled = ""; // Biến lưu ID chuyến bay muốn hủy
    private DefaultTableModel tableModel;

    /**
     * Khởi tạo panel.
     */
    public PanelListFlight() {
        setBounds(0, 0, 700, 600);
        setLayout(null);
        
        // Label tiêu đề
        JLabel labelListFlight = new JLabel("Danh sách chuyến bay");
        labelListFlight.setFont(new Font("Arial", Font.BOLD, 22));
        labelListFlight.setBounds(200, 11, 250, 49);
        add(labelListFlight);
        
        // Tạo mô hình bảng
        String[] columnNames = {"ID", "Điểm đi", "Điểm đến", "Ngày khởi hành","Số lượng ghế", "Trạng thái", "Giá vé"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableListFlight = new JTable(tableModel);
        
        // Thêm thanh cuộn cho bảng
        JScrollPane scrollPaneListFlight = new JScrollPane(tableListFlight);
        scrollPaneListFlight.setBounds(10, 69, 680, 443);
        add(scrollPaneListFlight);
        
        // Label nhập ID chuyến bay muốn hủy
        JLabel labelFlightCancellation = new JLabel("Nhập chuyến muốn hủy");
        labelFlightCancellation.setFont(new Font("Arial", Font.BOLD, 20));
        labelFlightCancellation.setBounds(10, 531, 233, 58);
        add(labelFlightCancellation);
        
        // Trường nhập ID chuyến bay
        textFieldFlightCancellation = new JTextField();
        textFieldFlightCancellation.setBounds(265, 545, 197, 36);
        add(textFieldFlightCancellation);
        textFieldFlightCancellation.setColumns(10);
        flightCancelled = textFieldFlightCancellation.getText(); // Lấy ID chuyến bay từ trường nhập liệu
        
        // Nút hủy chuyến bay
        JButton buttonFlightCancellation = new JButton("Hủy chuyến");
        buttonFlightCancellation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelFlight(); // Gọi phương thức hủy chuyến
                showAllFlight(); // Hiển thị lại danh sách chuyến bay sau khi hủy
                textFieldFlightCancellation.setText(""); // Xóa trường nhập liệu
            }
        });
        buttonFlightCancellation.setFont(new Font("Arial", Font.BOLD, 20));
        buttonFlightCancellation.setBounds(509, 545, 145, 36);
        add(buttonFlightCancellation);
        
        // Bắt sự kiện khi chọn một dòng trong bảng
        tableListFlight.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tableListFlight.getSelectedRow(); 
                if (selectedRow != -1 ) {
                    String idFlight = tableListFlight.getValueAt(selectedRow, 0).toString(); 
                    textFieldFlightCancellation.setText(idFlight); // Đặt ID chuyến bay được chọn vào trường nhập liệu
                }
            }
        });
    }

    // Getter cho biến flightCancelled
    public String getFlightCancelled() {
        return flightCancelled;
    }

    // Setter cho biến flightCancelled
    public void setFlightCancelled(String flightCancelled) {
        this.flightCancelled = flightCancelled;
    }
    
    // Phương thức để hủy chuyến bay
    public void cancelFlight() {
        String iDFlight = textFieldFlightCancellation.getText(); // Lấy ID chuyến bay từ trường nhập liệu
        Connection connection = null; 
        PreparedStatement statement = null; 
        
        try {
            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");
            String sql = "DELETE FROM ListFlight WHERE ID = ?"; 
            statement = connection.prepareStatement(sql); 
            statement.setString(1, iDFlight);
            statement.executeUpdate(); // Thực thi truy vấn để xóa chuyến bay
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và các tài nguyên
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
        }
    }
    
    // Phương thức để hiển thị tất cả các chuyến bay
    public void showAllFlight() {
        Connection connection = null; 
        PreparedStatement statement = null; 
        List<Flight> flights = new ArrayList<>(); 
        
        try {
            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");
            String sql = "SELECT * FROM " + "ListFlight"; 
            statement = connection.prepareStatement(sql); 
            ResultSet resultSet = statement.executeQuery(); 
            
            // Đọc dữ liệu từ ResultSet và tạo các đối tượng Flight
            while (resultSet.next()) {
                Flight flight = new Flight(
                        resultSet.getString("ID"),
                        resultSet.getString("StartPoint"),
                        resultSet.getString("EndPoint"),
                        resultSet.getDate("DateStart"),
                        resultSet.getInt("NumberOfSeat"),
                        resultSet.getString("Status"),
                        resultSet.getInt("Price")
                ); 
                flights.add(flight); 
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và các tài nguyên
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0); 
        
        // Thêm các dòng mới vào bảng dựa
        // Thêm các dòng mới vào bảng dựa trên danh sách chuyến bay
        for(Flight flight : flights) {
            tableModel.addRow(new Object[] {
                    flight.getID(), 
                    flight.getStartPoint(), 
                    flight.getEndPoint(), 
                    flight.getDateStart(), 
                    flight.getNumberOfSeat(), 
                    flight.getStatus(), 
                    flight.getPrice()
            }); 
        }
    }
}

