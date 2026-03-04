import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(7000);
            System.out.println("Server started...");

            while(true) {
                Socket s = ss.accept();
                System.out.println("Client connected");

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/project_db",
                        "root",
                        ""); 

                System.out.println("Database Connected!");

                String firstInput = "";

                try {
                    firstInput = dis.readUTF();

                    // ---------------- Retrieve ----------------
                    if(firstInput.equalsIgnoreCase("RETRIEVE")) {
                        String idStr = dis.readUTF();
                        int id = Integer.parseInt(idStr);

                        PreparedStatement ps = con.prepareStatement(
                                "SELECT Name, course, semester FROM students WHERE id = ?");
                        ps.setInt(1, id);
                        ResultSet rs = ps.executeQuery();

                        if(rs.next()) {
                            dos.writeUTF(rs.getString("Name"));
                            dos.writeUTF(rs.getString("course"));
                            dos.writeUTF(rs.getString("semester"));
                        } else {
                            dos.writeUTF("");
                            dos.writeUTF("");
                            dos.writeUTF("");
                        }

                    } else {
                        // ---------------- Update ----------------
                        int id = Integer.parseInt(firstInput);

                        String name = dis.readUTF();
                        String course = dis.readUTF();
                        String semester = dis.readUTF();

                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE students SET " +
                                        "Name = COALESCE(NULLIF(?,''), Name), " +
                                        "course = COALESCE(NULLIF(?,''), course), " +
                                        "semester = COALESCE(NULLIF(?,''), semester) " +
                                        "WHERE id = ?"
                        );

                        ps.setString(1, name);     
                        ps.setString(2, course);   
                        ps.setString(3, semester); 
                        ps.setInt(4, id);

                        int rows = ps.executeUpdate();
                        if(rows > 0)
                            System.out.println("Student Updated: ID=" + id);
                        else
                            System.out.println("No record found with ID=" + id);
                    }

                } catch(NumberFormatException ne) {
                    //add.
                    
                    String name = firstInput; 
                    String course = dis.readUTF();
                    String semester = dis.readUTF();

                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO students(Name, course, semester) VALUES (?, ?, ?)");
                    ps.setString(1, name);
                    ps.setString(2, course);
                    ps.setString(3, semester);

                    int rows = ps.executeUpdate();
                    System.out.println("New Student Saved: " + name + ", Rows inserted=" + rows);
                }

                con.close();
                s.close();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}