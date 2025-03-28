/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javanetwork;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

/**
 *
 * @author Maria Gabriela
 */
public class PSNUSers {

    RandomAccessFile psn;
    HashTable users;

    public PSNUSers() throws IOException {
        this.psn = new RandomAccessFile("usuarios.psn", "rw");
        this.users = new HashTable();
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        psn.seek(0);
        while (psn.getFilePointer() < psn.length()) {
            long pos = psn.getFilePointer();
            String username = psn.readUTF();
            boolean active = psn.readBoolean();
            psn.readInt();
            psn.readInt();
            if (active) {
                users.add(username, pos);
            }

        }
    }

    public void addUsers(String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (users.search(username) != -1L) {
            throw new IOException("User exists already");
        }
        long pos = psn.length();
        psn.seek(pos);
        psn.writeUTF(username);
        psn.writeBoolean(true);
        psn.writeInt(0);
        psn.writeInt(0);
        users.add(username, pos);

    }

    public void deactivateUser(String username) throws IOException {
        long pos = users.search(username);
        if (pos != -1) {
            psn.seek(pos + username.length() + 2);
            psn.writeBoolean(false);
            users.remove(username);
        } else {
            throw new IOException("Not a valid user");
        }
    }

    public void addTrophyTo(String username, String trophyGame, String trophyName, Trophy type) throws IOException {
        long pos = users.search(username);

        psn.seek(pos);
        psn.readUTF();
        psn.readBoolean();
        int trophyAmount = psn.readInt();
        trophyAmount++;
        int points = psn.readInt();
        points += type.points;
        psn.seek(pos + psn.readUTF().length() + 2);
        psn.writeInt(trophyAmount + 1);
        psn.writeInt(points + type.points);
        psn.seek(psn.length());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        psn.writeUTF(username);
        psn.writeUTF(type.name());
        psn.writeUTF(trophyGame);
        psn.writeUTF(trophyName);
        psn.writeUTF(date);

    }

   public void playerInfo(String username, JTextArea outputArea) throws IOException {
    long pos = users.search(username);
    if (pos == -1L) {
        outputArea.append("User not found: " + username + "\n");
        return;
    }

    psn.seek(pos);
    String storedUsername = psn.readUTF();
    boolean active = psn.readBoolean();
    int trophyCount = psn.readInt();
    int points = psn.readInt();

    if (!active) {
        outputArea.append("User is inactive: " + username + "\n");
        return;
    }

    outputArea.append("\n=== Player Information ===\n");
    outputArea.append("Username: " + storedUsername + "\n");
    outputArea.append("Status: " + (active ? "Active" : "Inactive") + "\n");
    outputArea.append("Trophy Count: " + trophyCount + "\n");
    outputArea.append("Total Points: " + points + "\n");
    outputArea.append("=========================\n");
}
}
