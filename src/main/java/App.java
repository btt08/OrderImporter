import Tools.DBUtils;
import Tools.DataTreatment;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * TODO
 * Me da que no vale con hacer una consulta,
 * eso incluiría toda la BD y no lo que se acaba de exportar
 * Igual con el ID/key generado con la opción esa que vi antes cuando se genera el ps
 * -Hacer el resumen tras insertar.
 * -Exportar a un nuevo fichero
 * -Crear una GUI o algo para elegir fichero y mostrar el resumen
 */


public class App {
  public static void main (String[] args) {
    String DB_URL = "jdbc:mysql://localhost/prueba_basica";
    String USER = "root";
    String query = "INSERT INTO `order` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("./src/main/resources/RegistroVentas1.csv"));
         Connection conn = DriverManager.getConnection(DB_URL, USER, "")) {
      PreparedStatement ps = conn.prepareStatement(query);
      conn.setAutoCommit(false);

      String line = null;
      int count = 0;
      int batchSize = 50;
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] record = line.split(",");

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
        if (count % batchSize == 0) {
          ps.executeBatch();
          System.out.println("Lanzo tras: " + count);
        }
        count++;
      }
      System.out.println("Lanzo tras: " + count);
      ps.executeBatch();
      conn.setAutoCommit(true);

//      DBUtils.emptyDB();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

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