public interface ScreenNavigator {

    void goHome();
    void goToLogin();
    void goToPCSelection(String paketName);
    void goToRegister();
    void goToPayment(String paketName, int pcIndex, long durMillis);
    void showCodeThenBack(String paketName, int pcIndex, long durMillis);
    void goToMainMenu();
    void goToGameList();
}
