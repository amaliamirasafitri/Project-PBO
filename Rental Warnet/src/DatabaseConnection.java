import java.sql.*;

public class DatabaseConnection {
    private static final String DB_NAME = "db_warnet";
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String URL = BASE_URL + DB_NAME;
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        initDatabase();
    }

    private static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Gagal membuat database: " + e.getMessage());
        }

        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL DEFAULT 'operator'
            )
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createUsersTable);
        } catch (SQLException e) {
            System.err.println("Gagal membuat tabel users: " + e.getMessage());
        }
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'operator')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Username sudah terpakai!");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String inputUser, String inputPass) {
        boolean isValid = false;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inputUser);
            stmt.setString(2, inputPass);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = true;
            }

        } catch (SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
        }
        return isValid;
    }
}