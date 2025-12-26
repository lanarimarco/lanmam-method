# Date/Time Conversion Patterns

## RPGLE Date Formats to Java

| RPGLE Format | Example | Java Type | Conversion |
|--------------|---------|-----------|------------|
| *ISO | 2024-12-14 | LocalDate | Direct parse |
| *USA | 12/14/2024 | LocalDate | Custom formatter |
| *EUR | 14.12.2024 | LocalDate | Custom formatter |
| *JIS | 2024-12-14 | LocalDate | Direct parse |
| YYMMDD | 241214 | LocalDate | Custom parse |
| MMDDYY | 121424 | LocalDate | Custom parse |
| YYYYMMDD | 20241214 | LocalDate | Custom parse |

## Conversion Utilities

```java
public class DateUtils {

    private static final DateTimeFormatter YYMMDD =
        DateTimeFormatter.ofPattern("yyMMdd");
    private static final DateTimeFormatter MMDDYY =
        DateTimeFormatter.ofPattern("MMddyy");
    private static final DateTimeFormatter YYYYMMDD =
        DateTimeFormatter.ofPattern("yyyyMMdd");

    public static LocalDate parseRpgDate(String dateStr, String format) {
        switch(format) {
            case "YYMMDD":
                return LocalDate.parse(dateStr, YYMMDD);
            case "MMDDYY":
                return LocalDate.parse(dateStr, MMDDYY);
            case "YYYYMMDD":
                return LocalDate.parse(dateStr, YYYYMMDD);
            default:
                return LocalDate.parse(dateStr);
        }
    }

    public static String formatToRpg(LocalDate date, String format) {
        switch(format) {
            case "YYMMDD":
                return date.format(YYMMDD);
            case "MMDDYY":
                return date.format(MMDDYY);
            case "YYYYMMDD":
                return date.format(YYYYMMDD);
            default:
                return date.toString();
        }
    }
}
```
