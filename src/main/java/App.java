import Tools.DataTreatment;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class App {
  public static void main (String[] args) {
    String DB_URL = "jdbc:mysql://localhost/prueba_basica";
    String USER = "root";
    String query = "INSERT INTO 'order' VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,)";
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("./src/main/resources/RegistroVentas1.csv"));
         Connection conn = DriverManager.getConnection(DB_URL, USER, "")) {
      PreparedStatement ps = conn.prepareStatement(query);
      String line = null;
      int count = 0;
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] record = line.split(",");
        String region = record[0];
        String country = record[1];
        String itemType = record[2];
        String salesChannel = record[3];
        String orderPriority = record[4];
        LocalDate orderDate = DataTreatment.parseDate(record[5]);
        String orderId = record[6];
        LocalDate shipDate = DataTreatment.parseDate(record[7]);
        Integer unitsSold = Integer.parseInt(record[8]);
        Double unitPrice = Double.parseDouble(record[9]);
        Double unitCost = Double.parseDouble(record[10]);
        Double totalRevenue = Double.parseDouble(record[11]);
        Double totalCost = Double.parseDouble(record[12]);
        Double totalProfit = Double.parseDouble(record[13]);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

//      List<List<String>> records =
//              reader.lines()
//                    .map(line -> Arrays.asList(line.split(",")))
//                    .toList();


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