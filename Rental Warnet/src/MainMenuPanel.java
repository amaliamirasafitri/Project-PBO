import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainMenuPanel extends JPanel {

    private JButton btnPilihPC;
    private JButton btnDaftarGame;
    private JButton btnLogout;
    private ScreenNavigator navigator;

    public MainMenuPanel(ScreenNavigator nav) {
        this.navigator = nav;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 35));

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        NeonBorderPanel neonPanel = new NeonBorderPanel();
        neonPanel.setLayout(new GridBagLayout());
        neonPanel.setBackground(new Color(20, 25, 35));
        neonPanel.setBorder(new EmptyBorder(60, 100, 60, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1.0;

        JLabel lblTitle = new JLabel("MENU UTAMA WARNET");
        lblTitle.setForeground(new Color(0, 255, 255));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        neonPanel.add(lblTitle, gbc);

        btnPilihPC = new JButton("PILIH PC");
        styleButton(btnPilihPC, new Color(0, 150, 200));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.ipady = 15;
        neonPanel.add(btnPilihPC, gbc);

        btnDaftarGame = new JButton("DAFTAR GAME");
        styleButton(btnDaftarGame, new Color(0, 150, 200));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        neonPanel.add(btnDaftarGame, gbc);

        btnLogout = new JButton("LOGOUT");
        styleButton(btnLogout, new Color(150, 80, 80));
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(200, 100, 100), 2));
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        neonPanel.add(btnLogout, gbc);

        contentPanel.add(neonPanel);
        add(contentPanel, BorderLayout.CENTER);

        btnPilihPC.addActionListener(e -> navigator.goToPCSelection(""));
        btnDaftarGame.addActionListener(e -> navigator.goToGameList());
        btnLogout.addActionListener(e -> navigator.onLogout());
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 220, 255), 2));
        btn.setPreferredSize(new Dimension(300, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}

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

        g2d.setColor(new Color(0, 150, 200, 60));
        g2d.setStroke(new BasicStroke(12));
        g2d.drawRoundRect(2, 2, w - 5, h - 5, 15, 15);

        g2d.setColor(NEON_CYAN);
        g2d.setStroke(new BasicStroke(thickness));

        g2d.drawLine(cornerLen, thickness, w - cornerLen, thickness);
        g2d.drawLine(cornerLen, h - thickness, w - cornerLen, h - thickness);
        g2d.drawLine(thickness, cornerLen, thickness, h - cornerLen);
        g2d.drawLine(w - thickness, cornerLen, w - thickness, h - cornerLen);

        int r = 15;

        g2d.drawArc(thickness, thickness, r * 2, r * 2, 90, 90);
        g2d.drawLine(thickness, thickness, thickness + cornerLen, thickness);
        g2d.drawLine(thickness, thickness, thickness, thickness + cornerLen);

        g2d.drawArc(w - thickness - r * 2, thickness, r * 2, r * 2, 0, 90);
        g2d.drawLine(w - thickness, thickness, w - thickness - cornerLen, thickness);
        g2d.drawLine(w - thickness, thickness, w - thickness, thickness + cornerLen);

        g2d.drawArc(thickness, h - thickness - r * 2, r * 2, r * 2, 180, 90);
        g2d.drawLine(thickness, h - thickness, thickness + cornerLen, h - thickness);
        g2d.drawLine(thickness, h - thickness, thickness, h - thickness - cornerLen);

        g2d.drawArc(w - thickness - r * 2, h - thickness - r * 2, r * 2, r * 2, 270, 90);
        g2d.drawLine(w - thickness, h - thickness, w - thickness - cornerLen, h - thickness);
        g2d.drawLine(w - thickness, h - thickness, w - thickness, h - thickness - cornerLen);
    }
}