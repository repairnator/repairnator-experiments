package org.grobid.core.layout;

/**
 * Class for representing the layout information associated to a PDF object.
 *
 * @author Patrice Lopez
 */
public class LayoutToken implements Comparable<LayoutToken> {
    private String text = null;
    public double y = -1.0;
    public double x = -1.0;
    public double width = 0.0;
    public double height = 0.0;
    private String font = null;
    private boolean bold = false;
    private boolean italic = false;
    private String colorFont = null;
    public double fontSize = 0.0;
    private boolean rotation = false;
    private int page = -1;
    private boolean newLineAfter;
    private int blockPtr;
	private int offset = 0;

    public LayoutToken() {
    }

    public LayoutToken(String text) {
        this.text = text;
    }

    public LayoutToken(LayoutToken token) {
        this.text = token.text;
        this.y = token.y;
        this.x = token.x;
        this.width = token.width;
        this.height = token.height;
        this.font = token.font;
        this.bold = token.bold;
        this.italic = token.italic;
        this.colorFont = token.colorFont;
        this.fontSize = token.fontSize;
        this.rotation = token.rotation;
        this.page = token.page;
        this.newLineAfter = token.newLineAfter;
        this.blockPtr = token.blockPtr;
        this.offset = token.offset;
    }

    public void setFont(String f) {
        font = f;
    }

    public String getFont() {
        return font;
    }

    public void setText(String f) {
        //text = f.replaceAll("\n", "");
		text = f;
    }

    public void setRotation(boolean b) {
        rotation = b;
    }

    public boolean getRotation() {
        return rotation;
    }

    public String getText() {
        return text;
    }

    public String t() {
        return text;
    }

    public void setColorFont(String f) {
        colorFont = f;
    }

    public String getColorFont() {
        return colorFont;
    }

    public void setBold(boolean b) {
        bold = b;
    }

    public void setItalic(boolean i) {
        italic = i;
    }

    public boolean getBold() {
        return bold;
    }

    public boolean getItalic() {
        return italic;
    }

    public void setFontSize(double d) {
        fontSize = d;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setX(double d) {
        x = d;
    }

    public double getX() {
        return x;
    }

    public void setY(double d) {
        y = d;
    }

    public double getY() {
        return y;
    }

    public void setHeight(double d) {
        height = d;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double d) {
        width = d;
    }

    public double getWidth() {
        return width;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isNewLineAfter() {
        return newLineAfter;
    }

    public void setNewLineAfter(boolean newLineAfter) {
        this.newLineAfter = newLineAfter;
    }

    public int getBlockPtr() {
        return blockPtr;
    }

    public void setBlockPtr(int blockPtr) {
        this.blockPtr = blockPtr;
    }

	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}

    @Override
    public String toString() {
        return text;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = text != null ? text.hashCode() : 0;
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (font != null ? font.hashCode() : 0);
        result = 31 * result + (bold ? 1 : 0);
        result = 31 * result + (italic ? 1 : 0);
        result = 31 * result + (colorFont != null ? colorFont.hashCode() : 0);
        temp = Double.doubleToLongBits(fontSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (rotation ? 1 : 0);
        result = 31 * result + page;
        return result;
    }

    @Override
    public int compareTo(LayoutToken token2) {
        if (y != token2.y) {
            if (y < token2.y)
                return -1;
            else 
                return 1;
        }
        else if (x != token2.x) {
            if (x < token2.x)
                return -1;
            else 
                return 1;
        }
        else {
            double area1 = height*width;
            double area2 = token2.height*token2.width;

            return Double.compare(area1, area2);
        }
    }

}