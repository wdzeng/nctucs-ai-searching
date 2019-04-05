package io.hyperbola.base;
import java.util.ArrayList;
import java.util.List;

/**
 * A variable of the puzzle.
 */
public final class Variable implements Comparable<Variable> {

    /**
     * A builder that optimizes all variable inputs, adjusting their coordinates so that the minimum x- and y-
     * coordinates are zeros.
     */
    public static class Builder {

        private boolean discarded = false;
        private List<Variable> varList = new ArrayList<>();

        /**
         * Creates a builder.
         */
        public Builder() {
        }

        /**
         * Adds a variable.
         * @param x         coordinate x
         * @param y         coordinate y
         * @param length    length of word
         * @param direction either {@link Variable#HORIZONTAL} or {@link Variable#VERTICAL}
         * @return this builder
         * @throws IllegalStateException    If this builder has already built restrictions.
         * @throws IllegalArgumentException if length is not positive
         */
        public Builder addVariable(int x, int y, int length, boolean direction) {
            if (discarded) throw new IllegalStateException();
            Variable r = new Variable(x, y, length, direction);
            updateNeighbors(r);
            varList.add(r);
            return this;
        }

        /**
         * Generates a list of optimized restrictions. This function can only be called once.
         * @return a list of optimized restrictions
         * @throws IllegalStateException if this method has been called before, or if no variable is provided
         */
        public VariableSurveyResult build() {
            if (discarded) throw new IllegalStateException();
            final int minX = varList.stream().mapToInt(r -> r.x).min().getAsInt();
            final int minY = varList.stream().mapToInt(r -> r.y).min().getAsInt();
            final int maxX = varList.stream()
                                    .mapToInt(r -> r.getDirection() == HORIZONTAL? r.x + r.length: r.x + 1)
                                    .max()
                                    .getAsInt();
            final int maxY = varList.stream()
                                    .mapToInt(r -> r.getDirection() == HORIZONTAL? r.y + 1: r.y + r.length)
                                    .max()
                                    .getAsInt();
            varList.forEach(r -> {
                r.x -= minX;
                r.y -= minY;
                r.neighbors = List.copyOf(r.neighbors);
            });
            List<Variable> result = new ArrayList<>(varList);
            result.sort(null);
            VariableSurveyResult set = new VariableSurveyResult(result, maxX, maxY);
            discarded = true;
            return set;
        }

        private void updateNeighbors(Variable newVar) {
            newVar.neighbors = new ArrayList<>();
            for (Variable v: varList) {
                if (v.isNeighborOf(newVar)) {
                    newVar.neighbors.add(v);
                    v.neighbors.add(newVar);
                }
            }
        }
    }

    public static final boolean HORIZONTAL = true;
    public static final boolean VERTICAL = false;
    private boolean horizon;
    private int length, x, y;
    private List<Variable> neighbors;

    private Variable(int x, int y, int length, boolean direction) {
        if (length <= 0) throw new IllegalArgumentException();
        this.length = length;
        this.horizon = direction;
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Variable o) {
        if (x + y == o.x + o.y) {
            if (x == o.x) return Boolean.compare(horizon, o.horizon);
            return x - o.x;
        }
        return (x + y) - (o.x + o.y);
    }

    /**
     * Queries direction of the word.
     */
    public boolean getDirection() {return horizon;}

    /**
     * Queries the count of neighbors of this variable.
     */
    public int getNeighborCount() {
        return neighbors.size();
    }

    /**
     * Queries all the variable that intersect to this one.
     * @return an unmodifiable list
     */
    public List<Variable> getNeighbors() {
        return neighbors;
    }

    /**
     * Queries x coordinate of starting point.
     */
    public int getX() {return x;}

    /**
     * Queries y coordinate of starting point.
     */
    public int getY() {return y;}

    @Override
    public int hashCode() {
        int h = length | (x << 12) | (y << 24);
        return horizon? h: ~h;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            Variable r = (Variable) o;
            return r.x == x && r.y == y && r.horizon == horizon && r.length == length;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Var{(%d, %d), %d, %s}", x, y, length, horizon? "H": "V");
    }

    /**
     * Queries if this variable intersects to another one.
     */
    private boolean isNeighborOf(Variable another) {
        if (horizon == another.horizon) return false;
        if (!horizon) return another.isNeighborOf(this);
        // This variable must be horizontal while another vertical
        return y < another.y + another.length
                && y >= another.y
                && x <= another.x
                && x + length > another.x;
    }

    /**
     * Queries the length of the variable.
     */
    public int length() {return length;}
}
