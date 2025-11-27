import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.util.Map;

public class WarnetFrame extends JFrame implements ScreenNavigator {

    private CardLayout cards;
    private JPanel root;
    private JPanel homePanel;
    private PCSelectionPanel pcSelectionPage;
    private PaymentPanel paymentPage;
    private CodePanel codePage;
    private Map<String, Long> paketDurasi;

    public WarnetFrame() {
    }

    @Override
    public void goHome() {
    }

    @Override
    public void goToPCSelection(String paketName) {
    }

    @Override
    public void goToPayment(String paketName, int pcIndex, long durMillis) {
    }

    @Override
    public void showCodeThenBack(String paketName, int pcIndex, long durMillis) {
    }
}