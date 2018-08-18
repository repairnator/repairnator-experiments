package tech.tablesaw.components;


import org.junit.Ignore;
import org.junit.Test;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Font;

@Ignore
public class AxisTest {

    @Test
    public void asJavascript() {
        Axis x = Axis.builder()
                .title("x Axis 1")
                .visible(true)
                .type(Axis.Type.DEFAULT)
                .titleFont(Font.builder()
                        .family(Font.Family.ARIAL)
                        .size(8)
                        .color("red")
                        .build())
                .build();

        System.out.println(x);
    }
}