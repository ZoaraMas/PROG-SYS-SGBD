package Database.Types;

import Ensembles.PackageEnsemble.Test;

public class MyFloat extends MyType<Float>{
    private float value;

    public MyFloat(float value) {
        this.value = value;
    }

    public boolean equals(Object target) {
        MyFloat t = (MyFloat)target;
        if(this.value == t.value) return true;
        return false;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    
}
