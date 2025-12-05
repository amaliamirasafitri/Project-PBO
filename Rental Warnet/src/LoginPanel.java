
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private ScreenNavigator navigator;

    public LoginPanel(ScreenNavigator nav) {
        this.navigator = nav;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 35));

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        // Neon border panel
        NeonBorderPanel neonPanel = new NeonBorderPanel();
        neonPanel.setLayout(new GridBagLayout());
        neonPanel.setBackground(new Color(20, 25, 35));
        neonPanel.setBorder(new EmptyBorder(60, 80, 60, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Title
        JLabel lblTitle = new JLabel("LOGIN SISTEM WARNET");
        lblTitle.setForeground(new Color(0, 255, 255));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 50, 0);
        gbc.weightx = 1.0;
        neonPanel.add(lblTitle, gbc);

        // Username label & field
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(new Color(180, 180, 180));
        lblUser.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 0, 3, 0);
        gbc.weightx = 0;
        neonPanel.add(lblUser, gbc);

        txtUsername = new JTextField(30);
        txtUsername.setBackground(new Color(40, 45, 55));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(new Color(0, 255, 255));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 210), 2));
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUsername.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.weightx = 1.0;
        neonPanel.add(txtUsername, gbc);

        // Password label & field
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(new Color(180, 180, 180));
        lblPass.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 0, 3, 0);
        gbc.weightx = 0;
        neonPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField(30);
        txtPassword.setBackground(new Color(40, 45, 55));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(new Color(0, 255, 255));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 210), 2));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 50, 0);
        gbc.weightx = 1.0;
        neonPanel.add(txtPassword, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        // Login button
        btnLogin = new JButton("MASUK");
        btnLogin.setBackground(new Color(0, 150, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(0, 220, 255), 2));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(btnLogin);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 40, 0);
        gbc.weightx = 1.0;
        neonPanel.add(buttonPanel, gbc);

        // Register link
        JButton btnRegister = new JButton("Belum punya akun? Daftar");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(new Color(0, 200, 255));
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        neonPanel.add(btnRegister, gbc);

        contentPanel.add(neonPanel);
        add(contentPanel, BorderLayout.CENTER);

        btnLogin.addActionListener((ActionEvent e) -> performLogin());
        btnRegister.addActionListener(e -> navigator.goToRegister());
    }

    private void performLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (DatabaseConnection.validateLogin(user, pass)) {
            JOptionPane.showMessageDialog(this, "Login Sukses! Selamat Datang " + user);
            String username = txtUsername.getText().trim();
            navigator.onLoginSuccess(username);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gagal Masuk! Cek username/password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Custom Panel dengan Border Neon seperti di gambar
class NeonBorderPanel extends JPanel {

    private static final Color NEON_CYAN = new Color(0, 255, 255);
    private static final Color DARK_CYAN = new Color(0, 180, 210);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int thickness = 3;
        int cornerLen = 35;

        // Outer glow (shadow effect)
        g2d.setColor(new Color(0, 150, 200, 60));
        g2d.setStroke(new BasicStroke(12));
        g2d.drawRoundRect(2, 2, w - 5, h - 5, 15, 15);

        // Main border (neon cyan)
        g2d.setColor(NEON_CYAN);
        g2d.setStroke(new BasicStroke(thickness));

        // Top border
        g2d.drawLine(cornerLen, thickness, w - cornerLen, thickness);
        // Bottom border
        g2d.drawLine(cornerLen, h - thickness, w - cornerLen, h - thickness);
        // Left border
        g2d.drawLine(thickness, cornerLen, thickness, h - cornerLen);
        // Right border
        g2d.drawLine(w - thickness, cornerLen, w - thickness, h - cornerLen);

        // Rounded corners with decorative lines
        int r = 15;

        // Top-left corner
        g2d.drawArc(thickness, thickness, r * 2, r * 2, 90, 90);
        g2d.drawLine(thickness, thickness, thickness + cornerLen, thickness);
        g2d.drawLine(thickness, thickness, thickness, thickness + cornerLen);

        // Top-right corner
        g2d.drawArc(w - thickness - r * 2, thickness, r * 2, r * 2, 0, 90);
        g2d.drawLine(w - thickness, thickness, w - thickness - cornerLen, thickness);
        g2d.drawLine(w - thickness, thickness, w - thickness, thickness + cornerLen);

        // Bottom-left corner
        g2d.drawArc(thickness, h - thickness - r * 2, r * 2, r * 2, 180, 90);
        g2d.drawLine(thickness, h - thickness, thickness + cornerLen, h - thickness);
        g2d.drawLine(thickness, h - thickness, thickness, h - thickness - cornerLen);

        // Bottom-right corner
        g2d.drawArc(w - thickness - r * 2, h - thickness - r * 2, r * 2, r * 2, 270, 90);
        g2d.drawLine(w - thickness, h - thickness, w - thickness - cornerLen, h - thickness);
        g2d.drawLine(w - thickness, h - thickness, w - thickness, h - thickness - cornerLen);
    }
}
