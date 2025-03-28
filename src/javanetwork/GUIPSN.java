package javanetwork;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class GUIPSN extends JFrame {
    private PSNUsers psn;
    private JTextField usernameField;
    private JTextField gameField;
    private JTextField trophyField;
    private JComboBox<Trophy> trophyTypeCombo;
    private JTextArea outputArea;

    public GUIPSN() {
        super("PSN Users Management System");
        initialize();
        setupUI();
    }

    private void initialize() {
        try {
            psn = new PSNUsers();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error initializing PSN system: " + e.getMessage(),
                                      "Initialization Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Game:"));
        gameField = new JTextField();
        inputPanel.add(gameField);

        inputPanel.add(new JLabel("Trophy Name:"));
        trophyField = new JTextField();
        inputPanel.add(trophyField);

        inputPanel.add(new JLabel("Trophy Type:"));
        trophyTypeCombo = new JComboBox<>(Trophy.values());
        inputPanel.add(trophyTypeCombo);

        JButton addUserBtn = new JButton("Add User");
        addUserBtn.addActionListener(e -> addUser());
        inputPanel.add(addUserBtn);

        JButton addTrophyBtn = new JButton("Add Trophy");
        addTrophyBtn.addActionListener(e -> addTrophy());
        inputPanel.add(addTrophyBtn);

        JButton deactivateBtn = new JButton("Deactivate User");
        deactivateBtn.addActionListener(e -> deactivateUser());
        inputPanel.add(deactivateBtn);

        JButton infoBtn = new JButton("Show Info");
        infoBtn.addActionListener(e -> showPlayerInfo());
        inputPanel.add(infoBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void addUser() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        try {
            psn.addUsers(username);
            outputArea.append("User '" + username + "' added successfully.\n");
        } catch (IOException e) {
            showError("Error adding user: " + e.getMessage());
        }
    }

    private void addTrophy() {
        String username = usernameField.getText().trim();
        String game = gameField.getText().trim();
        String trophy = trophyField.getText().trim();
        Trophy type = (Trophy) trophyTypeCombo.getSelectedItem();

        if (username.isEmpty() || game.isEmpty() || trophy.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        try {
            psn.addTrophyTo(username, game, trophy, type);
            outputArea.append("Trophy '" + trophy + "' added to user '" + username + "'.\n");
        } catch (IOException e) {
            showError("Error adding trophy: " + e.getMessage());
        }
    }

    private void deactivateUser() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        try {
            psn.deactivateUser(username);
            outputArea.append("User '" + username + "' deactivated.\n");
        } catch (IOException e) {
            showError("Error deactivating user: " + e.getMessage());
        }
    }

    private void showPlayerInfo() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        try {
            outputArea.append("\n=== Player Info for '" + username + "' ===\n");
            psn.playerInfo(username, outputArea);
            outputArea.append("=================================\n");
        } catch (IOException e) {
            showError("Error retrieving player info: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIPSN gui = new GUIPSN();
            gui.setVisible(true);
        });
    }
}
