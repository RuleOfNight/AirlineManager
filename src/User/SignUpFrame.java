package User;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class SignUpFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_username;
	private JPasswordField passwordField_firsttime;
	private JPasswordField passwordField_secondtime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUpFrame frame = new SignUpFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SignUpFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelMain = new JPanel();
		panelMain.setLayout(null);
		panelMain.setBorder(new LineBorder(new Color(0, 0, 0),3));
		panelMain.setBounds(189, 40, 500, 492);
		contentPane.add(panelMain);
		
		JLabel labelUsername = new JLabel("Tên đăng nhập");
		labelUsername.setFont(new Font("Arial", Font.BOLD, 20));
		labelUsername.setBounds(50, 110, 150, 30);
		panelMain.add(labelUsername);
		
		JLabel labelUPassword = new JLabel("Mật khẩu");
		labelUPassword.setFont(new Font("Arial", Font.BOLD, 20));
		labelUPassword.setBounds(50, 210, 150, 30);
		panelMain.add(labelUPassword);
		
		JLabel LabelWho = new JLabel("Bạn là...");
		LabelWho.setFont(new Font("Arial", Font.BOLD, 20));
		LabelWho.setBounds(50, 50, 150, 30);
		panelMain.add(LabelWho);
		
		JLabel LabelReEnterPassword = new JLabel("Nhập lại mật khẩu");
		LabelReEnterPassword.setFont(new Font("Arial", Font.BOLD, 20));
		LabelReEnterPassword.setBounds(50, 310, 295, 30);
		panelMain.add(LabelReEnterPassword);
		
		textField_username = new JTextField();
		textField_username.setColumns(10);
		textField_username.setBounds(45, 140, 400, 40);
		panelMain.add(textField_username);
		
		passwordField_firsttime = new JPasswordField();
		passwordField_firsttime.setBounds(45, 240, 400, 40);
		panelMain.add(passwordField_firsttime);
		
		passwordField_secondtime = new JPasswordField();
		passwordField_secondtime.setBounds(45, 340, 400, 40);
		panelMain.add(passwordField_secondtime);
		
		String[] typeAccount = {"","Người dùng", "Nhà quản lý"};
		JComboBox<String> comboBox_typeAccount = new JComboBox<String>(typeAccount);
		comboBox_typeAccount.setBounds(133, 45, 170, 40);
		panelMain.add(comboBox_typeAccount);
		
		JButton buttonSignUp = new JButton("Đăng ký");
		buttonSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    String typeAccount = comboBox_typeAccount.getSelectedItem().toString(); 
			    String username = textField_username.getText().trim(); 
			    String password = new String(passwordField_firsttime.getPassword()); 
			    String reinputPassword = new String(passwordField_secondtime.getPassword()); 
			    boolean status = true; 

			    if(username.length() < 6) {
			        status = false;
			        JOptionPane.showMessageDialog(null, "Tên đăng nhập không hợp lệ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			    }

			    if(password.length() < 6) {
			        status = false;
			        JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			    }

			    if (!reinputPassword.equals(password)) {
			        status = false;
			        JOptionPane.showMessageDialog(null, "Mật khẩu không trùng khớp", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			    }

			    if (typeAccount.isEmpty()) {
			        status = false; 
			        JOptionPane.showMessageDialog(null, "Bạn chưa chọn loại tài khoản", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			    }

			    if(status) {
			        if(typeAccount.equals("Người dùng")) {
			        	Connection connection = null; 
				        PreparedStatement statement = null; 
				        java.sql.Statement statement2 = null; 

				        try {
				            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "root", "");

				            // Kiểm tra xem tài khoản đã tồn tại hay chưa
				            String checkExistQuery = "SELECT * FROM User_Password WHERE User = ?";
				            statement = connection.prepareStatement(checkExistQuery);
				            statement.setString(1, username);
				            ResultSet resultSet = statement.executeQuery();

				            if (resultSet.next()) {
				                // Tài khoản đã tồn tại
				                JOptionPane.showMessageDialog(null, "Tài khoản đã tồn tại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				            } else {
				                // Tài khoản chưa tồn tại, thêm mới
				                String inputData = "INSERT INTO User_Password(User, Password) VALUES (?, ?)";
				                statement = connection.prepareStatement(inputData);
				                statement.setString(1, username); 
				                statement.setString(2, password); 
				                statement.execute(); 

				                // Tạo bảng mới cho tài khoản
				                String createTableQuery = "CREATE TABLE IF NOT EXISTS " + username + "(ID VARCHAR(50), StartPoint VARCHAR(100), EndPoint VARCHAR(100), DateStart Date, ClassSeat VARCHAR(255), TypeTicket VARCHAR(255), Status VARCHAR(50))";
				                statement2 = connection.createStatement();
				                statement2.execute(createTableQuery);
				                
				                JOptionPane.showMessageDialog(null, "Bạn đã đăng ký thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
						        
						        // Hiển thị hộp thoại hỏi có quay về màn hình đăng nhập không
						        int option = JOptionPane.showConfirmDialog(null, "Bạn muốn quay về màn hình đăng nhập không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
						        
						        // Nếu người dùng chọn Yes (có)
						        if (option == JOptionPane.YES_OPTION) {
						            dispose(); 
						            LoginPage loginPage = new LoginPage(); 
						            loginPage.setVisible(true); 
						        } else {
						            
						        }
				                
				            }
				        } catch (SQLException e1) {
				            e1.printStackTrace();
				        } finally {
				            // Đóng các tài nguyên
				            try {
				                if(connection != null) connection.close();
				                if(statement != null) statement.close();
				                if(statement2 != null) statement2.close();
				            } catch (SQLException e1) {
				                e1.printStackTrace();
				            } 
				        }
				        
			        }else {
			        	Connection connection = null; 
				        PreparedStatement statement = null; 

				        try {
				            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");

				            // Kiểm tra xem tài khoản đã tồn tại hay chưa
				            String checkExistQuery = "SELECT * FROM Manager_Password WHERE ManagerName = ?";
				            statement = connection.prepareStatement(checkExistQuery);
				            statement.setString(1, username);
				            ResultSet resultSet = statement.executeQuery();

				            if (resultSet.next()) {
				                // Tài khoản đã tồn tại
				                JOptionPane.showMessageDialog(null, "Tài khoản đã tồn tại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				            } else {
				                // Tài khoản chưa tồn tại, thêm mới
				                String inputData = "INSERT INTO Manager_Password(ManagerName, Password) VALUES (?, ?)";
				                statement = connection.prepareStatement(inputData);
				                statement.setString(1, username); 
				                statement.setString(2, password); 
				                statement.execute(); 
				            }
				        } catch (SQLException e1) {
				            e1.printStackTrace();
				        } finally {
				            // Đóng các tài nguyên
				            try {
				                if(connection != null) connection.close();
				                if(statement != null) statement.close();
				            } catch (SQLException e1) {
				                e1.printStackTrace();
				            } 
				        }
			        }
			    }
			}
		});
		buttonSignUp.setFont(new Font("Arial", Font.BOLD, 20));
		buttonSignUp.setBounds(175, 430, 170, 40);
		panelMain.add(buttonSignUp);
		
		JLabel lblNewLabel = new JLabel("(Tên đăng nhập phải bao gồm 6 ký tự)");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 14));
		lblNewLabel.setBounds(50, 180, 395, 20);
		panelMain.add(lblNewLabel);
		
		JLabel lblmtKhuPhi = new JLabel("(Mật khẩu phải bao gồm 6 ký tự)");
		lblmtKhuPhi.setFont(new Font("Lucida Grande", Font.ITALIC, 14));
		lblmtKhuPhi.setBounds(50, 280, 395, 20);
		panelMain.add(lblmtKhuPhi);
	}

}
