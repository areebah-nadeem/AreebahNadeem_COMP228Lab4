package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
import java.util.UUID;


import java.sql.*;


public class Main {
    public static String[] major = {"SCIENCE", "MATH", "ECONOMICS", "OTHER"};

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try( Connection connection = DriverManager.
                getConnection("jdbc:sqlite:test.db");
             Statement statement = connection.createStatement();) {


            if (connection != null) {
                System.out.println("Connected to the database");
            } else {
                System.out.println("Failed to connect to the database");
            }



            while (true) {
                int option = optionmenu();

                if (option != 5) {
                    if (option==1) {
                        addRecord(statement);

                    } else if (option == 2) {
                        listByMajor(connection, statement);

                    } else if (option == 3) {
                        listAll(statement);

                    } else if (option == 4) {
                        deleteById(connection);

                    } else {
                        System.out.println("Invalid option");
                        System.out.println("Try Again");


                    }
                } else {
                    System.out.println("Thanks for using this app");
                    break;
                }


                statement.close();

            }

            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        }


    public static int optionmenu(){

        System.out.println("\n+--------------------------------------+\n");
        System.out.println("Select an option:");
        System.out.println("1: Add a new student record");
        System.out.println("2: List All the students by Major");
        System.out.println("3: List all the students");
        System.out.println("4: Delete a student by ID");
        System.out.println("5: Exit");
        System.out.println("\n+--------------------------------------+\n");
        Scanner myOption = new Scanner(System.in);
        String option = myOption.nextLine();
        return (Integer.parseInt(option));
    }

    public static void addRecord(Statement statement) throws SQLException {
        UUID uuid = UUID.randomUUID();
        System.out.println("\n+--------------------------------------+\n");
        System.out.println("ENTER STUDENT INFORMATION");
        System.out.println("\n+--------------------------------------+\n");
        System.out.println("ENTER STUDENT NAME");

        Scanner name = new Scanner(System.in);
        String studentName = name.nextLine();
        System.out.println("\n+--------------------------------------+\n");

        System.out.println("SELECT MAJOR");
        System.out.println("1: SCIENCE");
        System.out.println("2: MATH");
        System.out.println("3: ECONOMICS");
        System.out.println("4: OTHER");
        Scanner majorId = new Scanner(System.in);
        int studentMajor = Integer.parseInt(majorId.nextLine())-1;
        System.out.println("\n+--------------------------------------+\n");


    String insertQuery = "INSERT INTO STUDENT VALUES('" + uuid.toString() +"', ' "+studentName+" ', '"+major[studentMajor]+"');";
     System.out.println("RECORD INSERTED SUCCESSFULLY\n"+insertQuery);
        statement.executeUpdate(insertQuery);


    }



    public static void listByMajor(Connection connection, Statement statement) throws SQLException {

        System.out.println("Select Major");
        System.out.println("1: SCIENCE");
        System.out.println("2: MATH");
        System.out.println("3: ECONOMICS");
        System.out.println("4: OTHER");
        Scanner myOption = new Scanner(System.in);
        int option = Integer.parseInt(myOption.nextLine())-1;


        String majorQuery = "SELECT * FROM STUDENT WHERE MAJOR = ?";


        PreparedStatement preparedStatement =  connection.prepareStatement(majorQuery);
            preparedStatement.setString(1, major[option]);
            ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    System.out.println("ID: " + rs.getString("ID") + ", Name: " + rs.getString("NAME") + ", Major: " + rs.getString("MAJOR"));
                }


    }


    public static void listAll(Statement statement) throws SQLException {
        String insertQuery = "SELECT * FROM STUDENT;";
        ResultSet rs = statement.executeQuery(insertQuery);
        while (rs.next()) {
            System.out.println("ID: " + rs.getString("ID") + ", Name: " + rs.getString("NAME") + ", Major: " + rs.getString("MAJOR"));

        }
    }

    public static void deleteById(Connection connection)throws SQLException {
        System.out.println("Enter ID you want to delete");
        Scanner myOption = new Scanner(System.in);
        String option = myOption.nextLine();
        String deleteQuery = "DELETE FROM STUDENT WHERE ID=?";

        PreparedStatement preparedStatement =  connection.prepareStatement(deleteQuery);

        preparedStatement.setString(1, option);
        int deleteStatement = preparedStatement.executeUpdate();
        String result= (deleteStatement != 0)? "Done: Successfully Deleted.": "Failed: Id not Found ";
        System.out.println(result);
    }




}