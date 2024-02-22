import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtility {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "";
    private static final String PASSWORD = "";

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw e;
        }
    }

    public static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }

    public static List<MedicineSale> getMedicineSalesData() throws SQLException {
        List<MedicineSale> medicineSales = new ArrayList<>();
        String query = "SELECT * FROM medicine_sales";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MedicineSale sale = new MedicineSale();
                sale.setId(resultSet.getInt("id"));
                sale.setMedicineName(resultSet.getString("medicine_name"));
                sale.setManufacturer(resultSet.getString("manufacturer"));
                sale.setSales(resultSet.getInt("sales"));
                medicineSales.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving medicine sales data: " + e.getMessage());
            throw e;
        }
        return medicineSales;
    }

    public static void insertMedicineSale(MedicineSale sale) throws SQLException {
        String query = "INSERT INTO medicine_sales (medicine_name, manufacturer, sales) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sale.getMedicineName());
            preparedStatement.setString(2, sale.getManufacturer());
            preparedStatement.setInt(3, sale.getSales());
            preparedStatement.executeUpdate();
            System.out.println("Sale data inserted successfully");
        } catch (SQLException e) {
            System.err.println("Error inserting medicine sale data: " + e.getMessage());
            throw e;
        }
    }
}
