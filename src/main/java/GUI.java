import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

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
}
