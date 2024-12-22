package Database.Types;

import Ensembles.PackageEnsemble.Test;

public class MyChar extends MyType<Character>{
    private char value;

    public MyChar(char value) {
        this.value = value;
    }

    public boolean equals(Object target) {
        MyChar t = (MyChar)target;
        if(this.value == t.value) return true;
        return false;
    }
    public Character getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

        
}
