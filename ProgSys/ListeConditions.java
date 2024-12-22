package ProgSys;

import Database.ElementsRelation.Condition;

public class ListeConditions {
    private Condition[] listeCondition;
    private String[] andOr;

    public ListeConditions(Condition[] listeCondition, String[] andOr) {
        this.listeCondition = listeCondition;
        this.andOr = andOr;
    }
    public Condition[] getListeCondition() {
        return listeCondition;
    }
    public void setListeCondition(Condition[] listeCondition) {
        this.listeCondition = listeCondition;
    }
    public String[] getAndOr() {
        return andOr;
    }
    public void setAndOr(String[] andOr) {
        this.andOr = andOr;
    }

    
}
