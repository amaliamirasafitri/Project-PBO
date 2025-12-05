// Import untuk AWT (layout, warna, font, grafik)

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;

public class PaketPanel extends JPanel {

    // Navigator untuk berpindah layar (mis. ke layar pembayaran)
    private ScreenNavigator navigator;

    // Index PC/seat yang dipilih dari panel lain; -1 artinya belum ada
    private int selectedPC = -1;

    /**
     * Konstruktor menerima ScreenNavigator untuk navigasi antar-layar.
     */
    public PaketPanel(ScreenNavigator nav) {
        this.navigator = nav;

        // Layout utama: judul di utara, kartu paket di tengah
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // background panel utama

        // Judul besar di atas
        JLabel title = new JLabel("Pilihan Paket", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Panel yang menampung kartu paket, diatur grid 2x2
        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(Color.BLACK);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        // Ambil data paket dari database menggunakan DatabaseConnection
        List<DatabaseConnection.PaketData> paketDataList = DatabaseConnection.getAllPaket();

        // Buat kartu UI untuk tiap paket dari database dan tambahkan ke grid
        for (DatabaseConnection.PaketData pd : paketDataList) {
            cards.add(createCard(pd));
        }

        add(cards, BorderLayout.CENTER);
    }

    private JPanel createCard(DatabaseConnection.PaketData pd) {
        // RoundedPanel adalah custom JPanel yang menggambar border melengkung
        RoundedPanel card = new RoundedPanel(14, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Judul paket
        JLabel lblTitle = new JLabel(pd.namaPaket);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Deskripsi singkat
        JLabel lblShort = new JLabel(pd.deskripsiSingkat);
        lblShort.setFont(new Font("Arial", Font.PLAIN, 12));

        // Area teks untuk menampilkan daftar fitur (non-editable)
        JTextArea featuresArea = new JTextArea(pd.fitur);
        featuresArea.setEditable(false);
        featuresArea.setOpaque(false);
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false); // biarkan background rounded panel terlihat
        center.add(lblShort, BorderLayout.NORTH);
        center.add(featuresArea, BorderLayout.CENTER);

        // Bagian bawah berisi harga di kiri dan tombol booking di kanan
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JLabel lblPrice = new JLabel(pd.harga);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrice.setForeground(new Color(52, 152, 219)); // warna aksen
        bottom.add(lblPrice, BorderLayout.WEST);

        // Tombol booking
        JButton btn = new JButton("Booking Paket");
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Action: jalankan pembuatan transaksi di background (doInBackground) supaya UI tidak freeze
        btn.addActionListener(e -> {
            if (selectedPC <= 0) {
                // Jika belum ada PC yang dipilih, tunjukkan dialog informasi
                JOptionPane.showMessageDialog(PaketPanel.this, "Pilih station PC terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Siapkan nilai yang akan dipakai di background thread
            final int pcId = selectedPC;
            final long dur = durationFromHours(pd.durasi);
            final int hargaInt = parseHargaToInt(pd.harga);
            final String kodeAcak = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

            // Non-aktifkan tombol sementara tugas background berjalan
            btn.setEnabled(false);

            // SwingWorker menjalankan doInBackground() di thread worker, dan done() di EDT
            SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
                private Exception error = null;

                @Override
                protected Integer doInBackground() {
                    try {
                        // Panggil helper DB yang seharusnya membuat dan menutup koneksi sendiri
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
                    // Re-enable tombol dan tangani hasil (done dijalankan di EDT)
                    btn.setEnabled(true);
                    try {
                        int idTrx = get(); // kalau doInBackground melempar, get() akan bungkusnya
                        if (idTrx > 0) {
                            // Navigasi ke layar pembayaran (EDT)
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

            // Jalankan worker
            worker.execute();
        });

        JPanel btnWrap = new JPanel();
        btnWrap.setOpaque(false);
        btnWrap.add(btn);

        bottom.add(btnWrap, BorderLayout.EAST);

        // Header sederhana untuk nama paket
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Mengonversi jam ke milidetik. Digunakan untuk menyampaikan durasi ke
     * layar pembayaran.
     */
    private long durationFromHours(int hours) {
        return (long) hours * 60L * 60L * 1000L;
    }

    /**
     * Parse harga yang ditampilkan seperti "Rp12.000" menjadi integer (12000).
     */
    private int parseHargaToInt(String hargaStr) {
        if (hargaStr == null) {
            return 0;
        }
        // Hapus semua karakter non-digit
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

    /**
     * Setter yang dipanggil dari luar untuk menginformasikan index PC/station
     * yang dipilih.
     */
    public void setSelectedPC(int pcIndex) {
        this.selectedPC = pcIndex;
    }

    /**
     * Custom JPanel yang menggambar background ber-radius. Digunakan untuk
     * membuat "card" dengan sudut melengkung.
     */
    private static class RoundedPanel extends JPanel {

        private int radius;
        private Color bg;

        RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false); // supaya kita bisa menggambar sendiri background
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Gunakan Graphics2D untuk anti-aliasing dan menggambar RoundRectangle2D
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Isi background bulat
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            // Gambar border tipis
            g2.setColor(new Color(220, 220, 220));
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
            // Panggil super setelah menggambar background custom supaya komponen child tetap tampil
            super.paintComponent(g);
        }
    }

}
