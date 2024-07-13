package Tools;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Tools {
  public static LocalDate parseDate (String date) {
    DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter
                            .ofPattern(
                                    "[MM/dd/uuuu]" +
                                    "[MM/d/uuuu]" +
                                    "[M/dd/uuuu]" +
                                    "[M/d/uuuu]" +
                                    "[MM/dd/uu]" +
                                    "[MM/d/uu]" +
                                    "[M/dd/uu]" +
                                    "[M/d/uu]"));
    return LocalDate.parse(date, dtfb.toFormatter());
  }
  
  public static String getFormattedDate (LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
  }
  
  public static File selectImportFile () {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter =
            new FileNameExtensionFilter("*.CSV", "CSV", "csv");
    chooser.setFileFilter(filter);
    chooser.setDialogTitle("Abrir fichero para importar");
    int returnVal = chooser.showOpenDialog(null);
    return returnVal == JFileChooser.APPROVE_OPTION ?
           chooser.getSelectedFile() : null;
  }
  
  public static File selectSaveFile () {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter =
            new FileNameExtensionFilter("*.CSV", "CSV", "csv");
    chooser.setFileFilter(filter);
    chooser.setDialogTitle("Elegir fichero para exportar");
    int returnVal = chooser.showSaveDialog(null);
    
    File file = null;
    if (returnVal == JFileChooser.APPROVE_OPTION){
      file = chooser.getSelectedFile();
      
      String fileName = file.getName();
      if (!fileName.matches(".csv&")){
        file = new File(file.getAbsolutePath() + ".csv");
      }
    }
    return file;
  }
}
