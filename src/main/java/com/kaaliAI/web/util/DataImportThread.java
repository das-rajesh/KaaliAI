/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaaliAI.web.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Dell
 */
public class DataImportThread extends Thread {

    String uploadPath = "C:\\Users\\Dell\\Documents\\NetBeansProjects\\Kaali\\src\\main\\webapp\\WEB-INF\\upload\\";
    String fileName;

    public DataImportThread() {
    }

    public DataImportThread(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            BufferedReader reader = new BufferedReader(new FileReader(uploadPath + "/" + fileName));
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/kaali", "root", "");
            String sql = "insert into root_word(key_word)"  
                    + "values(?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String line = "";
            int i=1;
            while ((line = reader.readLine()) != null) {
                System.out.println("importing......" +i+" "+ line);
                String[] tokens = line.split(",");
                stmt.setString(1, line);
               
                stmt.executeUpdate();
                i++;

            }
            conn.close();
        } catch (SQLException | ClassNotFoundException | IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

}
