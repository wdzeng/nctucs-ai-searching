package io.hyperbola.algo;
import io.hyperbola.base.Variable;

class IntersectJudger {

    private final int index;
    private final char c;

    public IntersectJudger(Variable unassigned, Variable assigned, String word) {
        index = intersectAt(unassigned, assigned);
        int indexOfAssigned = intersectAt(assigned, unassigned);
        c = word.charAt(indexOfAssigned);
    }

    public static int intersectAt(Variable subject, Variable object) {
        return subject.getDirection() == Variable.HORIZONTAL?
                object.getX() - subject.getX():
                object.getY() - subject.getY();
    }

    public boolean judge(String word) {
        return word.charAt(index) == c;
    }
}
