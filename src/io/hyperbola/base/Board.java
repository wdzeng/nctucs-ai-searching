package io.hyperbola.base;
import java.util.Arrays;

public final class Board {

    private final char[] sq;
    private final int width, height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        sq = new char[width * height];
    }

    private void fill(int x, int y, char c) {
        sq[index(x, y)] = c;
    }

    public void fillHorizontal(int x, int y, String word) {
        char[] chars = word.toCharArray();
        for (int i = 0, len = word.length(); i < len; i++) fill(x + i, y, chars[i]);
    }

    public void fillVertical(int x, int y, String word) {
        char[] chars = word.toCharArray();
        for (int i = 0, len = word.length(); i < len; i++) fill(x, y + i, chars[i]);
    }

    public int getHeight() {return height;}

    public int getWidth() {return width;}

    private int index(int x, int y) {
        return y * width + x;
    }

    public char query(int x, int y) {
        return sq[index(x, y)];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(width * height + height);
        boolean first = true;
        for (int y = 0; y < height; y++) {
            if (first) first = false;
            else sb.append('\n');
            for (int x = 0; x < width; x++) {
                char c = sq[index(x, y)];
                // Convert to full size
                if (c == 0) sb.append('　');
                else sb.append((char) (c - 'A' + 'Ａ'));
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board) {
            return Arrays.equals(sq, ((Board) o).sq);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sq);
    }
}
