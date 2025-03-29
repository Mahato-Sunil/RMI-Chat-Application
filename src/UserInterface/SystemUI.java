package UserInterface;

import Utils.ChatClient;
import Utils.RemoteChatService;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SystemUI extends JFrame {
    // Define UI components
    JLabel heading, title1, title2, loggedLabel, msgHeading, msgInfo, sendBtn, leaveBtn, userLabel, refreshLabel, response, logoutBtn;
    JTextArea textArea, responseArea;
    JPanel availableUserPanel, msgPanel, mainPanel, userListPanel, logoutPanel;
    Font customFont = new Font("Courier New", Font.BOLD, 18);
    GridBagConstraints mainGbc;
    private ChatClient chatClient;
    private String ACTIVE_CHAT_USER = "";
    private String SENDER = "";

    public SystemUI(String username) {
        this.SENDER = username;
        initChatClient(username);
        initUIComponents(username);
        fetchActiveUsers();
    }

    private void initUIComponents(String username) {
        setTitle("Chat Application : " + username);
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());  // Set layout (Don't use null)

        heading = new JLabel("Start Chatting Anonymously", JLabel.CENTER);
        heading.setFont(new Font("Courier New", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Panel for Available Users
        availableUserPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        loggedLabel = new JLabel("Messaging As => " + username);
        loggedLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        loggedLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.DARK_GRAY));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        availableUserPanel.add(loggedLabel, gbc);

        title1 = new JLabel("Available Users ...", JLabel.LEFT);
        title1.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        availableUserPanel.add(title1, gbc);

        refreshLabel = new JLabel("Refresh Users", JLabel.CENTER);
        refreshLabel.setFont(customFont);
        refreshLabel.setPreferredSize(new Dimension(340, 45));
        refreshLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
        refreshLabel.setBackground(new Color(90, 225, 255));
        refreshLabel.setOpaque(true);
        gbc.gridx = 0;
        gbc.gridy = 2;
        availableUserPanel.add(refreshLabel, gbc);
        refreshLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refreshLabel.setBackground(new Color(127, 139, 142));
                fetchActiveUsers();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                refreshLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                refreshLabel.setBackground(new Color(90, 225, 255));
            }
        });

        title2 = new JLabel("Click to start chatting ...", JLabel.LEFT);
        title2.setFont(customFont);
        gbc.gridy = 3;
        availableUserPanel.add(title2, gbc);

        userListPanel = new JPanel();
        gbc.gridy = 4;
        availableUserPanel.add(userListPanel, gbc);

        // Messaging Panel
        msgPanel = new JPanel(new GridBagLayout());
        msgPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        GridBagConstraints msgGbc = new GridBagConstraints();
        msgGbc.insets = new Insets(10, 10, 10, 10);
        msgGbc.fill = GridBagConstraints.VERTICAL;
        msgGbc.anchor = GridBagConstraints.WEST;
        msgGbc.gridwidth = 1;

        msgHeading = new JLabel("Start Writing Text Messages", JLabel.CENTER);
        msgHeading.setFont(customFont);
        msgHeading.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
        msgGbc.gridx = 0;
        msgGbc.gridy = 0;
        msgPanel.add(msgHeading, msgGbc);

        // label for showing whom message is being sent
        msgInfo = new JLabel();
        msgInfo.setFont(new Font("Consolas", Font.PLAIN, 16));
        msgGbc.gridx = 0;
        msgGbc.gridy = 1;
        msgGbc.insets = new Insets(10, 10, 10, 10);
        msgPanel.add(msgInfo, msgGbc);

        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(350, 140));
        textArea.setFont(customFont);
        msgGbc.gridx = 0;
        msgGbc.gridy = 2;
        msgPanel.add(textArea, msgGbc);

        // response from another user
        response = new JLabel("Resposnse ==>");
        response.setFont(new Font("Consolas", Font.ITALIC, 14));
        response.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.DARK_GRAY));
        msgGbc.anchor = GridBagConstraints.WEST;
        msgGbc.gridx = 0;
        msgGbc.gridy = 3;
        msgPanel.add(response, msgGbc);

        responseArea = new JTextArea();
        responseArea.setPreferredSize(new Dimension(350, 130));
        responseArea.setFont(customFont);
        responseArea.setEditable(false);
        msgGbc.gridx = 0;
        msgGbc.gridy = 4;
        msgPanel.add(responseArea, msgGbc);

        sendBtn = new JLabel("Send =>", JLabel.CENTER);
        sendBtn.setFont(customFont);
        sendBtn.setPreferredSize(new Dimension(340, 45));
        sendBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
        sendBtn.setBackground(new Color(90, 255, 92));
        sendBtn.setOpaque(true);
        msgGbc.gridx = 0;
        msgGbc.gridy = 5;
        msgPanel.add(sendBtn, msgGbc);
        sendBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendBtn.setBackground(new Color(0, 87, 3));
                try {
                    sendMessage();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                sendBtn.setBackground(new Color(90, 255, 92));
            }
        });

        leaveBtn = new JLabel("Leave", JLabel.CENTER);
        leaveBtn.setFont(customFont);
        leaveBtn.setPreferredSize(new Dimension(340, 45));
        leaveBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
        leaveBtn.setBackground(new Color(255, 103, 103));
        leaveBtn.setOpaque(true);
        msgGbc.gridx = 0;
        msgGbc.gridy = 6;
        msgPanel.add(leaveBtn, msgGbc);
        leaveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leaveBtn.setBackground(new Color(170, 0, 0));
                leaveMessage();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                leaveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                leaveBtn.setBackground(new Color(255, 103, 103));
            }

        });

        // Main Panel
        mainPanel = new JPanel(new GridBagLayout());
        mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(10, 10, 10, 80);
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainPanel.add(availableUserPanel, mainGbc);

        mainGbc.insets = new Insets(10, 70, 10, 10);
        mainGbc.gridx = 1;
        mainPanel.add(msgPanel, mainGbc);

        // logout panel
        logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        logoutBtn = new JLabel("Log Out", JLabel.CENTER);
        logoutBtn.setFont(customFont);
        logoutBtn.setPreferredSize(new Dimension(240, 45));
        logoutBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
        logoutBtn.setBackground(new Color(132, 183, 246));
        logoutBtn.setOpaque(true);
        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                logoutBtn.setBackground(new Color(132, 183, 246));
                try {
                    logout();
                } catch (RemoteException e1) {
                    System.out.println(e1.getMessage());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                logoutBtn.setBackground(new Color(132, 183, 246));
            }

        });

        logoutPanel.add(logoutBtn);
        // Add components to frame
        add(heading, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(logoutPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // function to init the chat client
    private void initChatClient(String username) {
        try {
            chatClient = new ChatClient(username);

            // listen for messages in a separate  thread
//            new Thread(() -> {
            new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() throws Exception {
                    while (true) {
                        String newMessage = chatClient.getMessage(SENDER);
                        if (newMessage != null) publish(newMessage);

                        Thread.sleep(500);     // pooling interval
                    }
                }

                @Override
                protected void process(List<String> chunks) {
                    for (String newMessage : chunks)
                        SwingUtilities.invokeLater(() -> responseArea.setText(newMessage));
                }
            }.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing chat client: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //fetch existing users
    private void fetchActiveUsers() {
        if (chatClient == null) {
            JOptionPane.showMessageDialog(this, "Chat client is not initialized yet.");
            return;
        }
        List<String> userList = new ArrayList<>();
        new Thread(() -> {
            try {
                List<String> activeUsers = chatClient.getRemoteService().getActiveUsers();
                SwingUtilities.invokeLater(() -> {
                    for (String user : activeUsers) {
                        if (!user.equals(chatClient.getUsername())) {  // Exclude logged-in user
                            try {
                                chatClient.updateActiveUsers(activeUsers);
                                userList.add(user);
                            } catch (RemoteException rem) {
                                rem.getMessage();
                            }
                        }
                    }

                    //clear the previous components
                    userListPanel.removeAll();
                    for (String user : userList)
                        addUserLabel(user);
                    userListPanel.revalidate();
                    userListPanel.repaint();
                });
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void addUserLabel(String userName) {
        userLabel = new JLabel(userName, JLabel.CENTER);
        userLabel.setFont(customFont);
        userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        userLabel.setPreferredSize(new Dimension(340, 40));

        // calculate the  number of components
        int compLength = availableUserPanel.getComponents().length;

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = compLength + 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // get message Infor
                String msg = "Chatting with ==> " + userName;
                ACTIVE_CHAT_USER = userName;
                msgInfo.setText(msg);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        //create a container so that it can be removed and repainted

        userListPanel.add(userLabel);
        userListPanel.revalidate();
        userListPanel.repaint();
    }

    //function to leave the message box
    private void leaveMessage() {
        msgInfo.setText("");
        textArea.setText("");
        responseArea.setText("");
    }

    // function to send mesage
    private void sendMessage() throws RemoteException {
        RemoteChatService rcs = chatClient.getRemoteService();
        String message = textArea.getText().trim();
        fetchActiveUsers();
        if (ACTIVE_CHAT_USER == null || ACTIVE_CHAT_USER.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Msg Chai Bhoot lai Pathauxas 😡 \n Chat Partner ta roj Paile ***");
        } else if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ooi Ullu ka Pattha, Message ta Lekh Paile");
        } else {
            rcs.sendMessage(SENDER, ACTIVE_CHAT_USER, message);
            JOptionPane.showMessageDialog(this, "Message sent to " + ACTIVE_CHAT_USER + ": " + message);
        }
    }

    private void logout() throws RemoteException {
        chatClient.logout();
        SwingUtilities.invokeLater(LoginPage::new);
        dispose();
    }
}