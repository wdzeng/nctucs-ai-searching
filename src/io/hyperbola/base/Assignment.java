package io.hyperbola.base;
public final class Assignment {

    public final Variable variable;
    public final String word;

    /**
     * Creates an assignment object.
     * @param wordFilled  word assigned to a variable
     * @param varAssigned variable assigned
     * @throws IllegalArgumentException if lengths of word and variable are not identical
     */
    public Assignment(String wordFilled, Variable varAssigned) {
        if (varAssigned.length() != wordFilled.length())
            throw new IllegalArgumentException();
        this.variable = varAssigned;
        this.word = wordFilled;
    }

    @Override
    public int hashCode() {
        return word.hashCode() ^ variable.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Assignment) {
            Assignment act = (Assignment) o;
            return word.equals(act.word) && variable.equals(act.variable);
        }
        return false;
    }
}
