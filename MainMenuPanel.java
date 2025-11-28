import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private JButton btnPilihPC;
    private JButton btnDaftarGame;
    private JButton btnLogout;
    private ScreenNavigator navigator;

    public MainMenuPanel(ScreenNavigator nav) {
        this.navigator = nav;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Judul
        JLabel lblTitle = new JLabel("MENU UTAMA WARNET", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.CYAN);
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(lblTitle, gbc);

        // Tombol Pilih PC
        btnPilihPC = new JButton("Pilih PC");
        styleButton(btnPilihPC);
        gbc.gridy = 1;
        add(btnPilihPC, gbc);

        // Tombol Daftar Game
        btnDaftarGame = new JButton("Daftar Game");
        styleButton(btnDaftarGame);
        gbc.gridy = 2;
        add(btnDaftarGame, gbc);

        // Tombol Logout
        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(180, 0, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        gbc.gridy = 3;
        add(btnLogout, gbc);

        // Action Listener
        btnPilihPC.addActionListener(e -> navigator.goToPCSelection(""));  // kosongkan paketName atau sesuaikan
        btnDaftarGame.addActionListener(e -> showDaftarGame());
        btnLogout.addActionListener(e -> navigator.goToLogin());
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
    }

    private void showDaftarGame() {
        // Jika Anda memiliki panel game list, tinggal panggil navigasi ke sana
        // Misal: navigator.goToGameList();
        JOptionPane.showMessageDialog(this, "Fungsi Daftar Game belum diimplementasikan.");
    }
}
