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

    private final PSNUSers psn;
    private final JTextField usernameField = new JTextField();
    private final JTextField gameField = new JTextField();
    private final JTextField trophyField = new JTextField();
    private final JComboBox<Trophy> trophyTypeCombo = new JComboBox<>(Trophy.values());
    private final JTextArea outputArea = new JTextArea();

    public GUIPSN() throws IOException {
        super("PSN Users Management System");
        this.psn = new PSNUSers();
        setupFrame();
        addComponents();
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

        if (username.isEmpty() || game.isEmpty() || trophy.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        try {
            Trophy type = (Trophy) trophyTypeCombo.getSelectedItem();
            psn.addTrophyTo(username, game, trophy, type);
            outputArea.append("Trophy '" + trophy + "' added to user '" + username + "'.\n");
            gameField.setText("");
            trophyField.setText("");
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
