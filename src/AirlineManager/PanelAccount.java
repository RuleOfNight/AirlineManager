package AirlineManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;

import User.LoginPage;
import User.MainPage;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelAccount extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JTextField textFieldPointStarting;
	private final JTextField textFieldPointEnding;
	private final JTextField textFieldDayDeparture;
	private final JTextField textFieldPassengerNumber;
	private final JTextField textFieldPriceTicket;
	private final JTextField textfieldID;
	private Flight flight; 


	public PanelAccount() {
		this.setBounds(0, 0, 700, 600);
		setLayout(null);
		
		JLabel labelConfirm = new JLabel("Xác nhận thêm chuyến");
		labelConfirm.setFont(new Font("Arial", Font.BOLD, 30));
		labelConfirm.setBounds(157, 29, 402, 47);
		add(labelConfirm);
		
		JLabel labelPointStarting = new JLabel("Điểm bắt đầu:");
		labelPointStarting.setFont(new Font("Arial", Font.BOLD, 20));
		labelPointStarting.setBounds(10, 103, 220, 40);
		add(labelPointStarting);
		
		JLabel labelPointEnding = new JLabel("Điểm đến:");
		labelPointEnding.setFont(new Font("Arial", Font.BOLD, 20));
		labelPointEnding.setBounds(10, 183, 220, 40);
		add(labelPointEnding);
		
		JLabel lebelDayDeparture = new JLabel("Ngày khởi hành:");
		lebelDayDeparture.setFont(new Font("Arial", Font.BOLD, 20));
		lebelDayDeparture.setBounds(10, 263, 220, 40);
		add(lebelDayDeparture);
		
		JLabel labelPassengerNumber = new JLabel("Số hành khách:");
		labelPassengerNumber.setFont(new Font("Arial", Font.BOLD, 20));
		labelPassengerNumber.setBounds(10, 343, 220, 40);
		add(labelPassengerNumber);
		
		JLabel labelPriceTicket = new JLabel("Giá vé:");
		labelPriceTicket.setFont(new Font("Arial", Font.BOLD, 20));
		labelPriceTicket.setBounds(10, 423, 220, 40);
		add(labelPriceTicket);
		
		textFieldPointStarting = new JTextField();
		textFieldPointStarting.setBounds(207, 103, 184, 40);
		add(textFieldPointStarting);
		textFieldPointStarting.setColumns(10);
		
		textFieldPointEnding = new JTextField();
		textFieldPointEnding.setColumns(10);
		textFieldPointEnding.setBounds(207, 183, 184, 40);
		add(textFieldPointEnding);
		
		textFieldDayDeparture = new JTextField();
		textFieldDayDeparture.setColumns(10);
		textFieldDayDeparture.setBounds(207, 263, 184, 40);
		add(textFieldDayDeparture);
		
		textFieldPassengerNumber = new JTextField();
		textFieldPassengerNumber.setColumns(10);
		textFieldPassengerNumber.setBounds(207, 343, 184, 40);
		add(textFieldPassengerNumber);
		
		textFieldPriceTicket = new JTextField();
		textFieldPriceTicket.setColumns(10);
		textFieldPriceTicket.setBounds(207, 423, 184, 40);
		add(textFieldPriceTicket);
		
		JLabel lblMChuyn = new JLabel("Mã chuyến:");
		lblMChuyn.setFont(new Font("Arial", Font.BOLD, 20));
		lblMChuyn.setBounds(450, 103, 120, 40);
		add(lblMChuyn);
		
		textfieldID = new JTextField();
		textfieldID.setColumns(10);
		textfieldID.setBounds(445, 143, 184, 40);
		add(textfieldID);
		
		JButton buttonConfirm = new JButton("Xác nhận");
		buttonConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creatTable();
			}
		});
		buttonConfirm.setFont(new Font("Arial", Font.BOLD, 20));
		buttonConfirm.setBounds(493, 531, 144, 40);
		
		add(buttonConfirm);

	}
	
	/**
	 * Tạo bảng chứa thông tin chuyến bay trong cơ sở dữ liệu
	 */
	public void creatTable() {
        if (flight == null) {
            JOptionPane.showMessageDialog(null, "Không có thông tin chuyến bay.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ListFlight WHERE ID = ?");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO ListFlight(ID, StartPoint, EndPoint, DateStart, NumberOfSeat, Status, Price) VALUES (?, ?, ?, ?, ?, ?, ? )");
             java.sql.Statement statement2 = connection.createStatement()) {

            statement.setString(1, flight.getID());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Chuyến bay đã tồn tại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                insertStatement.setString(1, flight.getID());
                insertStatement.setString(2, flight.getStartPoint());
                insertStatement.setString(3, flight.getEndPoint());
                insertStatement.setDate(4, flight.getDateStart());
                insertStatement.setInt(5, flight.getNumberOfSeat());
                insertStatement.setString(6, flight.getStatus());
                insertStatement.setInt(7, flight.getPrice());
                insertStatement.execute();

                String createTableQuery = "CREATE TABLE IF NOT EXISTS " + flight.getID() + "(Name VARCHAR(50), BirthDate Date, PhoneNumber VARCHAR(100), Email VARCHAR(100), CCCD_CMND VARCHAR(255), ClassSeat VARCHAR(100), TypeTicket VARCHAR(255))";
                statement2.execute(createTableQuery);

                int option = JOptionPane.showOptionDialog(null,
                        "Thêm chuyến thành công!",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Đặt chuyến tiếp", "Quay về màn hình chính"},
                        "Đặt chuyến tiếp");

                if (option == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    flight = null;

                    goToMainPage();
                } else if (option == JOptionPane.NO_OPTION) {
                    setVisible(false);
                    flight = null;

                    goToEmptyPanel();                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm chuyến bay: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void goToMainPage() {
       AirlineManager.MainPage.panelAddNewFlight.setVisible(true);
    }


    private void goToEmptyPanel() {
    	AirlineManager.MainPage.emptyJPanel.setVisible(true);
    }

    public void setTextField() {
        textfieldID.setText(flight.getID());
        textFieldPointStarting.setText(flight.getStartPoint());
        textFieldPointEnding.setText(flight.getEndPoint());
        textFieldDayDeparture.setText(flight.getDateStart().toString());
        String newPrice = String.format("%d", flight.getPrice()); 
        textFieldPriceTicket.setText(newPrice);
        String numberPassenger = String.format("%d", flight.getNumberOfSeat()); 
        textFieldPassengerNumber.setText(numberPassenger); 
    }
    

    public Flight getFlight() {
        return flight;
    }
    

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
}

