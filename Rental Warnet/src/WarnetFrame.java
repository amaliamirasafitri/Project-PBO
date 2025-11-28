import java.awt.CardLayout;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WarnetFrame extends JFrame implements ScreenNavigator {

    private CardLayout cards;
    private JPanel root;
    private JPanel homePanel;
    private PCSelectionPanel pcSelectionPage;
    private PaymentPanel paymentPage;
    private CodePanel codePage;
    private Map<String, Long> paketDurasi;
    private RegisterPanel registerPage; 
    private LoginPanel loginPage;
    private MainMenuPanel mainMenuPanel;
    private GameListPanel gameListPage;

    public WarnetFrame() {
        super("SISTEM BILLING WARNET");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cards = new CardLayout();
        root = new JPanel(cards); 

        loginPage = new LoginPanel(this);
        registerPage = new RegisterPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        homePanel = new JPanel(); 
        pcSelectionPage = new PCSelectionPanel(this);
        paymentPage = new PaymentPanel(this);
        codePage = new CodePanel(this);
        gameListPage = new GameListPanel(this);

        root.add(loginPage, "LOGIN");
        root.add(registerPage, "REGISTER");
        root.add(mainMenuPanel, "MAIN_MENU");
        root.add(homePanel, "HOME");
        root.add(pcSelectionPage, "PC_SELECT");
        root.add(paymentPage, "PAYMENT");
        root.add(codePage, "CODE");
        root.add(gameListPage, "GAME_LIST");

        add(root);
        cards.show(root, "LOGIN");
        setVisible(true);
    }

    @Override
    public void goHome() {
        cards.show(root, "MAIN_MENU");
    }

    @Override
    public void goToLogin() {
        cards.show(root, "LOGIN");
    }

    @Override
    public void goToRegister() {
        cards.show(root, "REGISTER");
    }

    @Override
    public void goToPCSelection(String paketName) {
        pcSelectionPage.setSelectedPaket(paketName);
        cards.show(root, "PC_SELECT");
    }

    @Override
    public void goToPayment(String paketName, int pcIndex, long durMillis) {
        paymentPage.updateTransactionInfo(paketName, pcIndex, durMillis);
        cards.show(root, "PAYMENT");
    }

    @Override
    public void showCodeThenBack(String paketName, int pcIndex, long durMillis) {
        codePage.showCode(paketName, pcIndex);
        cards.show(root, "CODE");
    }

    @Override
    public void goToGameList() {
        cards.show(root, "GAME_LIST");
    }

}
