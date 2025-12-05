
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WarnetFrame extends JFrame implements ScreenNavigator {

    private CardLayout cards;
    private JPanel root;
    private JPanel homePanel;
    private PCSelectionPanel pcSelectionPage;
    private PaketPanel paketPage;
    private PaymentPanel paymentPage;
    private CodePanel codePage;
    private RegisterPanel registerPage;
    private LoginPanel loginPage;
    private MainMenuPanel mainMenuPanel;
    private GameListPanel gameListPage;

    private String currentUsername;

    public WarnetFrame() {
        super("SISTEM BILLING WARNET");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        cards = new CardLayout();
        root = new JPanel(cards);

        loginPage = new LoginPanel(this);
        registerPage = new RegisterPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        homePanel = new JPanel();
        pcSelectionPage = new PCSelectionPanel(this);
        paketPage = new PaketPanel(this);
        paymentPage = new PaymentPanel(this);
        codePage = new CodePanel(this);
        gameListPage = new GameListPanel(this);

        root.add(loginPage, "LOGIN");
        root.add(registerPage, "REGISTER");
        root.add(mainMenuPanel, "MAIN_MENU");
        root.add(homePanel, "HOME");
        root.add(pcSelectionPage, "PC_SELECT");
        root.add(paketPage, "PAKET");
        root.add(paymentPage, "PAYMENT");
        root.add(codePage, "CODE");
        root.add(gameListPage, "GAME_LIST");
        add(root);

        WarnetDataStore.syncFromDatabase();

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
    public void goToPaket(int pcIndex) {
        paketPage.setSelectedPC(pcIndex);
        cards.show(root, "PAKET");
    }

    @Override
    public void goToPayment(String paketName, int pcIndex, long durMillis) {
        paymentPage.updateTransactionInfo(paketName, pcIndex, durMillis);
        cards.show(root, "PAYMENT");
    }

    @Override
    public void showCodeThenBack(String paketName, int pcIndex, long durMillis) {
        codePage.showCode(paketName, pcIndex);
        cards.show(root, "MAIN_MENU");
    }

    @Override
    public void goToGameList() {
        cards.show(root, "GAME_LIST");
    }

    @Override
    public void goToMainMenu() {
        cards.show(root, "MAIN_MENU");
    }

    @Override
    public void onLoginSuccess(String username) {
        this.currentUsername = username;

        pcSelectionPage.setCurrentUser(username);
        paymentPage.setCurrentUser(username);

        WarnetDataStore.syncFromDatabase();

        cards.show(root, "MAIN_MENU");
    }

    @Override
    public void onLogout() {
        currentUsername = null;
        cards.show(root, "LOGIN");
    }
}