package Common;

import java.io.Serializable;

/**
 * Created by azaz on 08.02.17.
 */
public class IntIntPair implements Serializable {
    private Integer val1;
    private Integer val2;

    public IntIntPair(Integer val1, Integer val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public Integer getVal1() {
        return val1;
    }

    @Override
    public String toString() {
        return "IntIntPair{" +
                "val1=" + val1 +
                ", val2=" + val2 +
                '}';
    }

    public void setVal1(Integer val1) {
        this.val1 = val1;
    }

    public Integer getVal2() {
        return val2;
    }

    public void setVal2(Integer val2) {
        this.val2 = val2;
    }
}
