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
}
