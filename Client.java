import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {

    public static void main(String[] args) {

        JFrame f = new JFrame("Student Management System");
        f.getContentPane().setBackground(Color.LIGHT_GRAY);
//for update and retrieve only.
        JLabel lid = new JLabel("ID:");
        lid.setBounds(50, 23, 100, 30);
        JTextField tid = new JTextField();
        tid.setBounds(150, 20, 150, 30);

        JLabel l1 = new JLabel("Name:");
        l1.setBounds(50, 65, 100, 30);
        JTextField t1 = new JTextField();
        t1.setBounds(150, 65, 150, 30);

        JLabel l2 = new JLabel("Course:");
        l2.setBounds(50, 105, 100, 30);
        JTextField t2 = new JTextField();
        t2.setBounds(150, 110, 150, 30);

        JLabel l3 = new JLabel("Semester:");
        l3.setBounds(50, 150, 100, 30);
        JTextField t3 = new JTextField();
        t3.setBounds(150, 155, 150, 30);

        Font labelFont = new Font("Arial", Font.PLAIN, 17);

           lid.setFont(labelFont);
           l1.setFont(labelFont);
           l2.setFont(labelFont);
          l3.setFont(labelFont);

          Font fieldFont = new Font("Arial", Font.PLAIN, 17);
           tid.setFont(fieldFont);
           t1.setFont(fieldFont);
           t2.setFont(fieldFont);
           t3.setFont(fieldFont);   
              
        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(40, 220, 100, 30);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(160, 220, 100, 30);

        JButton retrieveBtn = new JButton("Retrieve");
        retrieveBtn.setBounds(280, 220, 100, 30);

                saveBtn.setBackground(Color.DARK_GRAY);
                saveBtn.setForeground(Color.WHITE);

              updateBtn.setBackground(Color.GRAY);
             updateBtn.setForeground(Color.WHITE);

             saveBtn.setFocusPainted(false);
             updateBtn.setFocusPainted(false);
             //add.
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = t1.getText();
                String course = t2.getText();
                String semester = t3.getText();

                if(name.isEmpty() || course.isEmpty() || semester.isEmpty()) {
                    JOptionPane.showMessageDialog(f,
                            "Please fill all fields!");
                    return;
                }

                try {
                    Socket s = new Socket("localhost", 7000);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    dos.writeUTF(name);
                    dos.writeUTF(course);
                    dos.writeUTF(semester);

                    dos.flush();

                    JOptionPane.showMessageDialog(f,
                            "Data saves in database Successfully!");

                    t1.setText("");
                    t2.setText("");
                    t3.setText("");

                    s.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(f,
                            "Error: " + ex.getMessage());
                }
            }
        });

     //update.
        updateBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {

        String id = tid.getText();
        String name = t1.getText();
        String course = t2.getText();
        String semester = t3.getText();

        if(id.isEmpty()) {
            JOptionPane.showMessageDialog(f, "Please enter ID!");
            return;
                }

                try {
                    Socket s = new Socket("localhost", 7000);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    dos.writeUTF(id);
                    dos.writeUTF(name);
                    dos.writeUTF(course);
                    dos.writeUTF(semester);

                    dos.flush();

                    JOptionPane.showMessageDialog(f,
                            "data updated  Successfully!");

                    s.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(f,
                            "Error: " + ex.getMessage());
                }
            }
        });
        // Retrieve
        retrieveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = tid.getText();

                if(id.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please enter ID to retrieve!");
                    return;
                }

                try {
                    Socket s = new Socket("localhost", 7000);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    DataInputStream dis = new DataInputStream(s.getInputStream());

                    dos.writeUTF("RETRIEVE"); 
                    dos.writeUTF(id);
                    String name = dis.readUTF();
                    String course = dis.readUTF();
                    String semester = dis.readUTF();

                    t1.setText(name);
                    t2.setText(course);
                    t3.setText(semester);

                    s.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(f,
                            "Error: " + ex.getMessage());
                }
            }
        });

        f.add(lid); f.add(tid);
        f.add(l1); f.add(t1);
        f.add(l2); f.add(t2);
        f.add(l3); f.add(t3);
        f.add(saveBtn);
        f.add(updateBtn);
        f.add(retrieveBtn);

        f.setSize(410, 310);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}