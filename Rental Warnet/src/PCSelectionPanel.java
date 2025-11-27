import javax.swing.JPanel;

public class PCSelectionPanel extends JPanel {

    private ScreenNavigator navigator;
    private String selectedPaketName;
    private JPanel gridPanel;

    public PCSelectionPanel(ScreenNavigator nav) {
        this.navigator = nav;
    }

    public void initializeGrid() {
    }

    public void refreshPCStatus() {
    }
}