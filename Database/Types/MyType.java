package Database.Types;

import java.io.Serializable;

import Database.ElementsRelation.Attribut;
import Database.ElementsRelation.MyObject;

public class MyType<T> implements Cloneable, Serializable{
    public MyType() {};
    
    public String toString() {
        return this.getValue().toString();
    }
     public MyType<T> clone() {
        try {
            return (MyType) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public T getValue() {
        return null;
    };
    
}