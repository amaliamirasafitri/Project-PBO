import javax.swing.JLabel;
import javax.swing.JPanel;

public class CodePanel extends JPanel {

    private ScreenNavigator navigator;
    private JLabel lbCodeDisplay;
    private String activeCode;

    public CodePanel(ScreenNavigator nav) {
        this.navigator = nav;
    }

    public void showCode(String paketName, int pcIdx) {
    }

    private String generateRandomTiket() {
        return "";
    }

    private void handleBackToHome() {
    }
}