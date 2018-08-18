package tech.tablesaw.columns.datetimes;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.columns.AbstractColumnType;
import tech.tablesaw.columns.StringParser;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.time.LocalDateTime;

public class DateTimeColumnType extends AbstractColumnType {

    public static int BYTE_SIZE = 8;

    public static final DateTimeStringParser DEFAULT_PARSER = new DateTimeStringParser(ColumnType.LOCAL_DATE_TIME);

    public static final DateTimeColumnType INSTANCE =
            new DateTimeColumnType(BYTE_SIZE, "LOCAL_DATE_TIME", "DateTime");

    private DateTimeColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    @Override
    public DateTimeColumn create(String name) {
        return DateTimeColumn.create(name);
    }

    @Override
    public StringParser<LocalDateTime> defaultParser() {
        return new DateTimeStringParser(this);
    }

    @Override
    public DateTimeStringParser customParser(CsvReadOptions options) {
        return new DateTimeStringParser(this, options);
    }

    @Override
    public Comparable<?> getMissingValueIndicator() {
        return missingValueIndicator();
    }

    public static long missingValueIndicator() {
        return Long.MIN_VALUE;
    }
}
