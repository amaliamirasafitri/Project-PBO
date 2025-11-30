public interface ScreenNavigator {
    void goHome();
    void goToLogin();
    void goToRegister();
    void goToPCSelection(String paketName);
    void goToPaket(int pcIndex);
    void goToPayment(String paketName, int pcIndex, long durMillis);
    void showCodeThenBack(String paketName, int pcIndex, long durMillis);
    void goToGameList();
    void goToMainMenu();
    void onLoginSuccess(String username);
    void onLogout();
}