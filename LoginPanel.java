import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private ScreenNavigator navigator;

    public LoginPanel(ScreenNavigator nav) {
        this.navigator = nav;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(40, 44, 52)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("LOGIN SISTEM WARNET");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(lblUser, gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblPass, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        btnLogin = new JButton("MASUK");
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);

        JButton btnRegister = new JButton("Belum punya akun? Daftar");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(Color.CYAN);
    
        gbc.gridy = 4; 
        add(btnRegister, gbc);

        btnLogin.addActionListener((ActionEvent e) -> performLogin());
        btnRegister.addActionListener(e -> navigator.goToRegister());
    }

    private void performLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (DatabaseConnection.validateLogin(user, pass)) {
            JOptionPane.showMessageDialog(this, "Login Sukses! Selamat Datang " + user);
            navigator.goHome();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal Masuk! Cek username/password", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

