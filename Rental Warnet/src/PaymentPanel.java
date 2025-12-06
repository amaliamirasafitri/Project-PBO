import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PaymentPanel extends AbstractWarnetPanel {

    public String currentPaketName;
    private long durationMillis;
    private int targetPCIndex;
    private JLabel lblInfo;
    private JLabel lblQrCode;

    private String currentUsername;

    public PaymentPanel(ScreenNavigator nav) {
        super(nav);

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        NeonBorderPanel neonPanel = new NeonBorderPanel();
        neonPanel.setLayout(new GridBagLayout());
        neonPanel.setBackground(BG_DARK);
        neonPanel.setBorder(new EmptyBorder(50, 80, 50, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1.0;

        JLabel lblTitle = createTitleLabel("PROSES PEMBAYARAN");
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 35, 0);
        neonPanel.add(lblTitle, gbc);

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(TEXT_FONT);
        lblInfo.setForeground(TEXT_SOFT);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 35, 0);
        neonPanel.add(lblInfo, gbc);

        lblQrCode = new JLabel();
        lblQrCode.setHorizontalAlignment(SwingConstants.CENTER);
        lblQrCode.setPreferredSize(new Dimension(220, 220));
        lblQrCode.setBorder(BorderFactory.createLineBorder(NEON_CYAN, 3));
        lblQrCode.setOpaque(true);
        lblQrCode.setBackground(new Color(40, 50, 60));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        neonPanel.add(lblQrCode, gbc);

        loadQrImage();

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
    }

    @Override
    public String getPanelTitle() {
        return "Proses Pembayaran";
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

    private void loadQrImage() {
        String[] candidatePaths = {
                "Assets/img-qr.png",
                "./Assets/img-qr.png",
                "Rental Warnet/Assets/img-qr.png",
                "./Rental Warnet/Assets/img-qr.png"
        };

        File found = null;
        for (String p : candidatePaths) {
            File f = new File(p);
            if (f.exists()) {
                found = f;
                break;
            }
        }

        if (found != null) {
            ImageIcon icon = new ImageIcon(found.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(
                    lblQrCode.getPreferredSize().width,
                    lblQrCode.getPreferredSize().height,
                    Image.SCALE_SMOOTH
            );
            lblQrCode.setIcon(new ImageIcon(img));
            lblQrCode.setText(null);
            return;
        }

        java.net.URL res = getClass().getResource("/Assets/img-qr.png");
        if (res != null) {
            ImageIcon icon = new ImageIcon(res);
            Image img = icon.getImage().getScaledInstance(
                    lblQrCode.getPreferredSize().width,
                    lblQrCode.getPreferredSize().height,
                    Image.SCALE_SMOOTH
            );
            lblQrCode.setIcon(new ImageIcon(img));
            lblQrCode.setText(null);
        } else {
            lblQrCode.setIcon(null);
            lblQrCode.setText("QR CODE");
            lblQrCode.setForeground(new Color(70, 90, 110));
        }
    }
}