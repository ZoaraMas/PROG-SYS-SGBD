package Database.ElementsRelation;

public class Condition {
    private String element1;
    private String element2;
    private String signe;
    private boolean twoColumns;
    
    public Condition(String element1, String element2, String signe, boolean twoColumns) {
        this.element1 = element1;
        this.element2 = element2;
        this.signe = signe;
        this.twoColumns = twoColumns;
    }
    
    public String getElement1() {
        return element1;
    }
    public void setElement1(String element1) {
        this.element1 = element1;
    }
    public String getElement2() {
        return element2;
    }
    public void setElement2(String element2) {
        this.element2 = element2;
    }
    public String getSigne() {
        return signe;
    }
    public void setSigne(String signe) {
        this.signe = signe;
    }

    public boolean isTwoColumns() {
        return twoColumns;
    }

    public void setTwoColumns(boolean twoColumns) {
        this.twoColumns = twoColumns;
    }

    

    
}
