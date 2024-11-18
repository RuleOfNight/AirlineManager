package User;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ObjectInputFilter.Status;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

/**
 * This panel allows users to search for available flights based on their departure and destination locations.
 * Users can select a flight from the displayed list and proceed with their booking.
 */
public class ListFlightPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableListFlight;
    private String startPoint = "";
    private String endPoint = "";
    private String idFlight = "";
    private Date dateStart;
    private int NumberOfSeat;
    private String status;
    private int price;
    private DefaultTableModel tableModel;
    private Flight flight;
    private boolean acceptable = false;

    /**
     * Create the panel.
     */
    public ListFlightPanel() {
        this.setBounds(0, 0, 700, 600);
        setLayout(null);

        // Combo boxes for selecting departure and destination locations
        String[] departureLocations = {"", "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Quảng Ninh"};
        JComboBox<String> comboBox_StartingPoint = new JComboBox<>(departureLocations);
        comboBox_StartingPoint.setBounds(50, 70, 150, 40);
        add(comboBox_StartingPoint);

        String[] destinationLocations = {"", "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Quảng Ninh"};
        JComboBox<String> comboBox_EndingPoint = new JComboBox<String>(destinationLocations);
        comboBox_EndingPoint.setBounds(270, 70, 150, 40);
        add(comboBox_EndingPoint);

        // Labels for departure and destination points
        JLabel labelEndingPoint = new JLabel("Điểm đến");
        labelEndingPoint.setFont(new Font("Arial", Font.BOLD, 22));
        labelEndingPoint.setBounds(55, 35, 150, 35);
        add(labelEndingPoint);

        JLabel labelStartingPoint = new JLabel("Điểm đi");
        labelStartingPoint.setFont(new Font("Arial", Font.BOLD, 22));
        labelStartingPoint.setBounds(275, 35, 150, 35);
        add(labelStartingPoint);

        // Table for displaying flight information
        String[] columnNames = {"ID", "Điểm đi", "Điểm đến", "Ngày khởi hành", "Số ghế trống", "Trạng thái", "Giá vé"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableListFlight = new JTable(tableModel);
        tableListFlight.getColumnModel().getColumn(0).setPreferredWidth(34);
        tableListFlight.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableListFlight.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableListFlight.getColumnModel().getColumn(4).setPreferredWidth(77);

        JScrollPane scrollPaneListFlight = new JScrollPane(tableListFlight);
        scrollPaneListFlight.setBounds(50, 160, 610, 339);
        add(scrollPaneListFlight);

        // Mechanism to retrieve the selected flight ID by the user
        tableListFlight.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tableListFlight.getSelectedRow();
                if (selectedRow != -1) {
                    idFlight = tableListFlight.getValueAt(selectedRow, 0).toString();
                    startPoint = tableListFlight.getValueAt(selectedRow, 1).toString();
                    endPoint = tableListFlight.getValueAt(selectedRow, 2).toString();
                    String dateString = tableListFlight.getValueAt(selectedRow, 3).toString();
                    String format = "yyyy-MM-dd";
                    try {
                        dateStart = convertStringToDate(dateString, format);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    NumberOfSeat = (int) tableListFlight.getValueAt(selectedRow, 4);
                    status = tableListFlight.getValueAt(selectedRow, 5).toString();
                    price = (int) tableListFlight.getValueAt(selectedRow, 6);
                    flight = new Flight(idFlight, startPoint, endPoint, dateStart, NumberOfSeat, status, price);
                    acceptable = true;
                }
            }
        });

        // Button to select a flight
        JButton buttonSelectFlight = new JButton("Chọn chuyến");
        buttonSelectFlight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (acceptable) {
                    setVisible(false);
                    MainPage.fillInformationPanel.setVisible(true);
                    MainPage.panelPay.setFlight(flight);
                } else {
                    JOptionPane.showMessageDialog(null, "Bạn chưa chọn chuyến muốn đi", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonSelectFlight.setFont(new Font("Arial", Font.BOLD, 15));
        buttonSelectFlight.setBounds(529, 549, 131, 40);
        add(buttonSelectFlight);

        // Button to initiate the flight search
        JButton buttonSearch = new JButton("Search");
        buttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchFlights();
            }
        });
        buttonSearch.setFont(new Font("Arial", Font.BOLD, 15));
        buttonSearch.setBounds(555, 70, 105, 40);
        add(buttonSearch);
    }

    // Method to search for available flights based on user input
    private void searchFlights() {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Flight> flights = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Manager", "root", "");

            String sql = "SELECT * FROM ListFlight WHERE StartPoint = ? AND EndPoint = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, startPoint);
            statement.setString(2, endPoint);
            ResultSet resultSet = statement.executeQuery();

            try {
                while (resultSet.next()) {
                    LocalDate currentDate = LocalDate.now();
                    LocalDate flightDate = resultSet.getDate("DateStart").toLocalDate();

                    // Check for available seats and future departure dates
                    if (resultSet.getInt("NumberOfSeat") > 0 && flightDate.isAfter(currentDate)) {
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exceptions if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (connection != null) {
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

        // Clear old data in table
        tableModel.setRowCount(0);

        // Add new data to table
        for (Flight flight : flights) {
            tableModel.addRow(new Object[]{
                    flight.getID(),
                    flight.getStartPoint(),
                    flight.getEndPoint(),
                    flight.getDateStart(),
                    flight.getNumberOfSeat(),
                    flight.getStatus(),
                    flight.getPrice(),
            });
        }
    }

    /**
     * Convert a string to a Date object.
     *
     * @param dateString The string representing the date.
     * @param format     The format of the date string.
     * @return The Date object parsed from the string.
     * @throws ParseException if the string cannot be parsed as a date.
     */
    public static Date convertStringToDate(String dateString, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        java.util.Date parsedDate = dateFormat.parse(dateString);
        return new Date(parsedDate.getTime());
    }
}

