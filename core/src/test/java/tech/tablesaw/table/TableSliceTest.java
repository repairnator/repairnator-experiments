package tech.tablesaw.table;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.selection.Selection;

import static org.junit.Assert.*;
import static tech.tablesaw.aggregate.AggregateFunctions.*;

public class TableSliceTest {

    private Table source;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        source = Table.read().csv("../data/bush.csv");
    }

    @Test
    public void column() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, 4));
        assertEquals(source.column(1).name(), slice.column(1).name());
        assertTrue(source.rowCount() > slice.column(1).size());
        assertEquals(source.column("date").name(), slice.column("date").name());
        assertTrue(source.rowCount() > slice.column("date").size());
        assertEquals(slice.column(1).size(), slice.column("date").size());
        assertEquals(4, slice.column("date").size());
    }

    @Test
    public void columnCount() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.columnCount(), slice.columnCount());
    }

    @Test
    public void rowCount() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.rowCount(), slice.rowCount());

        TableSlice slice1 = new TableSlice(source, Selection.withRange(0, 100));
        assertEquals(100, slice1.rowCount());
    }

    @Test
    public void columns() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.columns(), slice.columns());
    }

    @Test
    public void columnIndex() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.columnIndex("who"), slice.columnIndex("who"));

        Column<?> who = source.column("who");
        assertEquals(source.columnIndex(who), slice.columnIndex(who));
    }

    @Test
    public void get() {
        TableSlice slice = new TableSlice(source, Selection.withRange(10, source.rowCount()));
        assertNotNull(slice.get(0, 1));
        assertEquals(source.get(10, 1), slice.get(0, 1));
    }

    @Test
    public void name() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.name(), slice.name());
    }

    @Test
    public void clear() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        slice.clear();
        assertTrue(slice.isEmpty());
        assertFalse(source.isEmpty());
    }

    @Test
    public void columnNames() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.columnNames(), slice.columnNames());
    }

    @Test
    public void addColumn() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Class TableSlice does not support the addColumns operation");
        slice.addColumns(StringColumn.create("test"));
    }

    @Test
    public void removeColumns() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Class TableSlice does not support the removeColumns operation");
        slice.removeColumns("who");
    }

    @Test
    public void first() {
        TableSlice slice = new TableSlice(source, Selection.withRange(2, 12));
        Table first = slice.first(5);
        assertEquals(first.get(0, 1), slice.get(0, 1));
        assertEquals(first.get(0, 1), source.get(2, 1));
    }

    @Test
    public void setName() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        slice.setName("foo");
        assertEquals("foo", slice.name());
        assertNotEquals("foo", source.name());
    }

    @Test
    public void print() {
        TableSlice slice = new TableSlice(source, Selection.withRange(0, source.rowCount()));
        assertEquals(source.print(), slice.print());
    }

    @Test
    public void asTable() {
        TableSlice slice = new TableSlice(source, Selection.withRange(1, 11));
        Table t = slice.asTable();
        assertEquals(10, t.rowCount());
        assertEquals(source.get(1, 1), t.get(0, 1));
    }

    @Test
    public void reduce() throws Exception {
        source = Table.read().csv("../data/bush.csv");
        TableSlice slice = new TableSlice(source, Selection.with(2));
        assertEquals(58.0, slice.reduce("approval", sum), 0.0001);
    }
}