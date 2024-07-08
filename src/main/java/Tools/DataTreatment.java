package Tools;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class DataTreatment {
  public static LocalDate parseDate (String date) {
    int currYearLastDigits = Year.now().getValue() % 100;
    String[] monthDayYear = date.split("/");
    
    // Al tratarse de pedidos a digitalizar se asume que las fechas son pasadas
    // si el año no incluye las 4 cifras
    // se revisa para ver si se trata de 19xx o de 20xx
    // Esta lógica funciona con pedidos de menos de 100 años
    String newStrDate =
            (monthDayYear[2].length() == 2 ?
             (currYearLastDigits >= Integer.parseInt(monthDayYear[2]) ?
              "20" + monthDayYear[2] : "19" + monthDayYear[2]) :
             monthDayYear[2])
                    + "-" +
                    (monthDayYear[0].length() == 1 ? "0" + monthDayYear[0] :
                     monthDayYear[0])
                    + "-" +
                    (monthDayYear[1].length() == 1 ? "0" + monthDayYear[1] :
                     monthDayYear[1]);
    
    return LocalDate.parse(newStrDate);
  }
  
  public static String showFormattedDate (LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
  }
}
