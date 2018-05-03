package ru.job4j.strategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * мне показался такой рефакторинг более длинным и запутанным
 * @author Alexander Kaleganov
 * @version 2.0
 */

public class PaintTestV2 {
    // поле содержит дефолтный вывод в консоль.
    private final PrintStream stdout = System.out;
    // буфер для результата.
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void loadOutput()  {
        System.out.println("execute before method");
        System.setOut(new PrintStream(this.out));
    }

    @After
    public void backOutput() {
        System.setOut(this.stdout);
        System.out.println("execute after method");
    }

    @Test
    public void testirovanieTRIANGLEpaint() {
        new Paint().draw(new Triangle());
        assertThat(
                new String(this.out.toByteArray()),
                is(new StringBuilder()
                        .append("  #  ")
                        .append(" ### ")
                        .append("#####")
                        .append(System.lineSeparator())
                        .toString()
                )
        );
    }

    @Test
    public void testirovanieSQUAREpaint() {
        new Paint().draw(new Square());
        assertThat(
                new String(this.out.toByteArray()), is(new StringBuilder()
                        .append("####")
                        .append("####")
                        .append("####")
                        .append("####")
                        .append(System.lineSeparator())
                        .toString()
                )
        );
    }
}

