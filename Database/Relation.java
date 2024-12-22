package Database;

import java.util.ArrayList;

import javax.imageio.stream.MemoryCacheImageOutputStream;

import Database.ElementsRelation.Attribut;
import Database.ElementsRelation.Condition;
import Database.ElementsRelation.Domaine;
import Database.ElementsRelation.Nuplet;
import Database.Types.MyType;
import Ensembles.PackageEnsemble.Ensemble;

import java.io.Serializable;
import java.lang.ClassCastException;

public class Relation implements Cloneable, Serializable{
    private String name;
    private ArrayList<Attribut> attributes;
    private ArrayList<Nuplet> data;
    private int columnCount;

    public Relation(String name, ArrayList<Attribut> attributes, ArrayList<Nuplet> data) throws Exception {
        this.name = name;
        this.attributes = attributes;
        if (data == null)
            this.data = new ArrayList<>();
        else
            this.data = data;
        this.columnCount = this.attributes.size();

        // Check data integrity
        System.out.println("kiwi");
        this.display();
        checkDataInDomain();
    }

    // Tester la correspondance des nuplets avec les domaines
    public void checkDataInDomain() throws Exception {
        ArrayList<Nuplet> list = this.getData();
        for(int j = 0; j < list.size(); j ++) {
            Nuplet nUplet = list.get(j);
            for (int i = 0; i < this.columnCount; i++) {
                if (!this.attributes.get(i).isCompatible(nUplet.getList().get(i))) {
                    throw new Exception("Attribute errorr = " + nUplet.getList().get(i).getValue() + " can't be inserted in " + this.attributes.get(i).getDomaine().describe());
                } else {
                }
            }
        }
    }
    // Obtenir une relation plus simple plus flexible n'incluant plus la classe reflect
    
    // Jointure
    public Relation jointure(Relation r1, Relation r2, String alias1, String alias2, Condition c, String type)
            throws Exception {
        Relation r3 = tetaJointure(r1, r2, alias1, alias2, c);
        // Tester les nuplets de r1 et de r2
        // r1
        if (type == null || type.equals("left")) {
            for (int i = 0; i < r1.getData().size(); i++) {
                Nuplet temp = r1.getData().get(i);
                System.out.println("j = " + i);
                if (!temp.insideRelation(r3, "left")) {
                    System.out.println("here");
                    r3.insertSmallerNuplet(temp, "left");
                }
            }
        }
        if (type == null || type.equals("right")) {
            // r2
            for (int i = 0; i < r2.getData().size(); i++) {
                Nuplet temp = r2.getData().get(i);
                if (!temp.insideRelation(r3, "right")) {
                    r3.insertSmallerNuplet(temp, "right");
                }
            }
        }
        return r3;
    }

    // Inserer un nuplet dant la taille est plus petite dans une relation
    public void insertSmallerNuplet(Nuplet target, String insertSide) throws Exception {
        Nuplet temp = target.clone();
        // Ajouter les termes manquants selon le insertSide
        for (int i = 0; i < this.getColumnCount() - temp.getCount(); i++) {
            if (insertSide.equals("left"))
                temp.addLast(null);
            else if (insertSide.equals("right"))
                temp.addFirst(null);
        }
        this.getData().add(temp);
    }

    // Create a nuplet that only contains null values according to the number of
    // columns of this relation
    public Nuplet createNullNuplet() throws Exception {
        Nuplet result = new Nuplet();
        for (int i = 0; i < this.columnCount; i++) {
            result.add(null);
        }
        return result;
    }

    public String getDisplay() {
        String result = new String("=> " + this.name + " <=");
        result += getDisplayAttributes();
        result += getDisplayDatas();
        result += "--------END-------";
        return result;
    }

    public String getDisplayAttributes() {
        String result = new String();
        for (int i = 0; i < this.columnCount; i++) {
            result += this.attributes.get(i).getName() + " | ";
        }
        return result;
    }

    public String getDisplayDatas() {
        String result = new String();
        for (int i = 0; i < this.data.size(); i++) {
            result += (i + 1 + " = ");
            result += this.data.get(i).getDisplay();
            result += "\n";
        }
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Relation clone() {
        try {
            Relation result = (Relation) super.clone();
            ArrayList<Attribut> attList = this.getAttributes();
            ArrayList<Attribut> newAttList = new ArrayList<>();
            for (int i = 0; i < attList.size(); i++) {
                newAttList.add(attList.get(i).clone());
            }
            result.setAttributes(newAttList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Relation masterSelection(Condition[] con, String[] andOr) throws Exception {
        // les valeurs dans AndOr seront les operations intermediaires a faire entre
        // chaque condition
        if (con.length != andOr.length + 1)
        throw new Exception("number of the conditions must be + 1 the number of andOr values");
        Relation result = selection(con[0]);
        System.out.println("panda");
        if (con.length == 1)
            return result;
        for (int i = 1; i < con.length; i++) {
            if (andOr[i - 1].equals("and")) {
                result = result.selection(con[i]);
            } else if (andOr[i - 1].equals("or")) {
                Relation temp = selection(con[i]);
                result = result.union(temp);
            } else
                throw new Exception(
                        "AndOr not recognized at index " + (i - 1) + " => " + andOr[i - 1] + " found instead");
        }
        if (result == null)
            throw new Exception("Syntax may be wrong");
        return result;
    }

    // Ajouter un alias devant le nom de tout les attributs
    public void setAlias(String alias) {
        for (int i = 0; i < this.attributes.size(); i++) {
            Attribut temp = this.attributes.get(i);
            temp.setName(alias + "." + temp.getName());
        }
    }

    // condition: meme type et mm nb colonne
    public Relation union(Relation r)
            throws Exception {
        ArrayList<Attribut> targetAttributes = r.getAttributes();
        ArrayList<Attribut> resultAttributes = new ArrayList<>();
        // Verify if the relations are unionable
        if (targetAttributes.size() == this.columnCount) {
            // From now, if the two attribute don't have the same domain, it will create new
            // Attribute with new Domaine
            for (int i = 0; i < this.columnCount; i++) {
                Domaine d = new Domaine(
                        this.attributes.get(i).getDomaine().combineDomaine(targetAttributes.get(i).getDomaine()));

                String columnName = this.attributes.get(i).getName();

                if (!this.attributes.get(i).getDomaine().sameDomaine(targetAttributes.get(i).getDomaine()))
                    columnName = this.attributes.get(i).getName() + "-" + targetAttributes.get(i).getName();
                Attribut temp = new Attribut(columnName, d);
                resultAttributes.add(temp);
            }
        } else {
            System.out.println("!!!!the count of attributes doesn't match");
            return null;
        }

        // Create the result relation
        Relation result = new Relation(this.name + "_" + r.getName(), resultAttributes, null);
        for (int i = 0; i < this.data.size(); i++) {
            result.insert(this.data.get(i));
        }
        for (int i = 0; i < r.getData().size(); i++) {
            result.insert(r.getData().get(i));
        }
        return result;
    }

    // Selection avec une infinite de conditions en utilisant Or
    public Relation selectOr(Condition[] con) throws Exception {
        if (con.length < 2)
            throw new Exception("Not enough condition, the method needs at least 2 conditions");
        ArrayList<Relation> listRelation = new ArrayList<>();
        for (int i = 0; i < con.length; i++) {
            listRelation.add(this.selection(con[i]));
        }
        Relation result = listRelation.get(0).union(listRelation.get(1));
        for (int i = 2; i < listRelation.size(); i++) {
            result = result.union(listRelation.get(i));
        }
        result.supprimerDoublons();
        return result;
    }

    // Selection avec une infinite de conditions successif en utilisant and
    public Relation selectAnd(Condition[] con) throws Exception {
        Relation result = this.selection(con[0]);
        for (int i = 1; i < con.length; i++) {
            result = result.selection(con[i]);
        }
        return result;
    }

    // Teta jointure
    public static Relation tetaJointure(Relation r1, Relation r2, String alias1, String alias2, Condition c)
            throws Exception {
        Relation r1Clone = r1.clone();
        Relation r2Clone = r2.clone();
        Relation cartesien = produitCartesien(r1Clone, r2Clone, alias1, alias2);
        Relation result = cartesien.selection(c);
        return result;
    }

    public ArrayList<Attribut> combineAttributes(Relation target) {
        ArrayList<Attribut> result = new ArrayList<>();
        for (int i = 0; i < this.attributes.size(); i++)
            result.add(this.attributes.get(i));
        for (int i = 0; i < target.getAttributes().size(); i++)
            result.add(target.getAttributes().get(i));
        return result;
    }

    // Condition meme nombre de nUplet
    public ArrayList<Nuplet> combineNuplets(Relation target) throws Exception {
        ArrayList<Nuplet> result = new ArrayList<>();
        ArrayList<Nuplet> ancienNupletArray = new ArrayList<>(this.getData());
        ArrayList<MyType> ancienMyObjectArray = new ArrayList<>();
        for (int i = 0; i < ancienNupletArray.size(); i++) {
            ancienMyObjectArray = new ArrayList<>(ancienNupletArray.get(i).getList());
            result.add(new Nuplet(ancienMyObjectArray));
        }

        // Combine the actual nuplets
        for (int i = 0; i < result.size(); i++) {
            ArrayList<MyType> tempList = new ArrayList<>(target.getData().get(i).getList());

            Nuplet temp = result.get(i);
            // La grosse bete qui m'empeche d'avoir mon result
            for (int j = 0; j < tempList.size(); j++) {
                temp.add(tempList.get(j));
            }
        }
        return result;
    }

    // Condition: meme nombre de lignes
    public Relation hardJoin(Relation target) throws Exception {
        Relation result = new Relation("hardJoin", combineAttributes(target), combineNuplets(target));
        return result;
    }

    public static Relation produitCartesien(Relation r1, Relation r2, String alias1, String alias2) throws Exception {
        Relation r3 = r1.selfUnion(r2.getData().size(), true);
        // System.out.println("coquillage");
        Relation r4 = r2.selfUnion(r1.getData().size(), false);
        // r3.display();
        r3.setAlias(alias1);
        r4.setAlias(alias2);
        return r3.hardJoin(r4);
    }

    // isNumber
    public static boolean isNumber(String str) {
        // Regular expression to match only digits, allowing leading zeros
        return str != null && str.matches("\\d+");
    }

    // Dans de vrais conditions de sgbd, les valeurs de comparaison sont represente
    // par des chiffres ou des chaines de caracteres entre guillemet
    // Alors que les noms de table sans juste represente en chaine de caractere sans
    // guillemet

    // Selection en colonne valeur
    public Relation selection(Condition con) throws Exception {
        // Fetch data from the column specified in the condition
        Ensemble target = getEnsemble(con.getElement1());
        ArrayList<Nuplet> myData = new ArrayList<>();
        ArrayList<Object> list = target.getVal(); // List of objects to compare
        String signe = con.getSigne();
        Object element2 = con.getElement2();

        // ArrayList if we have two columns in the selection
        ArrayList<Object> list1 = new ArrayList<>();
        ArrayList<Object> list2 = new ArrayList<>();

        boolean sameColumnCount = false;
        if (con.isTwoColumns()) {
            Ensemble[] targetArray = new Ensemble[2];
            int[] attributIndexes = getAttributeIndex(con.getElement1(), con.getElement2());
            targetArray[0] = getEnsemble(attributIndexes[0]);
            targetArray[1] = getEnsemble(attributIndexes[1]);

            if (targetArray[0].getList().size() != targetArray[1].getList().size())
                throw new Exception("Number of column in select not the same");

            // On prend les objets pour pouvoir comparer
            list1 = targetArray[0].getVal();
            list2 = targetArray[1].getVal();
        }

        for (int i = 0; i < target.getCard(); i++) {
            Object element1 = list.get(i);
            if (con.isTwoColumns()) {
                element1 = list1.get(i);
                element2 = list2.get(i);
            }

            if (element2 == null)
                continue;
            // Perform comparisons based on the operator
            boolean conditionMet = false;

            if (signe.equals("=") && (element1.toString().equals(element2.toString()))) {
                conditionMet = true;
            } else if ((signe.equals("!=") && !(element1.toString().equals(element2.toString())))) {
                conditionMet = true;
            } else if (element1 instanceof Number && isNumber(String.valueOf(element2))) {
                // Both elements are numbers, compare as doubles
                double value1 = ((Number) element1).doubleValue();
                double value2;
                try {
                    value2 = ((Number) element2).doubleValue();
                } catch (ClassCastException e) {
                    value2 = Integer.parseInt((String) element2);
                }

                switch (signe) {
                    case "<":
                        conditionMet = value1 < value2;
                        break;
                    case "<=":
                        conditionMet = value1 <= value2;
                        break;
                    case ">":
                        conditionMet = value1 > value2;
                        break;
                    case ">=":
                        conditionMet = value1 >= value2;
                        break;
                }
            } else if (element1 instanceof String) {
                // Both elements are strings, compare lexicographically
                String value1 = (String) element1;
                String value2;
                try {

                    value2 = (String) element2;
                } catch (ClassCastException e) {
                    value2 = String.valueOf(element2);
                }

                switch (signe) {
                    case "<":
                        conditionMet = value1.compareToIgnoreCase(value2) < 0;
                        break;
                    case "<=":
                        conditionMet = value1.compareToIgnoreCase(value2) <= 0;
                        break;
                    case ">":
                        conditionMet = value1.compareToIgnoreCase(value2) > 0;
                        break;
                    case ">=":
                        conditionMet = value1.compareToIgnoreCase(value2) >= 0;
                        break;
                }
            } else if (element1 instanceof Character) {
                // Both elements are characters, compare their Unicode values
                char value1 = (char) element1;
                char value2 = ((String) element2).charAt(0);

                switch (signe) {
                    case "<":
                        conditionMet = value1 < value2;
                        break;
                    case "<=":
                        conditionMet = value1 <= value2;
                        break;
                    case ">":
                        conditionMet = value1 > value2;
                        break;
                    case ">=":
                        conditionMet = value1 >= value2;
                        break;
                }
            }
            // If the condition is met, add the corresponding Nuplet
            if (conditionMet) {
                myData.add(this.data.get(i));
            }
        }

        // Create a new relation with the filtered data and remove duplicates
        Relation result = new Relation("S_", this.attributes, myData);
        result.supprimerDoublons();
        return result;
    }

    // Transformer les donnes d'une colonne en 1 ensemble dont l'order compte
    public Ensemble getEnsemble(String columnName) throws Exception {
        int attributeIndex = getAttributeIndex(columnName);

        // Create the ensemble
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < this.data.size(); i++) {
            list.add(this.data.get(i).getList().get(attributeIndex));
        }
        Ensemble result = new Ensemble(columnName, list, "Value", new Object());
        return result;
    }

    public Ensemble getEnsemble(int columnIndex) throws Exception {
        int attributeIndex = columnIndex;
        // Create the ensemble
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < this.data.size(); i++) {
            list.add(this.data.get(i).getList().get(attributeIndex));
        }
        Ensemble result = new Ensemble(getAttributeName(attributeIndex), list, "Value", new Object());
        return result;
    }

    // Copy the datas with omited indexes
    // Copier betement les datas d'une relation a une autre
    public ArrayList<Nuplet> getProjectedData(String[] columns) throws Exception {
        ArrayList<Nuplet> list = new ArrayList<>();
        for (int i = 0; i < this.data.size(); i++) {
            Nuplet nUpletTemp = new Nuplet();
            for (int j = 0; j < columns.length; j++) {
                nUpletTemp.add(this.data.get(i).getList().get(getAttributeIndex(columns[j])));
            }
            list.add(nUpletTemp);
        }
        return list;
    }

    // Obtenir l'indexe de la colonne avec le nom
    public int getAttributeIndex(String attributeName) throws Exception {
        for (int i = 0; i < this.attributes.size(); i++) {
            if (this.attributes.get(i).getName().equals(attributeName))
                return i;
        }
        throw new Exception("Attribute " + attributeName + " couldn't be find");
    }

    public String getAttributeName(int index) throws Exception {
        if (index >= this.attributes.size())
            throw new Exception(index + " is out of bound ");
        return this.attributes.get(index).getName();
    }

    // Obtenir l'index de deux colonnes avec leurs noms
    public int[] getAttributeIndex(String attributeName1, String attributeName2) throws Exception {
        String[] param = new String[2];
        param[0] = attributeName1;
        param[1] = attributeName2;
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;
        int resultIndex = 0;
        // variable that stocks the index that has already been taken
        int alreadyChosenIndex = -1;
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < this.attributes.size(); i++) {
                if (this.attributes.get(i).getName().equals(param[j]) && i != alreadyChosenIndex) {
                    result[resultIndex] = i;
                    alreadyChosenIndex = i;
                    resultIndex++;
                    break;
                }
            }
        }
        if (result[0] == -1)
            throw new Exception("Attribute " + param[0] + " couldn't be find");
        if (result[1] == -1)
            throw new Exception("Attribute " + param[1] + " couldn't be find");
        return result;
    }

    // public ArrayList<Nuplet> getProjectedData(int[] omitedIndexes) {
    // ArrayList<Nuplet> list = new ArrayList<>();
    // for (int i = 0; i < this.data.size(); i++) {
    // Nuplet nUpletTemp = new Nuplet();
    // for (int j = 0; j < columnCount; j++) {
    // if (!intInArray(omitedIndexes, j))
    // nUpletTemp.add(this.data.get(i).getList().get(j));
    // }
    // list.add(nUpletTemp);
    // }
    // return list;
    // }

    // public boolean intInArray(int[] tab, int val) {
    // for (int i = 0; i < tab.length; i++) {
    // if (val == tab[i])
    // return true;
    // }
    // return false;
    // }

    // Omettre les colonnes non specifies
    // Tid, name(emp)
    public Relation projection(String[] columns) throws Exception {
        // Obtenir la nouvelle liste d'attribut
        ArrayList<Attribut> temp = new ArrayList<>();
        for (int i = 0; i < columns.length; i++) {
            Attribut tempTemp = getAttribut(columns[i]);
            // On annule tout si la syntaxe des colonnes a garder est mauvaise
            if (tempTemp == null) {
                return null;
            }
            temp.add(tempTemp);
        }
        // Obtenir les datas avec les colonnes en moins
        Relation result = new Relation("T_" + this.name, temp, getProjectedData(columns));
        result.supprimerDoublons();
        if (result != null)
            return result;
        throw new Exception("columns not found in projection");
    }

    // Obtenir les indices de colonnes a omettre
    public int[] getOmitedIndex(String[] columns) {
        int omitedCount = this.columnCount - columns.length;
        if (omitedCount == 0)
            return null;
        int[] result = new int[omitedCount];
        int index = 0;
        for (int i = 0; i < this.columnCount; i++) {
            boolean appeared = false;
            for (int j = 0; j < columns.length; j++) {
                if (columns[j].equals(this.attributes.get(i).getName())) {
                    appeared = true;
                    break;
                }
            }
            if (!appeared) {
                result[index] = i;
                index++;
            }
        }
        return result;
    }

    // Reflect methods
    public static boolean checkInArray(ArrayList<Object> list, Object target) {
        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i)))
                return true;
        }
        return false;
    }

    // Obtenir un attribut a partir de son nom
    public Attribut getAttribut(String attributeName) {
        for (int i = 0; i < this.columnCount; i++) {
            if (this.attributes.get(i).getName().equals(attributeName))
                return this.attributes.get(i);
        }
        return null;
    }

    public void supprimerDoublons() {
        ArrayList<Nuplet> tempArray = new ArrayList<>();
        for (int i = 0; i < this.data.size(); i++) {
            if (!checkNUpletInArray(tempArray, this.data.get(i)))
                tempArray.add(this.data.get(i));
            else {
                this.data.remove(i);
                i--;
            }
        }
    }

    public boolean checkNUpletInArray(ArrayList<Nuplet> array, Nuplet target) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(target))
                return true;
        }
        return false;
    }

    // Fonction union speciale pour join, qui va alors union la table pour repeter
    // ses valeurs times nombres de fois
    // si memeValGroupe, on aura 11 22 33 etc si c'est l'inverse on a 123 123
    public Relation selfUnion(int times, boolean memeValGroupe)
            throws Exception {
        int newTimes = times; // we need this variable to keep times variable intact
        if (times == 1) {
            memeValGroupe = false;
        }
        // Create the result relation
        Relation result = new Relation("SelfUnion x " + times, this.attributes, null);
        System.out.println("panda = " + result.getColumnCount());
        // inserer la table apres table
        if (!memeValGroupe) {
            for (int j = 0; j < newTimes; j++) {
                for (int i = 0; i < this.data.size(); i++) {
                    if (!result.insert(this.data.get(i)))
                        throw new Exception("Nombre de nuplet target " + this.data.get(i).getCount()
                                + " ne correspond pas au nombre de colonne de la relation = " + result.getName() + " | "
                                + result.getColumnCount());
                }
            }
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                for (int j = 0; j < newTimes; j++) {
                    result.insert(this.data.get(i));
                }
            }
        }
        return result;
    }

    public void displayAttributes() {
        for (int i = 0; i < this.columnCount; i++) {
            System.out.print(this.attributes.get(i).getName() + " | ");
        }
        System.out.println();
    }

    public void displayDatas() {
        for (int i = 0; i < this.data.size(); i++) {
            System.out.print(i + 1 + " = ");
            this.data.get(i).display();
            System.out.println("\n");
        }

    }

    public void display() {
        System.out.println("=> " + this.name + " <=");
        displayAttributes();
        displayDatas();
        System.out.println("---------------");
    }

    // Insert a nuplet
    public boolean insert(Nuplet nUplet)
            throws Exception {
        // ArrayList<Attribut> targetAttributes = nUplet.getAttributs();
        if (nUplet.getCount() == this.columnCount) {
            for (int i = 0; i < this.columnCount; i++) {
                if (!this.attributes.get(i).isCompatible(nUplet.getList().get(i))) {
                    throw new Exception("Attribute error = " + nUplet.getList().get(i).getValue() + " can't be inserted in " + this.attributes.get(i).getDomaine().describe());
                }
            }
        } else {
            return false;
        }
        this.data.add(nUplet);
        return true;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Attribut> getAttributes() {
        return attributes;
    }

    public ArrayList<Nuplet> getData() {
        return data;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setAttributes(ArrayList<Attribut> attributes) {
        this.attributes = attributes;
    }
}
