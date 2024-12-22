package ProgSys;

import java.util.ArrayList;

import Database.DB;
import Database.Relation;
import Database.ElementsRelation.Attribut;
import Database.ElementsRelation.Condition;
import Database.ElementsRelation.Domaine;
import Database.ElementsRelation.Nuplet;
import Database.Types.MyFloat;
import Database.Types.MyInt;
import Database.Types.MyString;
import Database.Types.MyType;

public class Message {
    // Regles
    // Les elements de projections sont separes par des points virgules uniquement

    // Creer une table
    public static int createTable(String[] message, DB db, DB dbRep, boolean rep) throws Exception {
        if (rep == true)
            throw new Exception("Trying to update the replication instance which is read only");
        // Creer les domaines
        ArrayList<Attribut> listAttributes = new ArrayList<>();
        String[] tab = message[1].split(" ");
        for (int i = 0; i < tab.length; i += 2) {
            if (tab[i].equals("int")) {
                Domaine d = new Domaine(new MyInt(-10), new MyInt(10), "int");
                listAttributes.add(new Attribut("int", d));
            } else if (tab[i].equals("varchar")) {
                Domaine d = (new Domaine(true));
                listAttributes.add(new Attribut("varchar", d));
            } else
                throw new Exception("Attribute type must be int or varchar");
        }
        Relation result = new Relation(message[0].substring(1), listAttributes, null);
        db.addRelation(result);
        dbRep.addRelation(result);
        Relation temptemp = db.getRelationByName("temp");
        temptemp.display();
        return 1;
    }

    public static int insert(String[] message, DB db, DB dbRep, boolean rep) throws Exception {
        String relationName = message[0].substring(1);

        Relation r = db.getRelationByName(relationName);
        Relation rRep = dbRep.getRelationByName(relationName);

        System.out.println("a");

        if (rep == true)
            throw new Exception("Trying to update the replication instance which is read only");
        // Obtenir les differes elements a inserer
        String[] param = message[1].split(",");
        // Liste d'attribut de la relation
        ArrayList<Attribut> attributList = r.getAttributes();
        if (attributList.size() != param.length)
            throw new Exception("The count of inserted values doesn't match with the relation columnCount");
        int count = attributList.size();
        // Creer le nuplet selon les types d'attributs
        ArrayList<MyType> myObjectList = new ArrayList<>();
        // Object sample to create the nuplet
        MyInt myInt = new MyInt(0);
        MyString myString = new MyString("0");
        MyFloat myFloat = new MyFloat(0);
        System.out.println("b");

        // Create the nuplet elements
        for (int i = 0; i < count; i++) {
            System.out.println("count = " + i);
            if (attributList.get(i).getMyclass().equals(myInt.getClass())) {
                System.out.println("c");
                myObjectList.add(new MyInt(Integer.parseInt(param[i])));
                System.out.println("d " + param[i]);
            } else if (attributList.get(i).getMyclass().equals(myString.getClass())) {
                System.out.println("e");

                myObjectList.add(new MyString(param[i]));
                System.out.println("f");

            } else if (attributList.get(i).getMyclass().equals(myFloat.getClass())) {
                System.out.println("g");

                myObjectList.add(new MyFloat(Float.parseFloat(param[i])));
                System.out.println("h");

            } else
                throw new Exception("attritut non reconnu de " + attributList.get(i).getName());
        }
        // Insert the nuplet
        r.insert(new Nuplet(myObjectList));
        rRep.insert(new Nuplet(myObjectList));
        return 1;
    }

    public static Relation select(String[] message, DB db, DB dbRep, boolean rep) throws Exception {
        String relationName = message[0].substring(1);
        Relation r = db.getRelationByName(relationName);
        if (rep == true)
            r = dbRep.getRelationByName(relationName);
        // Obtenir les projections
        String[] argumentProjection = getProjectionElements(message[2]);
        System.out.println("1");
        // Voici la relation en question
        // Faire les operations
        // Selection avec conditions
        if (message[1] != null) {
            System.out.println("2");
            // Obtenir les conditions avec les successions de and et or
            ListeConditions lc = getConditionsObject(message[1]);
            System.out.println("5");
            r = r.masterSelection(lc.getListeCondition(), lc.getAndOr());
        }
        System.out.println("6");

        // Projection
        if (argumentProjection != null) {
            System.out.println("7");

            r = r.projection(argumentProjection);
        }
        System.out.println("3");

        if (r == null)
            throw new Exception("Could not translate the message");
        System.out.println("4");

        return r;
    }

    public static Relation translate(String[] message, DB db, DB dbRep, boolean rep) throws Exception {
        // Obtenir type d'operation et nom de relation
        String relationName = message[0].substring(1);
        String operation = "" + message[0].charAt(0);
        Relation r = db.getRelationByName(relationName);
        Relation rRep = dbRep.getRelationByName(relationName);

        if (operation.equals("1")) { // selection
            // Obtenir les projections
            String[] argumentProjection = getProjectionElements(message[2]);

            System.out.println("1");
            // Voici la relation en question

            // Faire les operations
            // Selection avec conditions
            if (message[1] != null) {
                System.out.println("2");
                // Obtenir les conditions avec les successions de and et or
                ListeConditions lc = getConditionsObject(message[1]);
                System.out.println("5");
                r = r.masterSelection(lc.getListeCondition(), lc.getAndOr());
            }
            System.out.println("6");

            // Projection
            if (argumentProjection != null) {
                System.out.println("7");

                r = r.projection(argumentProjection);
            }
            System.out.println("3");

            if (r == null)
                throw new Exception("Could not translate the message");
            System.out.println("4");

            return r;
        } else if (operation.equals("2")) { // insertion
            System.out.println("fleur = " + rep);
            if (rep == true)
                throw new Exception("Trying to write on read only instance of DB");
            // Obtenir les differes elements a inserer
            String[] param = message[1].split(",");
            // Liste d'attribut de la relation
            ArrayList<Attribut> attributList = r.getAttributes();
            if (attributList.size() != param.length)
                throw new Exception("The count of inserted values doesn't match with the relation columnCount");
            int count = attributList.size();
            // Creer le nuplet selon les types d'attributs
            ArrayList<MyType> myObjectList = new ArrayList<>();
            // Object sample to create the nuplet
            MyInt myInt = new MyInt(0);
            MyString myString = new MyString("0");
            MyFloat myFloat = new MyFloat(0);
            // Create the nuplet elements
            for (int i = 0; i < count; i++) {
                if (attributList.get(i).getMyclass().equals(myInt.getClass())) {
                    myObjectList.add(new MyInt(Integer.parseInt(param[i])));
                } else if (attributList.get(i).getMyclass().equals(myString.getClass())) {
                    myObjectList.add(new MyString(param[i]));
                } else if (attributList.get(i).getMyclass().equals(myFloat.getClass())) {
                    myObjectList.add(new MyFloat(Float.parseFloat(param[i])));
                } else
                    throw new Exception("attritut non reconnu de " + attributList.get(i).getName());
            }
            // Insert the nuplet
            r.insert(new Nuplet(myObjectList));
            rRep.insert(new Nuplet(myObjectList));
        }
        return null;
    }

    // Obtenir les elements de projection
    public static String[] getProjectionElements(String str) {
        if (str.equals("*"))
            return null;
        // elt1 signe elt2 and etc
        String[] splited = str.split(",");
        return splited;
    }

    // Obtenir les conditions avec les and et or
    public static ListeConditions getConditionsObject(String str) throws Exception {
        // enlever les cotes d'abord
        str = str.replaceAll("'", "");

        // Obtenir les conditions
        String[] conditions = getConditions(str);

        int conditionsCount = getNombreConditions(conditions.length);
        if (conditionsCount == -1)
            throw new Exception("Erreur dans les conditions");
        // Obtenir les conditions concretement
        Condition[] listeCondition = new Condition[conditionsCount];
        String[] andOr = new String[conditionsCount - 1];
        int andOrIndex = 0;
        int listecConditionIndex = 0;
        for (int i = 0; i < listeCondition.length; i++) {
            if (listeCondition.length > 1 && i != 0) {
                andOr[andOrIndex] = conditions[(4 * i) - 1];
                andOrIndex++;
            }
            int tempI = (i * 4);
            Condition c = new Condition(conditions[tempI], conditions[tempI + 2], conditions[tempI + 1], false);
            listeCondition[listecConditionIndex] = c;
            listecConditionIndex++;
        }
        ListeConditions result = new ListeConditions(listeCondition, andOr);
        return result;
    }

    // Verifier la validite du nombre d'argument des conditions se succedant
    public static int getNombreConditions(int count) {
        int result = 1;
        int temp = 3;
        if (count < temp)
            result = -1;
        while (temp < count) {
            temp += 4;
            result++;
        }
        if (temp == count)
            return result;
        return -1;
    }

    // Obtenir les elements de toutes les conditions
    public static String[] getConditions(String str) {
        // elt1 signe elt2 and etc
        String[] splited = str.split(" ");
        return splited;
    }

}
