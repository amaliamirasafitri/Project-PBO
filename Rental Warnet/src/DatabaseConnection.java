import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_warnet";
    private static final String USER = "root";     
    private static final String PASS = "";
    
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

