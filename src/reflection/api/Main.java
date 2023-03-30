package reflection.api;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        Rectangle rectangle = new Rectangle(4,6);
        MyInvestigator mI = new MyInvestigator();
        mI.load(rectangle);
        mI.getInheritanceChain(",");
    }
}
