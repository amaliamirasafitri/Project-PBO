import java.awt.*;
import javax.swing.*;

public class PCSelectionPanel extends JPanel {

    private ScreenNavigator navigator;
    private String selectedPaketName = "";
    private JPanel gridPanel;

    public PCSelectionPanel(ScreenNavigator nav) {
        this.navigator = nav;
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        // Judul
        JLabel lblTitle = new JLabel("Daftar Station", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);
        
        // Grid PC
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 4, 20, 20));
        gridPanel.setBackground(Color.BLACK);

        // Buat 12 Station PC
        for (int i = 1; i <= 12; i++) {

            final int pcNumber = i;

            JPanel pcBox = new JPanel(new BorderLayout());
            pcBox.setBackground(new Color(46, 204, 113));
            pcBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            
            JLabel lblNumber = new JLabel(String.valueOf(pcNumber), SwingConstants.CENTER);
            lblNumber.setFont(new Font("Arial", Font.BOLD, 28));
            pcBox.add(lblNumber, BorderLayout.CENTER);

            JLabel lblStatus = new JLabel("Available", SwingConstants.CENTER);
            lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
            pcBox.add(lblStatus, BorderLayout.SOUTH);

            // EVENT CLICK PC
            pcBox.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    long durasi = getDurasiFromPaket(selectedPaketName);
                    navigator.goToPayment(selectedPaketName, pcNumber, durasi);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    pcBox.setBackground(new Color(52, 152, 219));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    pcBox.setBackground(new Color(46, 204, 113));
                }
            });

            gridPanel.add(pcBox);
        }
        
        add(gridPanel, BorderLayout.CENTER);
    }

    // ⬅ Dipanggil dari WarnetFrame.goToPCSelection()
    public void setSelectedPaket(String paketName) {
        this.selectedPaketName = paketName;
    }

    // Durasi berdasarkan paket
    private long getDurasiFromPaket(String paket) {

        if (paket == null) return 1 * 60 * 60 * 1000;

        switch (paket) {
            case "Paket Bronze (1 Jam)":
                return 1L * 60 * 60 * 1000;

            case "Paket Silver (3 Jam)":
                return 3L * 60 * 60 * 1000;

            case "Paket Gold (5 Jam)":
                return 5L * 60 * 60 * 1000;

            default:
                return 1L * 60 * 60 * 1000; // default 1 jam
        }
    }

    public void initializeGrid() {}
    public void refreshPCStatus() {}

}
