/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Dell
 */



public class MySqlConnector implements Db{



    @Override

    public Connection openConnection() {

       try{

            String username = "root";

            String password = "bayak123";

            String database = "PetAdoptionSystem";

            Connection connection;

            connection = DriverManager.getConnection(

                    "jdbc:mysql://localhost:3306/" + database, username, password

            );

            if(connection == null){

                System.out.println("Database connection fail");

            }else{

                System.out.println("Database connection success");

            }

            return connection;

        }catch(Exception e){

            System.out.println(e);

            return null;

        }
       
    }

    



    @Override

    public void closeConnection(Connection conn) {

        try{

            if(conn != null && !conn.isClosed() ){

                conn.close();

                System.out.println("Connection close");

            }

            

        }catch(Exception e){

            System.out.println(e);

            

        }

    }



    @Override

    public ResultSet runQuery(Connection conn, String query) {

       try{

           Statement stmp = conn.createStatement();

           ResultSet result = stmp.executeQuery(query);

           return result;

       

       }catch (Exception e){

           System.out.println(e);

           return null;

       }

    }

    /**
     *
     * @param conn
     * @param query
     * @return
     */
    @Override

    public int executeUpdate(Connection conn, String query) {

      try{

          Statement stmp = conn.createStatement();

          int result = stmp.executeUpdate(query);

          return result;

          

      }catch(Exception e){

          System.out.println(e);

          return -1;

      }

    }

}