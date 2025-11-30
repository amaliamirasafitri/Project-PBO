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

        JLabel lblTitle = new JLabel("MENU UTAMA WARNET", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.CYAN);
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(lblTitle, gbc);

        btnPilihPC = new JButton("Pilih PC");
        styleButton(btnPilihPC);
        gbc.gridy = 1;
        add(btnPilihPC, gbc);

        btnDaftarGame = new JButton("Daftar Game");
        styleButton(btnDaftarGame);
        gbc.gridy = 2;
        add(btnDaftarGame, gbc);

        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(180, 0, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        gbc.gridy = 3;
        add(btnLogout, gbc);

        btnPilihPC.addActionListener(e -> navigator.goToPCSelection(""));
        btnDaftarGame.addActionListener(e -> navigator.goToGameList());
        btnLogout.addActionListener(e -> navigator.onLogout());
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
    }
}