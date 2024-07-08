package Tools;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
  public static void emptyDB(){
    String DB_URL = "jdbc:mysql://localhost/prueba_basica";
    String USER = "root";
    String query = "DELETE FROM `order`";
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, "")) {
      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
  }
}
