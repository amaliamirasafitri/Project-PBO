import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PaymentPanel extends JPanel {

    public ScreenNavigator navigator;
    public String currentPaketName;
    private long durationMillis;
    private int targetPCIndex;
    private JTextField inputCash;
    private JLabel lbChange;

    public PaymentPanel(ScreenNavigator nav) {
        this.navigator = nav;
    }

    public void updateTransactionInfo(String paket, int pcIndex, long dur) {
    }

    private void processPayment() {
    }

    private void confirmBooking() {
    }
}