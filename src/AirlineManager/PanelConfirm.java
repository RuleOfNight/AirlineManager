package AirlineManager;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import User.MainPage;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelConfirm extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPasswordField passwordField_ReInputNewPassword;
    private JPasswordField passwordField_OldPassword;
    private JPasswordField passwordField_NewPassword;

    /**
     * Tạo panel.
     */
    public PanelConfirm() {
        this.setBounds(0, 0, 700, 520);
        setLayout(null);
        
        JPanel panelMain = new JPanel();
        panelMain.setLayout(null);
        panelMain.setBorder(new LineBorder(new Color(0, 0, 0),3));
        panelMain.setBounds(100, 98, 500, 400);
        add(panelMain);
        
        // Nhãn cho các trường nhập mật khẩu
        JLabel OldPassword = new JLabel("Mật khẩu mới");
        OldPassword.setFont(new Font("Arial", Font.BOLD, 20));
        OldPassword.setBounds(50, 125, 150, 30);
        panelMain.add(OldPassword);
        
        JLabel NewPassword = new JLabel("Nhập lại mật khẩu mới ");
        NewPassword.setFont(new Font("Arial", Font.BOLD, 20));
        NewPassword.setBounds(50, 225, 282, 30);
        panelMain.add(NewPassword);
        
        JLabel ReInputPassword = new JLabel("Mật khẩu cũ");
        ReInputPassword.setFont(new Font("Arial", Font.BOLD, 20));
        ReInputPassword.setBounds(50, 35, 150, 30);
        panelMain.add(ReInputPassword);
        
        // Trường nhập mật khẩu
        passwordField_ReInputNewPassword = new JPasswordField();
        passwordField_ReInputNewPassword.setBounds(45, 255, 400, 40);
        panelMain.add(passwordField_ReInputNewPassword);
        
        passwordField_OldPassword = new JPasswordField();
        passwordField_OldPassword.setBounds(45, 65, 400, 40);
        panelMain.add(passwordField_OldPassword);
        
        passwordField_NewPassword = new JPasswordField();
        passwordField_NewPassword.setBounds(45, 155, 400, 40);
        panelMain.add(passwordField_NewPassword);
        
        // Nút để thay đổi mật khẩu
        JButton button_ChangePassword = new JButton("Đổi mật khẩu");
        button_ChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String oldPassword = passwordField_OldPassword.getPassword().toString(); 
                String newPassword = passwordField_NewPassword.getPassword().toString(); 
                String reInputPassword = passwordField_ReInputNewPassword.getPassword().toString(); 
                int error = checkPassWord(oldPassword, newPassword, reInputPassword); 
                
                if (error == 0) {
                    updatePassword(newPassword); 
                    return; 
                } else if (error == 1) {
                    JOptionPane.showMessageDialog(null, "Sai sai mật khẩu cũ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return; 
                } else if (error == 2){
                    JOptionPane.showMessageDialog(null, "Mật khẩu mới không hợp lệ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return; 
                }
            }
        });
        button_ChangePassword.setFont(new Font("Arial", Font.BOLD, 20));
        button_ChangePassword.setBounds(175, 315, 170, 40);
        panelMain.add(button_ChangePassword);

    }
    
    /**
     * Kiểm tra tính hợp lệ của mật khẩu.
     * 
     * @param oldPassword     Mật khẩu cũ
     * @param newPassword     Mật khẩu mới
     * @param reInputPassword Nhập lại mật khẩu mới
     * @return Mã lỗi: 0 - không có lỗi, 1 - sai mật khẩu cũ, 2 - mật khẩu mới không hợp lệ
     */
    public int checkPassWord(String oldPassword, String newPassword, String reInputPassword) {  
        int error = 0; 
        
        Connection connection = null; 
        PreparedStatement statement = null; 
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");
            String checkPassword = "SELECT * FROM Manager_Password WHERE ManagerName = ? AND Password = ?";
            statement = connection.prepareStatement(checkPassword); 
            statement.setString(1, MainPage.getUsername()); 
            statement.setString(2, oldPassword); 
            ResultSet resultSet = statement.executeQuery(); 
            
            if(resultSet.next()) {
                error = 0;
            }else {
                error = 1; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
        if (newPassword.length() < 6 || reInputPassword.length() < 6) {
            error = 2; 
        }
        if (!newPassword.equals(reInputPassword)) {
            error = 2; 
        }

        return error; 
    }
    
    /**
     * Cập nhật mật khẩu mới trong cơ sở dữ liệu.
     * 
     * @param newPassword Mật khẩu mới
     */
    public void updatePassword(String newPassword) {

        Connection connection = null; 
        PreparedStatement statement = null; 
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "root", "");
            String updateQuery = "UPDATE Manager_Password SET Password = ? WHERE ManagerName = ?";
            statement = connection.prepareStatement(updateQuery);

            // Thiết lập tham số cho câu lệnh UPDATE
            statement.setString(1, newPassword);
            statement.setString(2, MainPage.getUsername());
            statement.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
}
