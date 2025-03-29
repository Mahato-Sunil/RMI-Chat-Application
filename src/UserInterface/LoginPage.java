package UserInterface;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    // define the variables
    JLabel heading;
    JLabel imageLabel;
    JLabel title1;
    JLabel title2;
    JTextField usernameInptField;
    JButton registerBtn;

    Font customFont = new Font("Courier New", Font.BOLD, 18);

    public LoginPage() {
        initUIComponents();
    }

    private void initUIComponents() {
        heading = new JLabel("Welcome to Chat Application");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        heading.setFont(new Font("Courier New", Font.BOLD, 24));

        ImageIcon icon = new ImageIcon("Images/login-page-image.png", "Login Page Image");
        Image image = icon.getImage();
        Image scaledImaged = image.getScaledInstance(400, 510, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(scaledImaged);
        imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        // using the gridbaglayout
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();  // define the grid bag constraints
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1;

        title1 = new JLabel("Begin Chatting As ...", JLabel.CENTER);
        title1.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(title1, gbc);

        title2 = new JLabel("Username :", JLabel.LEFT);
        title2.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(title2, gbc);

        usernameInptField = new JTextField();
        usernameInptField.setFont(customFont);
        usernameInptField.setHorizontalAlignment(SwingConstants.CENTER);
        usernameInptField.setPreferredSize(new Dimension(300, 50));
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(usernameInptField, gbc);

        registerBtn = new JButton("Start Chatting");
        registerBtn.setFont(customFont);
        registerBtn.setHorizontalAlignment(SwingConstants.CENTER);
        registerBtn.setFocusable(false);
        registerBtn.setPreferredSize(new Dimension(200, 50));
        registerBtn.addActionListener(e -> initRegisterBtnEvent(this));
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(registerBtn, gbc);

        add(heading, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.WEST);
        add(loginPanel, BorderLayout.CENTER);

        setTitle("Chat Application");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private void initRegisterBtnEvent(JFrame frame) {
        // get the text of the input field
        String username = usernameInptField.getText();
        //validation
        if (username.isEmpty()) JOptionPane.showMessageDialog(frame, "Please Enter a Username First");
        else if (username.length() > 15) JOptionPane.showMessageDialog(frame, "Username is too long");
        else {
            dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    new SystemUI(username);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
