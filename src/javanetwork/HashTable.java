/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javanetwork;

/**
 *
 * @author Maria Gabriela
 */
public class HashTable {

    private Entry head;

    public HashTable() {
        this.head = null;
    }

    public void add(String username, long pos) {
        Entry newEnt = new Entry(username, pos);
        if (head == null) {
            head = newEnt;
        } else {
            Entry current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newEnt;
        }
    }

    public void remove(String username) {
        if (head == null) {
            return;
        }
        if (head.username.equals(username)) {
            head = head.next;
            return;
        }
        Entry previous = head;
        Entry current = head.next;
        while (current != null) {
            if (current.username.equals(username)) {
                previous.next = current.next;
                return;
            }
            previous = current;
            current = current.next;
        }

    }

    public Long search(String username) {
        Entry current = head;
        while (current != null) {
            if (current.username.equals(username)) {
                return current.pos;
            }
            current = current.next;
        }
        return -1L; 
    }
}
