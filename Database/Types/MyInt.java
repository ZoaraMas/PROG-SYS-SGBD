package Database.Types;

import Ensembles.PackageEnsemble.Test;

public class MyInt extends MyType<Integer>{
    private int value;

    public MyInt(int value) {
        this.value = value;
    }

    public boolean equals(Object target) {
        MyInt t = (MyInt)target;
        if(this.value == t.value) return true;
        return false;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
   
    
}
