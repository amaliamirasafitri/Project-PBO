import java.awt.*;
import javax.swing.*; 

public class CodePanel extends JPanel { 
    private ScreenNavigator navigator; 
    private JLabel lbCodeDisplay; 
    private JLabel lbInfo; 
    private String activeCode; 

    public CodePanel(ScreenNavigator nav) { 
        this.navigator = nav; 
        setLayout(new GridBagLayout()); 
        setBackground(Color.BLACK); 
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        JLabel lblTitle = new JLabel("Kode Tiket Anda", SwingConstants.CENTER); 
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22)); 
        lblTitle.setForeground(Color.WHITE); gbc.gridy = 0; 
        add(lblTitle, gbc); 

        lbInfo = new JLabel("", SwingConstants.CENTER); 
        lbInfo.setFont(new Font("Arial", Font.PLAIN, 14)); 
        lbInfo.setForeground(Color.LIGHT_GRAY); 
        gbc.gridy = 1; add(lbInfo, gbc); 
        
        lbCodeDisplay = new JLabel("------", SwingConstants.CENTER); 
        lbCodeDisplay.setFont(new Font("Consolas", Font.BOLD, 40)); 
        lbCodeDisplay.setForeground(Color.GREEN); 
        lbCodeDisplay.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3)); 
        lbCodeDisplay.setPreferredSize(new Dimension(300, 100)); 
        lbCodeDisplay.setOpaque(true); 
        lbCodeDisplay.setBackground(Color.BLACK); 
        gbc.gridy = 2; add(lbCodeDisplay, gbc); 
         
        JButton btnDone = new JButton("Kembali ke Menu"); 
        btnDone.setBackground(new Color(52, 152, 219)); 
        btnDone.setForeground(Color.WHITE);
        btnDone.setFont(new Font("Arial", Font.BOLD, 16)); 
        btnDone.setFocusPainted(false); gbc.gridy = 3; 
        add(btnDone, gbc); 
        btnDone.addActionListener(e -> handleBackToHome()); 
    }

    public void showCode(String paketName, int pcIdx) { 
        this.activeCode = generateRandomTiket(); 
            
        lbInfo.setText("Paket: " + paketName + " • PC: " + pcIdx); 
             
        lbCodeDisplay.setText(activeCode); 
    } 
        
    private String generateRandomTiket() { 
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; 
        StringBuilder sb = new StringBuilder(); 
            
        for (int i = 0; i < 6; i++) { 
            sb.append(chars.charAt((int)(Math.random() * chars.length()))); 
        } 
        return sb.toString(); 
    } 
        
    private void handleBackToHome() { 
        navigator.goHome();
    }
}