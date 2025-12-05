
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.swing.border.EmptyBorder;

public class GameListPanel extends JPanel {

    private final ScreenNavigator navigator;

    public GameListPanel(ScreenNavigator navigator) {
        this.navigator = navigator;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 35));

        // Top panel dengan title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 25, 35));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("DAFTAR GAME", SwingConstants.CENTER);
        title.setForeground(new Color(0, 255, 255));
        title.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Panel wrapper untuk centering konten
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(new Color(20, 25, 35));

        centerWrapper.add(Box.createVerticalGlue());

        final JPanel grid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        grid.setBackground(new Color(20, 25, 35));
        grid.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.setBackground(new Color(20, 25, 35));
        scroll.getViewport().setBackground(new Color(20, 25, 35));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);

        // Custom scrollbar UI yang invisible tapi tetap berfungsi
        scroll.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(20, 25, 35);
                this.trackColor = new Color(20, 25, 35);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton btn = super.createDecreaseButton(orientation);
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton btn = super.createIncreaseButton(orientation);
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });

        centerWrapper.add(scroll);
        centerWrapper.add(Box.createVerticalGlue());

        add(centerWrapper, BorderLayout.CENTER);
        new Thread(() -> {
            GameApp[] games = WarnetDataStore.getGames();

            SwingUtilities.invokeLater(() -> {
                if (games == null || games.length == 0) {
                    JLabel empty = new JLabel("Belum ada data game di database.", SwingConstants.CENTER);
                    empty.setForeground(new Color(150, 150, 150));
                    grid.setLayout(new BorderLayout());
                    grid.add(empty, BorderLayout.CENTER);
                } else {
                    for (GameApp game : games) {
                        grid.add(createGameCard(game));
                    }
                }
                grid.revalidate();
                grid.repaint();
            });
        }, "Load-Games-Thread").start();

        // Bottom panel
        JButton backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(100, 60, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 100, 100), 2));
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setPreferredSize(new Dimension(150, 40));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> navigator.goHome());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(new Color(20, 25, 35));
        bottom.setBorder(new EmptyBorder(10, 10, 20, 10));
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createGameCard(GameApp game) {
        GameCardPanel card = new GameCardPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 260));

        JLabel cover = new JLabel("", SwingConstants.CENTER);
        Icon icon = loadGameImage(game.imagePath, 160, 200);
        if (icon != null) {
            cover.setIcon(icon);
        } else {
            cover.setText("No Image");
            cover.setForeground(new Color(100, 100, 100));
            cover.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        cover.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 1));

        JLabel name = new JLabel(game.title, SwingConstants.CENTER);
        name.setForeground(new Color(0, 255, 255));
        name.setFont(new Font("Arial", Font.BOLD, 11));
        name.setBorder(new EmptyBorder(8, 4, 4, 4));

        card.add(cover, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(40, 60, 80));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(35, 45, 60));
                card.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showDetail(game);
            }
        });

        return card;
    }

    private void showDetail(GameApp game) {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                game.title,
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(20, 25, 35));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setBackground(new Color(20, 25, 35));

        JLabel cover = new JLabel();
        cover.setHorizontalAlignment(SwingConstants.CENTER);
        Icon detailIcon = loadGameImage(game.imagePath, 220, 320);
        if (detailIcon != null) {
            cover.setIcon(detailIcon);
        }
        cover.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));
        cover.setPreferredSize(new Dimension(240, 340));
        content.add(cover, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(20, 25, 35));

        JLabel titleLabel = new JLabel(
                "<html><div style='width:320px; color:#00FFFF;'><b>" + game.title + "</b></div></html>"
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel meta = new JLabel("<html><font color='#9999FF'>Genre: " + game.genre
                + "<br/>Year: " + game.year
                + "<br/>Developer: " + game.developer + "</font></html>");
        meta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea desc = new JTextArea(game.description);
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setOpaque(false);
        desc.setForeground(new Color(200, 200, 200));
        desc.setBorder(null);
        desc.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setBorder(null);
        descScroll.setOpaque(false);
        descScroll.getViewport().setOpaque(false);
        descScroll.getViewport().setBackground(new Color(20, 25, 35));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setPreferredSize(new Dimension(360, 180));

        right.add(titleLabel);
        right.add(Box.createVerticalStrut(8));
        right.add(meta);
        right.add(Box.createVerticalStrut(8));
        right.add(descScroll);

        content.add(right, BorderLayout.CENTER);

        dialog.add(content, BorderLayout.CENTER);

        JButton steamBtn = new JButton("View on Steam");
        steamBtn.setBackground(new Color(0, 150, 200));
        steamBtn.setForeground(Color.WHITE);
        steamBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 220, 255), 2));
        steamBtn.setFont(new Font("Arial", Font.BOLD, 12));
        steamBtn.setPreferredSize(new Dimension(120, 35));
        steamBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        steamBtn.addActionListener(e -> openUrl(game.imdbUrl));
        if (game.imdbUrl == null || game.imdbUrl.isEmpty()) {
            steamBtn.setEnabled(false);
        }

        JButton closeBtn = new JButton("TUTUP");
        closeBtn.setBackground(new Color(100, 60, 60));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 100, 100), 2));
        closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        closeBtn.setPreferredSize(new Dimension(120, 35));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(20, 25, 35));
        bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottom.add(steamBtn);
        bottom.add(closeBtn);

        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setSize(750, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private Icon loadGameImage(String imagePath, int targetW, int targetH) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return null;
        }

        String path = imagePath.trim();
        String userDir = System.getProperty("user.dir");

        File[] candidates = new File[]{
            new File(path),
            new File(userDir, path),
            new File(userDir, "Rental Warnet/" + path),
            new File(userDir, "Rental Warnet/Assets/"
            + new File(path).getName())
        };

        File found = null;
        for (File f : candidates) {
            System.out.println("Coba load gambar dari: '" + f.getAbsolutePath() + "'");
            if (f.exists() && f.isFile()) {
                found = f;
                break;
            } else {
                System.out.println("File gambar TIDAK ditemukan: '" + f.getAbsolutePath() + "'");
            }
        }

        if (found != null) {
            ImageIcon icon = new ImageIcon(found.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }

        java.net.URL res = GameListPanel.class.getClassLoader().getResource(path);
        if (res != null) {
            System.out.println("Gambar ditemukan sebagai resource: '" + path + "'");
            ImageIcon icon = new ImageIcon(res);
            Image scaled = icon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            System.out.println("Gambar tidak ditemukan sebagai resource: '" + path + "'");
        }

        return null;
    }

    private void openUrl(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal membuka browser: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Custom Panel untuk Game Card dengan Border Neon
class GameCardPanel extends JPanel {

    GameCardPanel() {
        setOpaque(false);
        setBackground(new Color(35, 45, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int radius = 8;

        // Background
        g2d.setColor(new Color(35, 45, 60));
        g2d.fillRoundRect(0, 0, w, h, radius, radius);

        // Glow effect
        g2d.setColor(new Color(0, 150, 200, 50));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, w - 3, h - 3, radius, radius);

        // Border
        g2d.setColor(new Color(0, 200, 255));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(2, 2, w - 5, h - 5, radius, radius);
    }
}
