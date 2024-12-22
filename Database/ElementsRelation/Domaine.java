package Database.ElementsRelation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Database.Types.MyChar;
import Database.Types.MyFloat;
import Database.Types.MyInt;
import Database.Types.MyString;
import Ensembles.PackageEnsemble.Ensemble;

public class Domaine implements Serializable {
    // Charque ensemble doit etre compare avec un object de meme type que les
    // objects contenu dans listeEnsemble
    protected ArrayList<Ensemble> listeEnsemble;
    protected int taille;

    // Prog sys
    protected boolean varchar;

    public Domaine(boolean varchar) {
        this.varchar = varchar;
        this.listeEnsemble = new ArrayList<>();
    }

    public Domaine(ArrayList<Ensemble> listeEnsemble) {
        this.varchar = false;
        this.listeEnsemble = listeEnsemble;
    }

    public Domaine(Ensemble e) {
        this.varchar = false;

        ArrayList<Ensemble> list = new ArrayList<>();
        list.add(e);
        this.listeEnsemble = list;
    }

    public Domaine(Domaine d) {
        this.varchar = false;

        this.listeEnsemble = new ArrayList<>();
        for (int i = 0; i < d.getListeEnsemble().size(); i++) {
            this.listeEnsemble.add(d.getListeEnsemble().get(i));
        }
    }

    public Class<?> getMyclass() {
        if (this.varchar) {
            MyString temp = new MyString("");
            return temp.getClass();
        }
        return this.listeEnsemble.get(0).getMyClass();
    }

    public String describe() {
        String result = new String();
        for (int i = 0; i < this.listeEnsemble.size(); i++) {
            result += this.listeEnsemble.get(i).getName() + " " + this.listeEnsemble.get(i).describe();
            result += " - ";
        }
        return result;
    }

    // Creer un domaine a partir du nombre de caractere
    // public Domaine(int taille, ) {

    // }

    // Pouvoir creer un domaine a partir d'un interval
    public Domaine(Object borneInf, Object borneSup, String ensembleName) throws Exception {
        this.varchar = false;

        // Ensemble result = new Ensemble(ensembleName, null, "Value");
        // Seul le String ne peut entrer
        ArrayList<Object> list = new ArrayList<>();
        if (borneInf instanceof MyInt) {
            int inf = ((MyInt) borneInf).getValue();
            int sup = ((MyInt) borneSup).getValue();
            while (inf != sup + 1) {
                list.add(new MyInt(inf));
                // e.insertObject(new MyInt(inf));
                inf++;
            }
        } else if (borneInf instanceof MyChar) {
            char inf = ((MyChar) borneInf).getValue();
            char sup = ((MyChar) borneSup).getValue();
            int taille = sup - inf + 1;
            for (int i = 0; i < taille; i++) {
                list.add(new MyChar((char) (inf + i)));
                // e.insertObject(new MyChar((char)(inf + i)));
            }
        } else if (borneInf instanceof MyFloat) {
            float inf = ((MyFloat) borneInf).getValue();
            float sup = ((MyFloat) borneSup).getValue();

            // Adjust precision to avoid floating-point issues
            inf = Math.round(inf * 100) / 100.0f;
            sup = Math.round(sup * 100) / 100.0f;

            for (float current = inf; current <= sup; current += 0.01f) {
                list.add(new MyFloat(Math.round(current * 100) / 100.0f)); // Round to 2 decimal places
            }
        } else
            throw new Exception("Type not allowed");
        Ensemble e = new Ensemble(ensembleName, list, "Value");
        this.listeEnsemble = new ArrayList<>();
        this.listeEnsemble.add(e);
    }

    // Combiner deux domaines
    public Domaine combineDomaine(Domaine cible) {
        if (this.equals(cible))
            return this;
        Domaine result = new Domaine(this.listeEnsemble);
        result.addlistEnsemble(cible.getListeEnsemble());
        return result;
    }

    public boolean sameDomaine(Domaine d) {
        if (this.equals(d))
            return true;
        return false;
    }

    public boolean equals(Domaine d) {
        if (this.varchar == true) {
            if (d.isVarchar() == true)
                return true;
            else
                return false;
        } else if (this.listeEnsemble.size() == d.getListeEnsemble().size()) {
            for (int i = 0; i < this.listeEnsemble.size(); i++) {
                if (!this.listeEnsemble.get(i).myEquals(d.getListeEnsemble().get(i)))
                    return false;
            }
        } else
            return false;
        return true;
    }

    public boolean InDomain(Object target) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        if (this.varchar == true) {
            if (target instanceof MyString)
                return true;
        } else {
            for (int i = 0; i < this.listeEnsemble.size(); i++) {
                Ensemble e = this.listeEnsemble.get(i);
                if (target == null)
                    return true;
                if (e.getMyClass().equals(target.getClass()) && e.appartenance(target))
                    return true;
            }
        }
        return false;
    }

    public ArrayList<Ensemble> getListeEnsemble() {
        return listeEnsemble;
    }

    public void setListeEnsemble(ArrayList<Ensemble> listeEnsemble) {
        this.listeEnsemble = listeEnsemble;
    }

    public void addEnsemble(Ensemble e) {
        this.listeEnsemble.add(e);
    }

    public void addlistEnsemble(ArrayList<Ensemble> list) {
        for (int i = 0; i < list.size(); i++) {
            addEnsemble(list.get(i));
        }
    }

    public boolean isVarchar() {
        return varchar;
    }

    public void setVarchar(boolean varchar) {
        this.varchar = varchar;
    }

}