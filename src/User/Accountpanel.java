package User;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Accountpanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPasswordField passwordField_OldPassword;
    private JPasswordField passwordField_NewPassword;
    private JPasswordField passwordField_ReInputNewPassword;

    // Constructor
    public Accountpanel() {
        setLayout(null);
        
        // Panel chính
        JPanel panelMain = new JPanel();
        panelMain.setLayout(null);
        panelMain.setBorder(new LineBorder(new Color(0, 0, 0), 3));
        panelMain.setBounds(100, 98, 500, 400);
        add(panelMain);
        
        // Label và trường nhập mật khẩu cũ
        JLabel oldPasswordLabel = new JLabel("Mật khẩu cũ");
        oldPasswordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        oldPasswordLabel.setBounds(50, 35, 150, 30);
        panelMain.add(oldPasswordLabel);
        
        passwordField_OldPassword = new JPasswordField();
        passwordField_OldPassword.setBounds(45, 65, 400, 40);
        panelMain.add(passwordField_OldPassword);
        
        // Label và trường nhập mật khẩu mới
        JLabel newPasswordLabel = new JLabel("Mật khẩu mới");
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPasswordLabel.setBounds(50, 125, 150, 30);
        panelMain.add(newPasswordLabel);
        
        passwordField_NewPassword = new JPasswordField();
        passwordField_NewPassword.setBounds(45, 155, 400, 40);
        panelMain.add(passwordField_NewPassword);
        
        // Label và trường nhập lại mật khẩu mới
        JLabel reInputNewPasswordLabel = new JLabel("Nhập lại mật khẩu mới");
        reInputNewPasswordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        reInputNewPasswordLabel.setBounds(50, 225, 282, 30);
        panelMain.add(reInputNewPasswordLabel);
        
        passwordField_ReInputNewPassword = new JPasswordField();
        passwordField_ReInputNewPassword.setBounds(45, 255, 400, 40);
        panelMain.add(passwordField_ReInputNewPassword);
        
        // Nút thay đổi mật khẩu
        JButton button_ChangePassword = new JButton("Đổi mật khẩu");
        button_ChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lấy thông tin từ các trường nhập liệu
                String oldPassword = String.valueOf(passwordField_OldPassword.getPassword());
                String newPassword = String.valueOf(passwordField_NewPassword.getPassword());
                String reInputPassword = String.valueOf(passwordField_ReInputNewPassword.getPassword());
                
                // Kiểm tra mật khẩu và thực hiện thay đổi
                int error = checkPassword(oldPassword, newPassword, reInputPassword);
                
                // Hiển thị thông báo phản hồi
                if (error == 0) {
                    updatePassword(newPassword);
                    JOptionPane.showMessageDialog(null, "Bạn đã đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else if (error == 1) {
                    JOptionPane.showMessageDialog(null, "Sai mật khẩu cũ!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                } else if (error == 2) {
                    JOptionPane.showMessageDialog(null, "Mật khẩu mới không hợp lệ!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        button_ChangePassword.setFont(new Font("Arial", Font.BOLD, 20));
        button_ChangePassword.setBounds(175, 315, 170, 40);
        panelMain.add(button_ChangePassword);
    }
    
    // Phương thức kiểm tra mật khẩu
    public int checkPassword(String oldPassword, String newPassword, String reInputPassword) {  
        int error = 0; 
        
        Connection connection = null; 
        PreparedStatement statement = null; 
        ResultSet resultSet = null;
        
        try {
            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "root", "");
            String checkPassword = "SELECT * FROM User_Password WHERE User = ? AND Password = ?";
            statement = connection.prepareStatement(checkPassword); 
            statement.setString(1, MainPage.getUsername()); 
            statement.setString(2, oldPassword); 
            resultSet = statement.executeQuery(); 
            
            // Kiểm tra mật khẩu cũ và mật khẩu mới
            if(!resultSet.next()) {
                error = 1; // Sai mật khẩu cũ
            } else if (newPassword.length() < 6 || reInputPassword.length() < 6 || !newPassword.equals(reInputPassword)) {
                error = 2; // Mật khẩu mới không hợp lệ
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            if (resultSet != null) {
                try {
                    resultSet.close();
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
        }

        return error; // Trả về mã lỗi
    }
    
    // Phương thức để cập nhật mật khẩu mới
    public void updatePassword(String newPassword) {
        Connection connection = null; 
        PreparedStatement statement = null; 
        
        try {
            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "root", "");
            String updateQuery = "UPDATE User_Password SET Password = ? WHERE User = ?";
            statement = connection.prepareStatement(updateQuery);

            // Cập nhật mật khẩu mới
            statement.setString(1, newPassword);
            statement.setString(2, MainPage.getUsername());
            statement.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }
        }
    }
}

