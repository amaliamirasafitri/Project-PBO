
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PaketPanel extends JPanel {

    private ScreenNavigator navigator;
    private int selectedPC = -1;

    public PaketPanel(ScreenNavigator nav) {
        this.navigator = nav;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 35));

        // Top panel dengan title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 25, 35));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Pilihan Paket", SwingConstants.CENTER);
        title.setForeground(new Color(0, 255, 255));
        title.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Cards panel
        JPanel cards = new JPanel(new GridLayout(2, 2, 25, 25));
        cards.setBackground(new Color(20, 25, 35));
        cards.setBorder(new EmptyBorder(20, 40, 40, 40));

        List<DatabaseConnection.PaketData> paketDataList = DatabaseConnection.getAllPaket();

        for (DatabaseConnection.PaketData pd : paketDataList) {
            cards.add(createCard(pd));
        }

        add(cards, BorderLayout.CENTER);
    }

    private JPanel createCard(DatabaseConnection.PaketData pd) {
        NeonCard card = new NeonCard();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(20, 18, 20, 18));

        // Title
        JLabel lblTitle = new JLabel(pd.namaPaket);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 255, 255));
        lblTitle.setBorder(new EmptyBorder(0, 0, 5, 0));

        // Short description
        JLabel lblShort = new JLabel(pd.deskripsiSingkat);
        lblShort.setFont(new Font("Arial", Font.PLAIN, 12));
        lblShort.setForeground(new Color(200, 200, 200));

        // Features
        JTextArea featuresArea = new JTextArea(pd.fitur);
        featuresArea.setEditable(false);
        featuresArea.setOpaque(false);
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setFont(new Font("Arial", Font.PLAIN, 11));
        featuresArea.setForeground(new Color(180, 180, 180));

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(lblShort, BorderLayout.NORTH);
        center.add(featuresArea, BorderLayout.CENTER);

        // Bottom panel with price and button
        JPanel bottom = new JPanel(new BorderLayout(15, 0));
        bottom.setOpaque(false);

        JLabel lblPrice = new JLabel(pd.harga);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrice.setForeground(new Color(100, 255, 150));
        bottom.add(lblPrice, BorderLayout.WEST);

        JButton btn = new JButton("BOOKING");
        btn.setBackground(new Color(0, 150, 200));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 220, 255), 2));
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (selectedPC <= 0) {
                JOptionPane.showMessageDialog(PaketPanel.this, "Pilih station PC terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            final int pcId = selectedPC;
            final long dur = durationFromHours(pd.durasi);
            final int hargaInt = parseHargaToInt(pd.harga);
            final String kodeAcak = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

            btn.setEnabled(false);

            SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
                private Exception error = null;

                @Override
                protected Integer doInBackground() {
                    try {
                        int idTrx = DatabaseConnection.createTransaksi(null, pcId, pd.paketId, kodeAcak, null, dur, hargaInt);
                        if (idTrx > 0) {
                            DatabaseConnection.updatePcStatus(pcId, "in-use");
                        }
                        return idTrx;
                    } catch (Exception ex) {
                        this.error = ex;
                        return -1;
                    }
                }

                @Override
                protected void done() {
                    btn.setEnabled(true);
                    try {
                        int idTrx = get();
                        if (idTrx > 0) {
                            navigator.goToPayment(pd.namaPaket, pcId, dur);
                        } else {
                            String msg = "Gagal membuat transaksi. Coba lagi.";
                            if (error != null) {
                                msg += "\n" + error.getMessage();
                            }
                            JOptionPane.showMessageDialog(PaketPanel.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(PaketPanel.this, "Gagal membuat transaksi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
        });

        JPanel btnWrap = new JPanel();
        btnWrap.setOpaque(false);
        btnWrap.add(btn);

        bottom.add(btnWrap, BorderLayout.EAST);

        // Header panel
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(lblTitle, BorderLayout.WEST);

        card.add(header, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private long durationFromHours(int hours) {
        return (long) hours * 60L * 60L * 1000L;
    }

    private int parseHargaToInt(String hargaStr) {
        if (hargaStr == null) {
            return 0;
        }
        String digits = hargaStr.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setSelectedPC(int pcIndex) {
        this.selectedPC = pcIndex;
    }

    // Custom Panel dengan Border Neon untuk Card
    private static class NeonCard extends JPanel {

        private static final Color NEON_CYAN = new Color(0, 255, 255);

        NeonCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int radius = 12;

            // Background gelap
            g2d.setColor(new Color(30, 40, 50));
            g2d.fillRoundRect(0, 0, w, h, radius, radius);

            // Glow effect
            g2d.setColor(new Color(0, 150, 200, 60));
            g2d.setStroke(new BasicStroke(6));
            g2d.drawRoundRect(2, 2, w - 5, h - 5, radius, radius);

            // Border neon cyan
            g2d.setColor(NEON_CYAN);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(3, 3, w - 7, h - 7, radius, radius);
        }
    }
}
