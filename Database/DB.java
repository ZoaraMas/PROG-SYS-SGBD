package Database;

import java.io.Serializable;
import java.util.ArrayList;

public class DB implements Serializable {
    private ArrayList<Relation> listeRelation;

    public DB() {
        this.listeRelation = new ArrayList<>();
    }

    public void drop(String name) throws Exception {
        Relation temp = getRelationByName(name);
        for(int i = 0; i < this.listeRelation.size(); i ++) {
            if(this.listeRelation.get(i).getName().equals(name)) {
                this.listeRelation.remove(i);
                break;
            }
        }
    }

    // Get a Relation by its name
    public Relation getRelationByName(String name) throws Exception {
        for (int i = 0; i < listeRelation.size(); i++) {
            if (this.listeRelation.get(i).getName().equals(name))
                return this.listeRelation.get(i);
        }
        throw new Exception(name + " table not found");
    }

    public void addRelation(Relation r) {
        System.out.println("before = " + this.listeRelation.size());
        this.listeRelation.add(r);
        System.out.println("after = " + this.listeRelation.size());

    }

    public ArrayList<Relation> getListeRelation() {
        return listeRelation;
    }

}
