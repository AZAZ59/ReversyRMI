package Common;

import java.io.Serializable;

/**
 * Created by azaz on 08.02.17.
 */
public class Pair<T1,T2> implements Serializable {
    T1 val1;
    T2 val2;

    public Pair(T1 val1, T2 val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "val1=" + val1 +
                ", val2=" + val2 +
                '}';
    }

    public T1 getVal1() {
        return val1;
    }

    public void setVal1(T1 val1) {
        this.val1 = val1;
    }

    public T2 getVal2() {
        return val2;
    }

    public void setVal2(T2 val2) {
        this.val2 = val2;
    }
}
