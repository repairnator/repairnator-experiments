package tech.tablesaw.columns.numbers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberColumnFormatter {

    private final NumberFormat format;
    private String missingString = "";

    public static NumberColumnFormatter percent(int fractionalDigits) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(fractionalDigits);
        format.setMaximumFractionDigits(fractionalDigits);
        return new NumberColumnFormatter(format);
    }

    public static NumberColumnFormatter ints() {
        NumberFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        return new NumberColumnFormatter(format);
    }

    public static NumberColumnFormatter intsWithGrouping() {
        NumberFormat format = new DecimalFormat();
        format.setGroupingUsed(true);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        return new NumberColumnFormatter(format);
    }

    public static NumberColumnFormatter fixedWithGrouping(int fractionalDigits) {
        NumberFormat format = new DecimalFormat();
        format.setGroupingUsed(true);
        format.setMinimumFractionDigits(fractionalDigits);
        format.setMaximumFractionDigits(fractionalDigits);
        return new NumberColumnFormatter(format);
    }

    public static NumberColumnFormatter currency(String language, String country) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale(language, country));
        return new NumberColumnFormatter(format);
    }

    public NumberColumnFormatter() {
        this.format = null;
    }

    public NumberColumnFormatter(NumberFormat format) {
        this.format = format;
    }

    public NumberColumnFormatter(NumberFormat format, String missingString) {
        this.format = format;
        this.missingString = missingString;
    }

    public String format(double value) {

        if (isMissingValue(value)) {
            return missingString;
        }
        if (format == null) {
            return String.valueOf(value);
        }
        return format.format(value);
    }

    @Override
    public String toString() {
        return "NumberColumnFormatter{" +
                "format=" + format +
                ", missingString='" + missingString + '\'' +
                '}';
    }

    private boolean isMissingValue(double value) {
        return value != value;
    }
}
