package Database.Types;

import Ensembles.PackageEnsemble.Test;

public class MyString extends MyType<String> {
    private String value;

    public MyString(String value) {
        this.value = value;
    }

    public boolean equals(Object target) {
        MyString t = (MyString)target;
        if(this.value.equals(t.value)) return true;
        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
