package javanetwork;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class GUIPSN extends JFrame {

    private PSNUSers psn;
    private JTextField usernameField = new JTextField();
    private JTextField gameField = new JTextField();
    private JTextField trophyField = new JTextField();
    private JComboBox<Trophy> trophyTypeCombo = new JComboBox<>(Trophy.values());
    private JTextArea outputArea = new JTextArea();

    public GUIPSN() throws IOException {
        super("PSNetwork");
        initialize();
        setupUI();
    }
    
    private void initialize() throws IOException {
            psn = new PSNUSers();
       
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JPanel usernamePanel = new JPanel(new BorderLayout(5, 0));
        usernamePanel.add(new JLabel("Username:"), BorderLayout.WEST);
        usernameField = new JTextField();
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        inputPanel.add(usernamePanel);

        JPanel gamePanel = new JPanel(new BorderLayout(5, 0));
        gamePanel.add(new JLabel("Game:"), BorderLayout.WEST);
        gameField = new JTextField();
        gamePanel.add(gameField, BorderLayout.CENTER);
        inputPanel.add(gamePanel);

        JPanel trophyPanel = new JPanel(new BorderLayout(5, 0));
        trophyPanel.add(new JLabel("Trophy Name:"), BorderLayout.WEST);
        trophyField = new JTextField();
        trophyPanel.add(trophyField, BorderLayout.CENTER);
        inputPanel.add(trophyPanel);

        // Trophy type row
        JPanel typePanel = new JPanel(new BorderLayout(5, 0));
        typePanel.add(new JLabel("Trophy Type:"), BorderLayout.WEST);
        trophyTypeCombo = new JComboBox<>(Trophy.values());
        typePanel.add(trophyTypeCombo, BorderLayout.CENTER);
        inputPanel.add(typePanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addUserBtn = new JButton("Add User");
        addUserBtn.addActionListener(e -> addUser());
        buttonPanel.add(addUserBtn);

        JButton addTrophyBtn = new JButton("Add Trophy");
        addTrophyBtn.addActionListener(e -> addTrophy());
        buttonPanel.add(addTrophyBtn);

        JButton deactivateBtn = new JButton("Deactivate User");
        deactivateBtn.addActionListener(e -> deactivateUser());
        buttonPanel.add(deactivateBtn);

        JButton infoBtn = new JButton("Show Info");
        infoBtn.addActionListener(e -> showPlayerInfo());
        buttonPanel.add(infoBtn);

        inputPanel.add(buttonPanel);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
    }

    private void addComponents() {
        add(createInputPanel(), BorderLayout.NORTH);
        add(createOutputPanel(), BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        addLabelAndField(panel, "Username:", usernameField);
        addLabelAndField(panel, "Game:", gameField);
        addLabelAndField(panel, "Trophy Name:", trophyField);
        addLabelAndField(panel, "Trophy Type:", trophyTypeCombo);

        addButton(panel, "Add User", this::addUser);
        addButton(panel, "Add Trophy", this::addTrophy);
        addButton(panel, "Deactivate User", this::deactivateUser);
        addButton(panel, "Show Info", this::showPlayerInfo);

        return panel;
    }

    private void addLabelAndField(JPanel panel, String labelText, JComponent field) {
        panel.add(new JLabel(labelText));
        panel.add(field);
    }

    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private JScrollPane createOutputPanel() {
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        return scrollPane;
    }

    // Action methods
    private void addUser() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        try {
            psn.addUsers(username);
            outputArea.append("User '" + username + "' added successfully.\n");
            usernameField.setText("");
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
        showError("Por favor complete todos los campos");
        return;
    }

    try {

        if (psn.userExists(username)) {
            psn.addTrophyTo(username, game, trophy, type);
            outputArea.append("Trofeo '" + trophy + "' agregado a '" + username + "'\n");
            gameField.setText("");
            trophyField.setText("");
        } else {
            showError("El usuario '" + username + "' no existe");
        }
    } catch (IOException e) {
        // Mensaje de error más descriptivo
        String errorMsg = "Error al agregar trofeo: ";
        errorMsg += e.getMessage() != null ? e.getMessage() : "Verifique los datos e intente nuevamente";
        showError(errorMsg);
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
            usernameField.setText("");
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
            outputArea.append("\n=== Player Info ===\n");
            psn.playerInfo(username, outputArea);
            outputArea.append("===================\n");
        } catch (IOException e) {
            showError("Error retrieving player info: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GUIPSN().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(GUIPSN.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
