import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

public class GameListPanel extends JPanel {

    private final ScreenNavigator navigator;

    public GameListPanel(ScreenNavigator navigator) {
        this.navigator = navigator;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 10, 20));

        JLabel title = new JLabel("Daftar Game", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 24));
        grid.setBackground(new Color(10, 10, 20));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        GameApp[] games = WarnetDataStore.getGames();
        for (GameApp game : games) {
            grid.add(createGameCard(game));
        }

        JButton backBtn = new JButton("Kembali ke Menu");
        backBtn.addActionListener(e -> navigator.goHome());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(new Color(10, 10, 20));
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createGameCard(GameApp game) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 260));
        card.setBackground(new Color(25, 25, 40));
        card.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel cover = new JLabel("", SwingConstants.CENTER);
        if (game.imagePath != null && !game.imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(game.imagePath);
                Image scaled = icon.getImage()
                                   .getScaledInstance(160, 200, Image.SCALE_SMOOTH);
                cover.setIcon(new ImageIcon(scaled));
            } catch (Exception ex) {
                cover.setText("No Image");
                cover.setForeground(Color.LIGHT_GRAY);
            }
        } else {
            cover.setText("No Image");
            cover.setForeground(Color.LIGHT_GRAY);
        }

        JLabel name = new JLabel(game.title, SwingConstants.CENTER);
        name.setForeground(Color.WHITE);
        name.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        card.add(cover, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            Color base = card.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(base.brighter());
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(base);
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
        dialog.getContentPane().setBackground(new Color(10, 10, 20));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.setBackground(new Color(10, 10, 20));

        JLabel cover = new JLabel();
        cover.setHorizontalAlignment(SwingConstants.CENTER);
        if (game.imagePath != null && !game.imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(game.imagePath);
                Image scaled = icon.getImage()
                                   .getScaledInstance(220, 320, Image.SCALE_SMOOTH);
                cover.setIcon(new ImageIcon(scaled));
            } catch (Exception ex) {
            }
        }
        cover.setPreferredSize(new Dimension(240, 340));
        content.add(cover, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(10, 10, 20));

        JLabel titleLabel = new JLabel(
                "<html><div style='width:320px;'><b>" + game.title + "</b></div></html>"
        );
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel meta = new JLabel("<html>Genre: " + game.genre +
                "<br/>Year: " + game.year +
                "<br/>Developer: " + game.developer + "</html>");
        meta.setForeground(Color.LIGHT_GRAY);
        meta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea desc = new JTextArea(game.description);
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setOpaque(false);
        desc.setForeground(Color.WHITE);
        desc.setBorder(null);

        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setBorder(null);
        descScroll.setOpaque(false);
        descScroll.getViewport().setOpaque(false);
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setPreferredSize(new Dimension(360, 180));

        right.add(titleLabel);
        right.add(Box.createVerticalStrut(8));
        right.add(meta);
        right.add(Box.createVerticalStrut(8));
        right.add(descScroll);

        content.add(right, BorderLayout.CENTER);

        dialog.add(content, BorderLayout.CENTER);

        // Tombol bawah
        JButton imdbBtn = new JButton("View on Steam");
        imdbBtn.addActionListener(e -> openUrl(game.imdbUrl));
        if (game.imdbUrl == null || game.imdbUrl.isEmpty()) {
            imdbBtn.setEnabled(false);
        }

        JButton closeBtn = new JButton("Tutup");
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(10, 10, 20));
        bottom.add(imdbBtn);
        bottom.add(closeBtn);

        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setSize(750, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openUrl(String url) {
        if (url == null || url.isEmpty()) return;
        if (!Desktop.isDesktopSupported()) return;

        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal membuka browser: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
