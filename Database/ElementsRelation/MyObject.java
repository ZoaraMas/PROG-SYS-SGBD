package Database.ElementsRelation;

import Ensembles.PackageEnsemble.Test;

public class MyObject {
    private Attribut attribut;
    private Object value;

    public MyObject(Attribut attribut, Object value) throws Exception {
        String error = attribut.getName() + " | " + value.toString() + ".";
        if (attribut.isCompatible(value)) {
            this.attribut = attribut;
            this.value = value;
        } else
            throw new Exception(error + " incompatible domain");
    }

    public boolean customEquals(MyObject target) {
        if(this.value.equals(target.getValueAsObject())) return true;
        return false;
    }

    public static MyObject copy(MyObject myObject) throws Exception {
        return new MyObject(myObject.geAttribut(), myObject.getValueAsObject());
    }

    public void displayDetailed() {
        if (this.attribut != null)
            System.out.println(attribut.getName() + " | " + value.toString());
        else
            System.out.println("Il n'y a pas d'attribut");
    }

    public Object getValueAsObject() {
        return this.value;
    }

    public String getValue() {
        if (this.attribut != null)
            return value.toString();
        else
            return "err";
    }

    public Attribut geAttribut() {
        return this.attribut;
    }

    public boolean equals(Object target) {
        MyObject o = (MyObject) target;
        if (this.value == o.value)
            return true;
        return false;
    }

}
