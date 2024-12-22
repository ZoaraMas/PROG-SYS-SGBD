package Database.ElementsRelation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Ensembles.PackageEnsemble.Ensemble;

public class Attribut implements Cloneable, Serializable{
    private String name;
    private Domaine domaine;

    public Attribut(String name, Domaine domaine) {
        setName(name);
        setDomaine(domaine);
    }

    public Attribut clone() {
        try {
            return (Attribut) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class<?> getMyclass() {
        return this.domaine.getMyclass();
    }

    // Test if this Attribute intersects with another one
    public boolean isCompatible(Attribut a) {
        if (this.domaine.equals(a.getDomaine())) return true;
        return false;
    }

    // Test if an object is compatible with this attribute
    public boolean isCompatible(Object o) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if(this.domaine.InDomain(o)) return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }
}
