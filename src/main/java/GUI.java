import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GUI {
  public static File selectImportFile () {
    JFileChooser chooser = getChooser("Abrir fichero para importar");
    int returnVal = chooser.showOpenDialog(null);
    return returnVal == JFileChooser.APPROVE_OPTION ?
           chooser.getSelectedFile() : null;
  }
  
  public static File selectSaveFile () {
    JFileChooser chooser = getChooser("Elegir fichero para exportar");
    int returnVal = chooser.showSaveDialog(null);
    
    File file = null;
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      file = chooser.getSelectedFile();
      
      String fileName = file.getName();
      if (!fileName.matches(".csv")) {
        file = new File(file.getAbsolutePath() + ".csv");
      }
    }
    return file;
  }
  
  private static JFileChooser getChooser (String title) {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter =
            new FileNameExtensionFilter("*.CSV", "CSV", "csv");
    chooser.setFileFilter(filter);
    chooser.setDialogTitle(title);
    return chooser;
  }
  
  public static void showError () {
    JOptionPane.showMessageDialog(
            null,
            "No se seleccionó un archivo válido. Saliendo del programa",
            "Alerta", JOptionPane.WARNING_MESSAGE);
    System.exit(1);
  }
  
  public static JScrollPane createTable (ResultSet rs) {
    JTable table = new JTable();
    
    table.setFont(new Font("Serif", Font.PLAIN, 18));
    table.setRowHeight(25);
    
    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("Value");
    tableModel.addColumn("#");
    
    Object[] row = new Object[2];
    
    try {
      while (rs.next()) {
        row[0] = rs.getString("value");
        row[1] = rs.getInt("quantity");
        System.out.println("\t" + row[0] + ": " + row[1]);
        tableModel.addRow(row);
      }
      
      table.setModel(tableModel);
      
      // Cambia el ancho de las columnas
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      table.getColumnModel().getColumn(0).setPreferredWidth(250);
      table.getColumnModel().getColumn(1).setPreferredWidth(60);
      
      // Fija la columna de cantidad a la derecha
      DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
      cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
      table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
      table.setPreferredScrollableViewportSize(table.getPreferredSize());
      
      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      return scrollPane;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static JPanel createGridElement (String field) {
    JLabel label = new JLabel(field.toUpperCase());
    label.setFont(new Font("Serif", Font.BOLD, 20));
    label.setSize(100, 50);
    
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(label, BorderLayout.NORTH);
    
    return panel;
  }
  
  public static JFrame createMainWindow(){
    JFrame mainWindow = new JFrame();
    GridLayout gridLayout = new GridLayout(0, 3);
    gridLayout.setHgap(30);
    gridLayout.setVgap(30);
    mainWindow.setLayout(gridLayout);
    mainWindow.setResizable(false);
    mainWindow.setBounds(200, 200, 1100, 800);
    mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    return mainWindow;
  }
}
