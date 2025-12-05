
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegisterPanel extends JPanel {

    private JTextField txtUser;
    private JPasswordField txtPass, txtConfirmPass;
    private JButton btnRegister, btnBack;
    private ScreenNavigator navigator;

    public RegisterPanel(ScreenNavigator nav) {
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
        neonPanel.setBorder(new EmptyBorder(50, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Title
        JLabel lblTitle = new JLabel("DAFTAR AKUN BARU");
        lblTitle.setForeground(new Color(0, 255, 255));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
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

        txtUser = new JTextField(30);
        txtUser.setBackground(new Color(40, 45, 55));
        txtUser.setForeground(Color.WHITE);
        txtUser.setCaretColor(new Color(0, 255, 255));
        txtUser.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 210), 2));
        txtUser.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUser.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.weightx = 1.0;
        neonPanel.add(txtUser, gbc);

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

        txtPass = new JPasswordField(30);
        txtPass.setBackground(new Color(40, 45, 55));
        txtPass.setForeground(Color.WHITE);
        txtPass.setCaretColor(new Color(0, 255, 255));
        txtPass.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 210), 2));
        txtPass.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPass.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.weightx = 1.0;
        neonPanel.add(txtPass, gbc);

        // Confirm Password label & field
        JLabel lblConf = new JLabel("Ulangi Password:");
        lblConf.setForeground(new Color(180, 180, 180));
        lblConf.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 0, 3, 0);
        gbc.weightx = 0;
        neonPanel.add(lblConf, gbc);

        txtConfirmPass = new JPasswordField(30);
        txtConfirmPass.setBackground(new Color(40, 45, 55));
        txtConfirmPass.setForeground(Color.WHITE);
        txtConfirmPass.setCaretColor(new Color(0, 255, 255));
        txtConfirmPass.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 210), 2));
        txtConfirmPass.setFont(new Font("Arial", Font.PLAIN, 13));
        txtConfirmPass.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.weightx = 1.0;
        neonPanel.add(txtConfirmPass, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        btnRegister = new JButton("DAFTAR");
        btnRegister.setBackground(new Color(0, 150, 200));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createLineBorder(new Color(0, 220, 255), 2));
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(120, 40));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(btnRegister);

        btnBack = new JButton("BATAL");
        btnBack.setBackground(new Color(100, 60, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createLineBorder(new Color(200, 100, 100), 2));
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(120, 40));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.weightx = 1.0;
        neonPanel.add(buttonPanel, gbc);

        contentPanel.add(neonPanel);
        add(contentPanel, BorderLayout.CENTER);

        btnRegister.addActionListener((ActionEvent e) -> performRegister());
        btnBack.addActionListener(e -> navigator.goToLogin());
    }

    private void performRegister() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        String conf = new String(txtConfirmPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return;
        }

        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Password konfirmasi tidak cocok!");
            return;
        }

        if (DatabaseConnection.registerUser(user, pass)) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
            navigator.goToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal Daftar! Username mungkin sudah ada.");
        }
    }
}

// Custom Panel dengan Border Neon seperti di Login
class NeonBorderPanel extends JPanel {

    private static final Color NEON_CYAN = new Color(0, 255, 255);

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
