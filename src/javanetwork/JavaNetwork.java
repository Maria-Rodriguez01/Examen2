/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javanetwork;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Maria Gabriela
 */
public class JavaNetwork {

    /**
     * @param args the command line arguments
     */
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
