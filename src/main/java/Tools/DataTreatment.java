package Tools;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DataTreatment {
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

//  public static LocalDate parseDate (String date) {
//    int currYearLastDigits = Year.now().getValue() % 100;
//    String[] monthDayYear = date.split("/");
//
//    // Al tratarse de pedidos a digitalizar se asume que las fechas son pasadas
//    // si el año no incluye las 4 cifras
//    // se revisa para ver si se trata de 19xx o de 20xx
//    // Esta lógica funciona con pedidos de menos de 100 años
//    String newStrDate =
//            (monthDayYear[2].length() == 2 ?
//             (currYearLastDigits >= Integer.parseInt(monthDayYear[2]) ?
//              "20" + monthDayYear[2] : "19" + monthDayYear[2]) :
//             monthDayYear[2])
//                    + "-" +
//                    (monthDayYear[0].length() == 1 ? "0" + monthDayYear[0] :
//                     monthDayYear[0])
//                    + "-" +
//                    (monthDayYear[1].length() == 1 ? "0" + monthDayYear[1] :
//                     monthDayYear[1]);
//
//    return LocalDate.parse(newStrDate);
//  }
  
  public static String getFormattedDate (LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
  }
  
  public static File selectFile () {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Comma separated values", "CSV", "csv");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);
    return returnVal == JFileChooser.APPROVE_OPTION ?
           chooser.getSelectedFile() : null;
  }
}
