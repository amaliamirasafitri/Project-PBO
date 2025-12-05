import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;

public class PaketPanel extends JPanel {
    private ScreenNavigator navigator;
    private int selectedPC = -1;

    public PaketPanel(ScreenNavigator nav) {
        this.navigator = nav;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Pilihan Paket", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(Color.BLACK);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        List<DatabaseConnection.PaketData> paketDataList = DatabaseConnection.getAllPaket();

        for (DatabaseConnection.PaketData pd : paketDataList) {
            cards.add(createCard(pd));
        }

        add(cards, BorderLayout.CENTER);
    }

    private JPanel createCard(DatabaseConnection.PaketData pd) {
        RoundedPanel card = new RoundedPanel(14, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel(pd.namaPaket);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel lblShort = new JLabel(pd.deskripsiSingkat);
        lblShort.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextArea featuresArea = new JTextArea(pd.fitur);
        featuresArea.setEditable(false);
        featuresArea.setOpaque(false);
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(lblShort, BorderLayout.NORTH);
        center.add(featuresArea, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JLabel lblPrice = new JLabel(pd.harga);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrice.setForeground(new Color(52, 152, 219));
        bottom.add(lblPrice, BorderLayout.WEST);

        JButton btn = new JButton("Booking Paket");
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

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

    private static class RoundedPanel extends JPanel {

        private int radius;
        private Color bg;

        RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.setColor(new Color(220, 220, 220));
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

}