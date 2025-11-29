
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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

        // Use a 2x2 grid so paket cards always fit the screen and scale with the window
        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(Color.BLACK);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        // Paket definitions (features use HTML lists for nicer rendering)
        Paket[] paketList = new Paket[]{
            new Paket("Paket Bronze (1 Jam)", "Cocok buat kamu yang cuma ingin main sebentar atau sekadar mengisi waktu luang.", "• 1 jam bermain\n• Bebas pilih game\n• Kontrol PC\n• Headset & sofa nyaman", "Rp12.000", 1),
            new Paket("Paket Silver (3 Jam)", "Pilihan pas untuk sesi bermain lebih lama tanpa khawatir waktu cepat habis.", "• 3 jam bermain\n• Game kapan saja\n• Konsol & mesin ringan\n• WIFI gratis & ruangan ber-AC", "Rp30.000", 3),
            new Paket("Paket Gold (5 Jam)", "Buat kamu yang mau puas main game favorit tanpa batas waktu yang mepet!", "• 5 jam bermain\n• Akses semua game premium\n• 1 minuman gratis\n• Prioritas room", "Rp50.000", 5),
            new Paket("Paket Malam", "Spesial untuk gamer malam yang mau suasana tenang dan bebas ngetes.", "• Bermain nonstop jam 00:00-06:00\n• Snack & minuman gratis\n• Ruang nyaman dengan lampu redup\n• Diskon untuk member", "Rp60.000", 8)
        };

        for (Paket p : paketList) {
            cards.add(createCard(p));
        }

        add(cards, BorderLayout.CENTER);
    }

    private JPanel createCard(Paket p) {
        RoundedPanel card = new RoundedPanel(14, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Title
        JLabel lblTitle = new JLabel(p.name);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Description: short label + features as plain multiline text
        JLabel lblShort = new JLabel(p.shortDesc);
        lblShort.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextArea featuresArea = new JTextArea(p.features);
        featuresArea.setEditable(false);
        featuresArea.setOpaque(false);
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(lblShort, BorderLayout.NORTH);
        center.add(featuresArea, BorderLayout.CENTER);

        // Bottom: price and button
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JLabel lblPrice = new JLabel(p.price);
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
                JOptionPane.showMessageDialog(this, "Pilih station PC terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            long dur = durationFromHours(p.hours);
            navigator.goToPayment(p.name, selectedPC, dur);
        });

        JPanel btnWrap = new JPanel();
        btnWrap.setOpaque(false);
        btnWrap.add(btn);

        bottom.add(btnWrap, BorderLayout.EAST);

        // Put title in a small header panel to give it some left padding
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

    public void setSelectedPC(int pcIndex) {
        this.selectedPC = pcIndex;
    }

    private static class Paket {

        String name;
        String shortDesc;
        String features;
        String price;
        int hours;

        Paket(String name, String shortDesc, String features, String price, int hours) {
            this.name = name;
            this.shortDesc = shortDesc;
            this.features = features;
            this.price = price;
            this.hours = hours;
        }
    }

    // Rounded white card panel
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
