import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PaymentPanel extends JPanel {

    public ScreenNavigator navigator;
    public String currentPaketName;
    private long durationMillis;
    private int targetPCIndex;
    private JLabel lblInfo;
    private JLabel lblQrCode;

    private String currentUsername;

    public PaymentPanel(ScreenNavigator nav) {
        this.navigator = nav;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 35));

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        NeonBorderPanel neonPanel = new NeonBorderPanel();
        neonPanel.setLayout(new GridBagLayout());
        neonPanel.setBackground(new Color(20, 25, 35));
        neonPanel.setBorder(new EmptyBorder(50, 80, 50, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1.0;

        JLabel lblTitle = new JLabel("PROSES PEMBAYARAN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 255, 255));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 35, 0);
        neonPanel.add(lblTitle, gbc);

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInfo.setForeground(new Color(150, 200, 255));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 35, 0);
        neonPanel.add(lblInfo, gbc);

        lblQrCode = new JLabel();
        lblQrCode.setHorizontalAlignment(SwingConstants.CENTER);
        lblQrCode.setPreferredSize(new Dimension(220, 220));
        lblQrCode.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 3));
        lblQrCode.setOpaque(true);
        lblQrCode.setBackground(new Color(40, 50, 60));
        lblQrCode.setText("QR CODE");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        neonPanel.add(lblQrCode, gbc);

        JButton btnSelesai = new JButton("SELESAI");
        btnSelesai.setBackground(new Color(0, 150, 200));
        btnSelesai.setForeground(Color.WHITE);
        btnSelesai.setFocusPainted(false);
        btnSelesai.setBorder(BorderFactory.createLineBorder(new Color(0, 220, 255), 2));
        btnSelesai.setFont(new Font("Arial", Font.BOLD, 16));
        btnSelesai.setPreferredSize(new Dimension(150, 45));
        btnSelesai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        neonPanel.add(btnSelesai, gbc);

        btnSelesai.addActionListener(e -> {
            if (currentUsername != null && !currentUsername.isEmpty()) {
                WarnetDataStore.startUserPcSession(currentUsername, targetPCIndex, durationMillis);
            } else {
                WarnetDataStore.startPcSession(targetPCIndex, durationMillis);
            }

            JOptionPane.showMessageDialog(
                    PaymentPanel.this,
                    "Pembayaran berhasil!\n"
                            + "Station " + targetPCIndex + " aktif selama "
                            + formatTimeSimple(durationMillis),
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE
            );

            navigator.showCodeThenBack(currentPaketName, targetPCIndex, durationMillis);
        });

        contentPanel.add(neonPanel);
        add(contentPanel, BorderLayout.CENTER);

        loadQrImage();
    }

    private void loadQrImage() {
        String[] candidatePaths = {
                "Assets/img-qr.png",
                "Rental Warnet/Assets/img-qr.png"
        };

        ImageIcon qrIcon = null;

        for (String path : candidatePaths) {
            File f = new File(path);
            if (f.exists()) {
                ImageIcon rawIcon = new ImageIcon(f.getAbsolutePath());
                Image scaled = rawIcon.getImage().getScaledInstance(
                        lblQrCode.getPreferredSize().width,
                        lblQrCode.getPreferredSize().height,
                        Image.SCALE_SMOOTH
                );
                qrIcon = new ImageIcon(scaled);
                break;
            }
        }

        if (qrIcon != null) {
            lblQrCode.setText(null);
            lblQrCode.setIcon(qrIcon);
        }
    }

    public void setCurrentUser(String username) {
        this.currentUsername = username;
    }

    public void updateTransactionInfo(String paket, int pcIndex, long dur) {
        this.currentPaketName = paket;
        this.targetPCIndex = pcIndex;
        this.durationMillis = dur;

        String jamText = (dur / (1000 * 60 * 60)) + " Jam";
        lblInfo.setText("Station " + pcIndex + "  •  " + paket + "  •  " + jamText);
    }

    private String formatTimeSimple(long millis) {
        long totalSeconds = millis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return String.format("%02d jam %02d menit", hours, minutes);
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