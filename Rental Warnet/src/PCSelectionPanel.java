import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        setBackground(new Color(20, 25, 35));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 25, 35));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Pilih Station PC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 255, 255));
        topPanel.add(lblTitle, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 4, 20, 20));
        gridPanel.setBackground(new Color(20, 25, 35));
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        int pcCount = WarnetDataStore.getPcCount();
        pcBoxes = new JPanel[pcCount + 1];
        statusLabels = new JLabel[pcCount + 1];

        for (int stationNumber = 1; stationNumber <= pcCount; stationNumber++) {

            final int pcNumber = stationNumber;

            PCTilePanel pcBox = new PCTilePanel();
            pcBox.setLayout(new BorderLayout());
            pcBox.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));

            JLabel lblNumber = new JLabel(String.valueOf(pcNumber), SwingConstants.CENTER);
            lblNumber.setFont(new Font("Arial", Font.BOLD, 36));
            lblNumber.setForeground(Color.WHITE);
            pcBox.add(lblNumber, BorderLayout.CENTER);

            JLabel lblStatus = new JLabel("Available", SwingConstants.CENTER);
            lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
            lblStatus.setForeground(new Color(150, 255, 150));
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
                        pcBox.setBackground(new Color(30, 40, 50));
                        ((JLabel) pcBox.getComponent(0)).setForeground(new Color(0, 255, 255));
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!WarnetDataStore.isPcBusy(pcNumber)) {
                        pcBox.setBackground(new Color(40, 50, 60));
                        ((JLabel) pcBox.getComponent(0)).setForeground(Color.WHITE);
                    }
                }
            });

            gridPanel.add(pcBox);
        }

        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(20, 25, 35));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JButton btnBack = new JButton("KEMBALI");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setBackground(new Color(100, 60, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createLineBorder(new Color(200, 100, 100), 2));
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
                box.setBackground(new Color(100, 50, 50));
                label.setText("In Use - " + formatTime(remaining));
                label.setForeground(new Color(255, 150, 150));
            } else {
                box.setBackground(new Color(40, 50, 60));
                label.setText("Available");
                label.setForeground(new Color(150, 255, 150));
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

class PCTilePanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2d.setColor(new Color(0, 150, 200, 40));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(2, 2, w - 5, h - 5);
    }
}