import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;

public class PCTile extends JPanel {

    private final int pcIndex;
    private boolean isOccupied;
    private Timer refreshTimer;

    public PCTile(int index) {
        this.pcIndex = index;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void updateUIStatus() {
    }
}