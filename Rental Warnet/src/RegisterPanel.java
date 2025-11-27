import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
    private JTextField txtUser;
    private JPasswordField txtPass, txtConfirmPass;
    private JButton btnRegister, btnBack;
    private ScreenNavigator navigator;

    public RegisterPanel(ScreenNavigator nav) {
        this.navigator = nav;
        setLayout(new GridBagLayout());
        setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("DAFTAR AKUN BARU", SwingConstants.CENTER);
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        add(lblUser, gbc);

        txtUser = new JTextField(15);
        gbc.gridx = 1;
        add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        add(lblPass, gbc);

        txtPass = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblConf = new JLabel("Ulangi Pass:");
        lblConf.setForeground(Color.WHITE);
        add(lblConf, gbc);

        txtConfirmPass = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtConfirmPass, gbc);

        btnRegister = new JButton("DAFTAR SEKARANG");
        btnRegister.setBackground(new Color(0, 150, 0));
        btnRegister.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnRegister, gbc);

        btnBack = new JButton("Batal / Kembali Login");
        btnBack.setBackground(Color.RED);
        btnBack.setForeground(Color.WHITE);
        gbc.gridy = 5;
        add(btnBack, gbc);

        btnRegister.addActionListener(e -> performRegister());
        btnBack.addActionListener(e -> navigator.goToLogin());
    }

    private void performRegister() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        String conf = new String(txtConfirmPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return;
        }

        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Password konfirmasi tidak cocok!");
            return;
        }

        if (DatabaseConnection.registerUser(user, pass)) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
            navigator.goToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal Daftar! Username mungkin sudah ada.");
        }
    }
}
