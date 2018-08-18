package tech.tablesaw.plotly.components;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HoverLabel extends Component {

    public static HoverLabelBuilder builder() {
        return new HoverLabelBuilder();
    }

    /**
     * Sets the background color of all hover labels on graph
     */
    private final String bgColor;

    /**
     * Sets the border color of all hover labels on graph.
     */
    private final String borderColor;

    /**
     * Sets the default hover label font used by all traces on the graph.
     */
    private final Font font;

    /**
     * Sets the default length (in number of characters) of the trace name in the hover labels for all traces.
     * -1 shows the whole name regardless of length. 0-3 shows the first 0-3 characters,
     * and an integer >3 will show the whole name if it is less than that many characters,
     * but if it is longer, will truncate to `namelength - 3` characters and add an ellipsis.
     */
    private final int nameLength;

    HoverLabel(HoverLabelBuilder builder) {
        this.bgColor = builder.bgColor;
        this.borderColor = builder.borderColor;
        this.font = builder.font;
        this.nameLength = builder.nameLength;
    }

    public String asJavascript() {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate;

        try {
            compiledTemplate = engine.getTemplate("hoverLabel_template.html");
            compiledTemplate.evaluate(writer, getContext());
        } catch (PebbleException | IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private Map<String, Object> getContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("bgColor", bgColor);
        context.put("borderColor", borderColor);
        context.put("nameLength", nameLength);
        context.put("font", font);
        return context;
    }

    public static class HoverLabelBuilder {

        /**
         * Sets the background color of all hover labels on graph
         */
        String bgColor = "";

        /**
         * Sets the border color of all hover labels on graph.
         */
        String borderColor = "";

        /**
         * Sets the default hover label font used by all traces on the graph.
         */
        Font font;

        /**
         * Sets the default length (in number of characters) of the trace name in the hover labels for all traces.
         * -1 shows the whole name regardless of length. 0-3 shows the first 0-3 characters,
         * and an integer >3 will show the whole name if it is less than that many characters,
         * but if it is longer, will truncate to `namelength - 3` characters and add an ellipsis.
         */
        int nameLength = 15;

        public HoverLabel build() {
            return new HoverLabel(this);
        }

        public HoverLabelBuilder nameLength(int nameLength) {
            this.nameLength = nameLength;
            return this;
        }

        public HoverLabelBuilder font(Font font) {
            this.font = font;
            return this;
        }

        public HoverLabelBuilder borderColor(String borderColor) {
            this.borderColor = borderColor;
            return this;
        }
        public HoverLabelBuilder bgColor(String bgColor) {
            this.bgColor = bgColor;
            return this;
        }
    }
}
