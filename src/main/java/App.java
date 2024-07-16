import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class App {
  private static final Integer BATCH_SIZE = 1000;
  
  private static File importFile;
  private static File exportFile;
  
  public static void main (String[] args) {
    Properties props = new Properties();
    
    try (FileInputStream fis = new FileInputStream("app.config")) {
      props.load(fis);
    } catch (IOException e) {
      System.out.println("Error cargando el fichero de configuración");
      throw new RuntimeException(e);
    }
    
    importFile = GUI.selectImportFile();
    exportFile = GUI.selectSaveFile();
    
    if (importFile == null || exportFile == null) {
      GUI.showError();
    }
    
    try (Connection conn = DriverManager.getConnection(props.getProperty("DB_URL"),
                                                       props.getProperty("DB_USER"),
                                                       props.getProperty("DB_PWD"))) {
      conn.setAutoCommit(false);
      
      // Guardamos id mínimo y máximo para identificar lo insertado
      Integer[] ids = insertRecords(conn);
      
      if (ids == null) {
        System.out.println("Todos los registros son duplicados o el fichero estaba vacío");
        System.exit(0);
      }
      
      exportToFile(conn, ids);
      
      System.out.println("Creando resumen de los datos\n");
      generateSummary(conn, ids);
      
      conn.setAutoCommit(true);
      System.out.println("\nFinalizada transacción. Datos insertados en la BD");
      System.out.println("Proceso finalizado con éxito");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Integer[] insertRecords (Connection conn) {
    String query =
            "INSERT IGNORE INTO `order`" +
            "(`region`, `country`, `item_type`, `sales_channel`, `order_priority`," +
            "`order_date`, `order_id`, `ship_date`, `units_sold`, `unit_price`, " +
            "`unit_cost`, `total_revenue`, `total_cost`, `total_profit`)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    System.out.println("Iniciando lectura del fichero " + importFile.toPath());
    try (BufferedReader fileReader = Files.newBufferedReader(importFile.toPath())) {
      PreparedStatement ps = conn.prepareStatement(query,
                                                   PreparedStatement.RETURN_GENERATED_KEYS);
      String line;
      int count = 0;
      List<Integer> generatedKeys = new ArrayList<>();
      
      // Salta la línea de la cabecera
      fileReader.readLine();
      
      System.out.println("Iniciada transacción");
      while ((line = fileReader.readLine()) != null) {
        String[] record = line.split(",");
        
        setPreparedStatement(ps, record);
        
        if (count % BATCH_SIZE == 0 && count > 0) {
          executeBatch(ps, generatedKeys);
          System.out.println("Registros procesados: " + count);
        }
        count++;
      }
      executeBatch(ps, generatedKeys);
      
      System.out.println("Registros procesados: " + count);
      System.out.println("\nFinalizada lectura del fichero\n");
      System.out.println("Se han encontrado " + (count - generatedKeys.size()) + " duplicados");
      
      return !generatedKeys.isEmpty() ?
             new Integer[] {generatedKeys.get(0),
                            generatedKeys.get(generatedKeys.size() - 1)} : null;
    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void exportToFile (Connection conn, Integer[] keys) {
    String query = String.format(
            "SELECT" +
            " `order_id`, `order_priority`, `order_date`, `region`, `country`, " +
            " `item_type`, `sales_channel`, `ship_date`, `units_sold`, `unit_price`, " +
            " `unit_cost`, `total_revenue`, `total_cost`, `total_profit` " +
            "FROM `order` " +
            "WHERE `id` BETWEEN %d AND %d " +
            "ORDER BY `order_id`", keys[0], keys[1]);
    
    try (Statement statement = conn.createStatement()) {
      System.out.println("Exportando datos al fichero " + exportFile.toPath());
      
      ResultSet rs = statement.executeQuery(query);
      List<String> dataToExport = getCSVDataToExport(rs);
      
      writeToFile(dataToExport);
      System.out.println("Archivo creado correctamente");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void writeToFile (List<String> dataToExport) {
    try (FileWriter fw = new FileWriter(exportFile)) {
      for (String record : dataToExport) {
        fw.write(record + "\n");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void generateSummary (Connection conn, Integer[] ids) {
    JFrame mainWindow = GUI.createMainWindow();
    
    String[] summaryFields = new String[] {"region", "country", "item_type",
                                           "sales_channel", "order_priority"};
    
    for (String field : summaryFields) {
      String query = String.format("SELECT `%s` AS `value`, COUNT(*) AS 'quantity' " +
                                   "FROM `order` " +
                                   "WHERE `id` BETWEEN %d AND %d " +
                                   "GROUP BY `%s`", field, ids[0], ids[1], field);
      
      try (Statement statement = conn.createStatement()) {
        statement.execute(query);
        ResultSet rs = statement.getResultSet();
        
        JPanel gridElement = GUI.createGridElement(field);
        gridElement.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        gridElement.add(GUI.createTable(rs), BorderLayout.CENTER);
        mainWindow.add(gridElement);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    
    mainWindow.setVisible(true);
  }
  
  private static void setPreparedStatement (PreparedStatement ps, String[] record) {
    try {
      ps.setString(1, record[0]);
      ps.setString(2, record[1]);
      ps.setString(3, record[2]);
      ps.setString(4, record[3]);
      ps.setString(5, record[4]);
      ps.setObject(6, Tools.parseDate(record[5]));
      ps.setString(7, record[6]);
      ps.setObject(8, Tools.parseDate(record[7]));
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
  
  private static void executeBatch (PreparedStatement ps, List<Integer> generatedKeys) throws SQLException {
    ps.executeBatch();
    ResultSet rs = ps.getGeneratedKeys();
    
    while (rs.next()) {
      generatedKeys.add(rs.getInt("GENERATED_KEY"));
    }
  }
  
  private static List<String> getCSVDataToExport (ResultSet rs) throws SQLException {
    List<String> dataToExport = new ArrayList<>();
    
    dataToExport.add("Order ID,Order Priority,Order Date,Region,Country," +
                     "Item Type,Sales Channel,Ship Date,Units Sold,Unit Price," +
                     "Unit Cost,Total Revenue,Total Cost,Total Profit");
    
    while (rs.next()) {
      String csvRecord =
              String.format(
                      "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,",
                      rs.getString("order_id"),
                      rs.getString("order_priority"),
                      Tools.getFormattedDate(
                              rs.getDate("order_date").toLocalDate()),
                      rs.getString("region"),
                      rs.getString("country"),
                      rs.getString("item_type"),
                      rs.getString("sales_channel"),
                      Tools.getFormattedDate(
                              rs.getDate("ship_date").toLocalDate()),
                      rs.getInt("units_sold"),
                      rs.getDouble("unit_price"),
                      rs.getDouble("unit_cost"),
                      rs.getDouble("total_revenue"),
                      rs.getDouble("total_cost"),
                      rs.getDouble("total_profit"));
      dataToExport.add(csvRecord);
    }
    return dataToExport;
  }
}