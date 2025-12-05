
import java.awt.*;
import javax.swing.*;

public class PCSelectionPanel extends JPanel {

    private ScreenNavigator navigator;
    private String selectedPaketName = "";
    private JPanel gridPanel;

    private JPanel[] pcBoxes;
    private JLabel[] statusLabels;
    private Timer refreshTimer;

    private String currentUsername;

    public PCSelectionPanel(ScreenNavigator nav) {
        this.navigator = nav;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel lblTitle = new JLabel("Daftar Station", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 4, 20, 20));
        gridPanel.setBackground(Color.BLACK);

        int pcCount = WarnetDataStore.getPcCount();
        pcBoxes = new JPanel[pcCount + 1];
        statusLabels = new JLabel[pcCount + 1];

        for (int stationNumber = 1; stationNumber <= pcCount; stationNumber++) {

            final int pcNumber = stationNumber;

            JPanel pcBox = new JPanel(new BorderLayout());
            pcBox.setBackground(new Color(46, 204, 113));
            pcBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

            JLabel lblNumber = new JLabel(String.valueOf(pcNumber), SwingConstants.CENTER);
            lblNumber.setFont(new Font("Arial", Font.BOLD, 28));
            lblNumber.setForeground(Color.BLACK);
            pcBox.add(lblNumber, BorderLayout.CENTER);

            JLabel lblStatus = new JLabel("Available", SwingConstants.CENTER);
            lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
            lblStatus.setForeground(Color.WHITE);
            pcBox.add(lblStatus, BorderLayout.SOUTH);

            pcBoxes[pcNumber] = pcBox;
            statusLabels[pcNumber] = lblStatus;

            pcBox.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (WarnetDataStore.isPcBusy(pcNumber)) {
                        long sisa = WarnetDataStore.getRemainingMillisForPc(pcNumber);
                        JOptionPane.showMessageDialog(
                                PCSelectionPanel.this,
                                "Station " + pcNumber + " sedang dipakai.\n"
                                + "Sisa waktu: " + formatTime(sisa),
                                "Informasi",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    if (currentUsername != null && !currentUsername.isEmpty()) {
                        if (WarnetDataStore.userHasActiveSession(currentUsername)) {
                            int station = WarnetDataStore.getUserActiveStation(currentUsername);
                            long sisaUser = WarnetDataStore.getUserRemainingMillis(currentUsername);
                            JOptionPane.showMessageDialog(
                                    PCSelectionPanel.this,
                                    "Anda sudah memesan Station " + station
                                    + " dan masih ada sisa waktu " + formatTime(sisaUser) + ".",
                                    "Tidak dapat memesan",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }
                    }

                    navigator.goToPaket(pcNumber);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!WarnetDataStore.isPcBusy(pcNumber)) {
                        pcBox.setBackground(new Color(52, 152, 219));
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!WarnetDataStore.isPcBusy(pcNumber)) {
                        pcBox.setBackground(new Color(46, 204, 113));
                    }
                }
            });

            gridPanel.add(pcBox);
        }

        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.BLACK);

        JButton btnBack = new JButton("Kembali");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setBackground(new Color(52, 73, 94));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> navigator.goHome());
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshTimer = new Timer(1000, e -> refreshPCStatus());
        refreshTimer.start();
        refreshPCStatus();
    }

    public void setCurrentUser(String username) {
        this.currentUsername = username;
    }

    public void setSelectedPaket(String paketName) {
        this.selectedPaketName = paketName;
    }

    private long getDurasiFromPaket(String paket) {
        if (paket == null) {
            return 1 * 60 * 60 * 1000;
        }

        switch (paket) {
            case "Paket Bronze (1 Jam)":
                return 1L * 60 * 60 * 1000;
            case "Paket Silver (3 Jam)":
                return 3L * 60 * 60 * 1000;
            case "Paket Gold (5 Jam)":
                return 5L * 60 * 60 * 1000;
            default:
                return 1L * 60 * 60 * 1000;
        }
    }

    public void initializeGrid() {
        refreshPCStatus();
    }

    public void refreshPCStatus() {
        int pcCount = WarnetDataStore.getPcCount();
        for (int stationNumber = 1; stationNumber <= pcCount; stationNumber++) {
            JPanel box = pcBoxes[stationNumber];
            JLabel label = statusLabels[stationNumber];
            if (box == null || label == null) {
                continue;
            }

            boolean busy = WarnetDataStore.isPcBusy(stationNumber);
            long remaining = WarnetDataStore.getRemainingMillisForPc(stationNumber);

            if (currentUsername != null && !currentUsername.isEmpty()) {
                int userStation = WarnetDataStore.getUserActiveStation(currentUsername);
                if (userStation == stationNumber) {
                    busy = true;
                    remaining = WarnetDataStore.getUserRemainingMillis(currentUsername);
                }
            }

            if (busy) {
                box.setBackground(new Color(192, 57, 43));
                label.setText("In Use - " + formatTime(remaining));
                label.setForeground(Color.YELLOW);
            } else {
                box.setBackground(new Color(46, 204, 113));
                label.setText("Available");
                label.setForeground(Color.WHITE);
            }
        }
    }

    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}