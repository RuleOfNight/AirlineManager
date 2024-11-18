package User;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import AirlineManager.MainPage;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_username;
    private JPasswordField passwordField_password;
    private JComboBox<String> comboBox_typeAccount;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginPage frame = new LoginPage();
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
    public LoginPage() {
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 628);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelMain = createMainPanel();
        contentPane.add(panelMain);
    }

    private JPanel createMainPanel() {
        JPanel panelMain = new JPanel();
        panelMain.setLayout(null);
        panelMain.setBorder(new LineBorder(new Color(0, 0, 0), 3));
        panelMain.setBounds(207, 100, 500, 400);

        createLabels(panelMain);
        createTextFields(panelMain);
        createButtons(panelMain);

        return panelMain;
    }

    private void createLabels(JPanel panel) {
        JLabel labelUsername = new JLabel("Tên đăng nhập");
        labelUsername.setFont(new Font("Arial", Font.BOLD, 20));
        labelUsername.setBounds(50, 110, 150, 30);
        panel.add(labelUsername);

        JLabel labelUPassword = new JLabel("Mật khẩu");
        labelUPassword.setFont(new Font("Arial", Font.BOLD, 20));
        labelUPassword.setBounds(50, 210, 150, 30);
        panel.add(labelUPassword);

        JLabel LabelWho = new JLabel("Bạn là...");
        LabelWho.setFont(new Font("Arial", Font.BOLD, 20));
        LabelWho.setBounds(50, 50, 150, 30);
        panel.add(LabelWho);
    }

    private void createTextFields(JPanel panel) {
        textField_username = new JTextField();
        textField_username.setColumns(10);
        textField_username.setBounds(45, 140, 400, 40);
        panel.add(textField_username);

        passwordField_password = new JPasswordField();
        passwordField_password.setBounds(45, 240, 400, 40);
        panel.add(passwordField_password);

        String[] typeAccount = {"", "Người dùng", "Nhà quản lý"};
        comboBox_typeAccount = new JComboBox<>(typeAccount);
        comboBox_typeAccount.setBounds(133, 45, 170, 40);
        panel.add(comboBox_typeAccount);
    }

    private void createButtons(JPanel panel) {
        JButton buttonLogin = new JButton("Đăng nhập");
        buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLoginAction();
            }
        });
        buttonLogin.setFont(new Font("Arial", Font.BOLD, 20));
        buttonLogin.setBounds(175, 300, 170, 40);
        panel.add(buttonLogin);

        JButton buttonSignUp = new JButton("Đăng ký");
        buttonSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSignUpAction();
            }
        });
        buttonSignUp.setFont(new Font("Arial", Font.ITALIC, 13));
        buttonSignUp.setBounds(344, 364, 150, 30);
        panel.add(buttonSignUp);

        JButton buttonSetPasswordAgain = new JButton("Lấy lại mật khẩu");
        buttonSetPasswordAgain.setFont(new Font("Arial", Font.PLAIN, 13));
        buttonSetPasswordAgain.setBounds(6, 364, 150, 30);
        panel.add(buttonSetPasswordAgain);
    }

    private void handleLoginAction() {
        String username = textField_username.getText();
        String password = String.valueOf(passwordField_password.getPassword());
        String typeAccount = comboBox_typeAccount.getSelectedItem().toString();

        if (validateLoginInputs(username, password, typeAccount)) {
            if (typeAccount.equals("Người dùng")) {
                handleUserLogin(username, password);
            } else if (typeAccount.equals("Nhà quản lý")) {
                handleManagerLogin(username, password);
            }
        }
    }

    public boolean validateLoginInputs(String username, String password, String typeAccount) {
        if (username.length() < 6 || password.length() < 6) {
            JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không đúng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (typeAccount.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn loại tài khoản", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    void handleUserLogin(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "root", "");
            String checkExist = "SELECT * FROM User_Password WHERE User = ? AND Password = ?";
            statement = connection.prepareStatement(checkExist);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User.MainPage mainPage = new User.MainPage();
                mainPage.setVisible(true);
                User.MainPage.setUsername(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không đúng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources(connection, statement);
        }
    }

    public void handleManagerLogin(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");
            String checkExist = "SELECT * FROM Manager_Password WHERE ManagerName = ? AND Password = ?";
            statement = connection.prepareStatement(checkExist);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AirlineManager.MainPage mainPage = new MainPage();
                mainPage.setVisible(true);
                AirlineManager.MainPage.setUsername(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không đúng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources(connection, statement);
        }
    }

    public void closeDatabaseResources(Connection connection, PreparedStatement statement) {
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleSignUpAction() {
        SignUpFrame signUpFrame = new SignUpFrame();
        signUpFrame.setVisible(true);
        dispose();
    }
}
