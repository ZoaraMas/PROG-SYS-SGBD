package Ensembles.PackageEnsemble;

// Ceci est une classe de test pour les differents types d'ensembles
public class Test {
    private int value;
    public Test(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public void afficher() {
        System.out.println(this.value);
    }

    public boolean customEquals(Object target) {
        Test t = (Test)target;
        if(this.value == t.value) return true;
        return false;
    }
}