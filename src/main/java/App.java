import Tools.DataTreatment;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.System.exit;

/**
 * TODO
 * -Hacer el resumen tras insertar.
 * -Exportar a un nuevo fichero
 * -Crear una GUI o algo para mostrar el resumen
 */


public class App {
  private static final Integer BATCH_SIZE = 100;
  
  private static final String DB_URL = "jdbc:mysql://localhost/prueba_basica";
  private static final String DB_USER = "root";
  private static final String DB_PWD = "";
  
  public static void main (String[] args) {
    File importFile = DataTreatment.selectFile();
    
    if (importFile == null) {
      JOptionPane.showMessageDialog(
              null,
              "No se seleccionó un archivo válido. Saliendo del programa",
              "Alerta", JOptionPane.WARNING_MESSAGE);
      exit(1);
    }
    
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD)) {
      insertRecords(conn, importFile);
      generateSummary(conn);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void insertRecords (Connection conn, File importFile) {
    String query =
            "INSERT INTO `order`" +
            "(`region`, `country`, `itemType`, `salesChannel`, `orderPriority`," +
            "`orderDate`, `orderId`, `shipDate`, `unitsSold`, `unitPrice`, " +
            "`unitCost`, `totalRevenue`, `totalCost`, `totalProfit`)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (BufferedReader reader = Files.newBufferedReader(importFile.toPath());) {
      PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
      conn.setAutoCommit(false);
      
      String line;
      int count = 0;
      
      // Salta la línea de la cabecera antes de recorrer los datos
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] record = line.split(",");
        
        setPreparedStatement(ps, record);
        
        if (count % BATCH_SIZE == 0) {
          ps.executeBatch();
          System.out.println("Registros insertados: " + count);
        }
        count++;
        System.out.println(count);
      }
      ps.executeBatch();
      System.out.println("Registros insertados: " + count);
      conn.setAutoCommit(true);
      System.out.println("Finalizada copia a la base de datos");
    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void setPreparedStatement(PreparedStatement ps, String[] record){
    try {
      ps.setString(1, record[0]);
      ps.setString(2, record[1]);
      ps.setString(3, record[2]);
      ps.setString(4, record[3]);
      ps.setString(5, record[4]);
      ps.setObject(6, DataTreatment.parseDate(record[5]));
      ps.setString(7, record[6]);
      ps.setObject(8, DataTreatment.parseDate(record[7]));
      ps.setInt(9, Integer.parseInt(record[8]));
      ps.setDouble(10, Double.parseDouble(record[9]));
      ps.setDouble(11, Double.parseDouble(record[10]));
      ps.setDouble(12, Double.parseDouble(record[11]));
      ps.setDouble(13, Double.parseDouble(record[12]));
      ps.setDouble(14, Double.parseDouble(record[13]));
      ps.addBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void generateSummary (Connection conn) {
    String query = "SELECT `region`, COUNT(*) AS 'quantity' " +
                   "FROM `order`" +
                   "GROUP BY `region`";
  }
}

// List<Integer> generatedKeys = new ArrayList<Integer>();

//generatedKeys.addAll(Arrays.asList());


//      ResultSet rs = ps.getGeneratedKeys();
//      while(rs.next()){
//        System.out.println(rs.getString("GENERATED_KEY"));
//      }

//      List<List<String>> records =
//              reader.lines()
//                    .map(line -> Arrays.asList(line.split(",")))
//                    .toList();


//      String region = record[0];
//      String country = record[1];
//      String itemType = record[2];
//      String salesChannel = record[3];
//      String orderPriority = record[4];
//      LocalDate orderDate = DataTreatment.parseDate(record[5]);
//      String orderId = record[6];
//      LocalDate shipDate = DataTreatment.parseDate(record[7]);
//      int unitsSold = Integer.parseInt(record[8]);
//      double unitPrice = Double.parseDouble(record[9]);
//      double unitCost = Double.parseDouble(record[10]);
//      double totalRevenue = Double.parseDouble(record[11]);
//      double totalCost = Double.parseDouble(record[12]);
//      double totalProfit = Double.parseDouble(record[13]);

//      System.out.println(region);
//      System.out.println(country);
//      System.out.println(itemType);
//      System.out.println(salesChannel);
//      System.out.println(orderPriority);
//      System.out.println(DataTreatment.showFormattedDate(orderDate));
//      System.out.println(orderId);
//      System.out.println(DataTreatment.showFormattedDate(shipDate));
//      System.out.println(unitsSold);
//      System.out.println(unitPrice);
//      System.out.println(unitCost);
//      System.out.println(totalRevenue);
//      System.out.println(totalCost);
//      System.out.println(totalProfit);