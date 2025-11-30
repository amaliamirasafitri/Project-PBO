import java.awt.*;
import javax.swing.*;

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
        
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitle = new JLabel("Proses Pembayaran Anda", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(lblTitle, gbc);
        
        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInfo.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        add(lblInfo, gbc);
        
        lblQrCode = new JLabel();
        lblQrCode.setHorizontalAlignment(SwingConstants.CENTER);
        lblQrCode.setPreferredSize(new Dimension(200, 200));
        lblQrCode.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        lblQrCode.setOpaque(true);
        lblQrCode.setBackground(Color.WHITE);
        lblQrCode.setText("QR CODE");
        gbc.gridy = 2;
        add(lblQrCode, gbc);
        
        JButton btnSelesai = new JButton("Selesai");
        btnSelesai.setBackground(new Color(52, 152, 219));
        btnSelesai.setForeground(Color.WHITE);
        btnSelesai.setFocusPainted(false);
        btnSelesai.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 3;
        add(btnSelesai, gbc);
        
        btnSelesai.addActionListener(e -> {
            if (currentUsername != null && !currentUsername.isEmpty()) {
                WarnetDataStore.startUserPcSession(currentUsername, targetPCIndex, durationMillis);
            } else {
                WarnetDataStore.startPcSession(targetPCIndex, durationMillis);
            }

            JOptionPane.showMessageDialog(
                    PaymentPanel.this,
                    "Pembayaran berhasil!\n" +
                    "Station " + targetPCIndex + " aktif selama " +
                    formatTimeSimple(durationMillis),
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE
            );

            navigator.showCodeThenBack(currentPaketName, targetPCIndex, durationMillis);
        });
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