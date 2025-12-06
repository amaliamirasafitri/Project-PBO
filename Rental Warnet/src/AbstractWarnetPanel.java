import java.awt.*;
import javax.swing.*;

/**
 * AbstractWarnetPanel
 * -------------------
 * Kelas dasar untuk semua panel utama sistem billing warnet.
 * Menyimpan referensi ScreenNavigator dan beberapa util tampilan
 * (warna, font, helper membuat title).
 */
public abstract class AbstractWarnetPanel extends JPanel {

    protected final ScreenNavigator navigator;

    // Konstanta warna & font yang sering dipakai
    protected static final Color BG_DARK     = new Color(20, 25, 35);
    protected static final Color NEON_CYAN   = new Color(0, 255, 255);
    protected static final Color TEXT_SOFT   = new Color(150, 200, 255);
    protected static final Font  TITLE_FONT  = new Font("Arial", Font.BOLD, 28);
    protected static final Font  TEXT_FONT   = new Font("Arial", Font.PLAIN, 14);

    protected AbstractWarnetPanel(ScreenNavigator navigator) {
        this.navigator = navigator;
        setBackground(BG_DARK);
        setLayout(new BorderLayout());
    }

    /** 
     * Setiap panel bisa mengembalikan judul utama untuk keperluan laporan / debugging.
     * Karena method ini abstract, otomatis membuat kelas ini jadi abstract class.
     */
    public abstract String getPanelTitle();

    /** Helper untuk membuat label judul standar warna neon */
    protected JLabel createTitleLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(TITLE_FONT);
        lbl.setForeground(NEON_CYAN);
        return lbl;
    }

    /** Helper untuk meletakkan komponen di tengah layar dengan GridBagLayout transparan */
    protected JPanel createCenteredWrapper(Component inner) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(inner, new GridBagConstraints());
        return wrapper;
    }
}
