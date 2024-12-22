package Ensembles.PackageEnsemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.io.Serializable;
import java.lang.Object;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* To do list
    (OK)-card
    (ok)-apartenance
    (ok)-intersection
    (ok)-union
    (ok)-difference
*/

public class Ensemble implements Serializable{
    private String name;
    private ArrayList<Object> list;

    private Class<?> myClass;
    // private Method myMethod;
    // the method that directly returns the value
    private transient Method myEqualsMethod;
    private String myTarget; // needed to get the main method
    private int card;

    // If the list is null after the constructor, there are doublons in the list
    public Ensemble(String name, ArrayList<Object> list, String target) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        setName(name);
        setList(list);
        setMyTarget(target);
        setMyClass(list.get(0).getClass());
        // setMyMethod(myClass.getMethod("get" + target));
        setMyEqualsMethod(myClass.getMethod("equals", Object.class));
        this.afficher();
        removeDoublons();
        setCard(card());
    }

    public Ensemble(String name, ArrayList<Object> list, String target, Object o)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        setName(name);
        setList(list);
        setMyTarget(target);
        setMyClass(list.get(0).getClass());
        // setMyMethod(myClass.getMethod("get" + target));
        setMyEqualsMethod(myClass.getMethod("equals", Object.class));
        // removeDoublons();
        setCard(card());
    }

    public String describe() {
        String result = new String();
        for(int i = 0; i < this.list.size(); i ++) {
            result += this.list.get(i).toString() + "\n";
        }
        return result;
    }
    
    public Method getMethode(Object o) throws NoSuchMethodException, SecurityException {
        Class<?> c = o.getClass();
        return c.getMethod("get" + "Value");
    }

    public static void reflectSort(ArrayList<Object> list, String attributeName, boolean ascending)
            throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, SecurityException {
        if (list == null || list.isEmpty() || attributeName == null || attributeName.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments provided.");
        }

        Class<?> myClass = list.get(0).getClass();
        Method method = myClass.getMethod("get" + attributeName);
        // Sort using a comparator that leverages reflection
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    Object value1 = method.invoke(o1);
                    Object value2 = method.invoke(o2);

                    if (value1 instanceof Comparable && value2 instanceof Comparable) {
                        int comp = ((Comparable) value1).compareTo(value2);
                        return ascending ? comp : -comp;
                    } else {
                        throw new IllegalArgumentException("Field values are not comparable.");
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to access field values.", e);
                }
            }
        });
    }

    // Compare this ensemble with another one
    public boolean myEquals(Ensemble e) {
        // Les elements de comparaisons: classe des elements, la taille et les valeurs a
        // l'interieur
        if (e.getMyClass().equals(this.myClass) && this.list.size() == e.getList().size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).equals(e.getList().get(i)))
                    return false;
            }
        } else
            return false;
        return true;
    }

    // Checks if an object is inside an array of the same type of object
    public static boolean checkInArray(ArrayList<Object> list, Object target) {
        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i)))
                return true;
        }
        return false;
    }

    // Because this a reflect method, it returns an arrayy of objects, so we still
    // have to cast before using the datas
    public static ArrayList<Object> ReflectSort(ArrayList<Object> list, String target) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ArrayList<Object> result = new ArrayList<Object>();
        // Decroissant
        Class<?> myClass = list.get(0).getClass();

        for (int i = 0; i < list.size(); i++) {
            int startIndex = 0;
            Object temp = list.get(startIndex);
            // Temp is the starting point
            // we must ensure that temp is still not used
            while (checkInArray(result, temp)) {
                if (startIndex == list.size() - 1)
                    break;
                startIndex++;
                temp = list.get(startIndex);
            }
            // The real operation begins here
            for (int j = 0; j < list.size(); j++) {
                if (!checkInArray(result, list.get(j)) && j != startIndex) {
                    Method method = myClass.getMethod("get" + target);
                    if (((Number) method.invoke(temp)).doubleValue() < ((Number) method.invoke(list.get(j)))
                            .doubleValue()) {
                        temp = list.get(j);
                    }
                }
            }
            result.add(temp);
        }
        return result;
    }

    // Verify if there are not any doublons
    // Traditional 2 loops
    public boolean checkDoublons() {
        for (int i = 0; i < this.card; i++) {
            for (int j = i + 1; j < this.card; j++) {
                if (this.list.get(i).equals(this.list.get(j)))
                    return true;
            }
        }
        return false;
    }

    public ArrayList<Object> getListInString() {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            list.add(this.list.get(i).toString());
        }
        return list;
    }

    public static ArrayList<ArrayList<Object>> groupByClass(ArrayList<Object> objects) {
        // A map to group objects by their class type
        HashMap<Class<?>, ArrayList<Object>> groupedObjects = new HashMap<>();

        // Iterate through the input list
        for (Object obj : objects) {
            // Get the class of the current object
            Class<?> objClass = obj.getClass();

            // If the class is not in the map, add it with a new ArrayList
            if (!groupedObjects.containsKey(objClass)) {
                groupedObjects.put(objClass, new ArrayList<>());
            }

            // Add the object to the appropriate group
            groupedObjects.get(objClass).add(obj);
        }

        // Convert the values of the map into an ArrayList of ArrayLists
        return new ArrayList<>(groupedObjects.values());
    }

    ArrayList<Object> combine2DArrayList(ArrayList<ArrayList<Object>> list) {
        ArrayList<Object> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Object> temp = list.get(i);
            for (int j = 0; j < temp.size(); j++) {
                result.add(temp.get(j));
            }
        }
        return result;
    }

    // Using sort to clear doublons
    public void removeDoublons() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        ArrayList<ArrayList<Object>> list = groupByClass(this.list);
        for (int i = 0; i < list.size(); i++) {
            removeDoublons(list.get(i));
        }
        this.list = combine2DArrayList(list);
    }

    // Remove Doublon for an ArrayList for the same type
    public void removeDoublons(ArrayList<Object> list)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        reflectSort(list, this.myTarget, false); // Decroissant
        ArrayList<Object> sortedList = list;
        // Main operation
        for (int i = 0; i < sortedList.size(); i++) {
            int j = i + 1;
            if (j == sortedList.size())
                break;
            Comparable<Object> aComp = ((Comparable<Object>) getMethode(sortedList.get(i)).invoke(sortedList.get(i)));
            Comparable<Object> bComp = ((Comparable<Object>) getMethode(sortedList.get(j)).invoke(sortedList.get(j)));
            // On repete pour faire en sorte que tout des doublons soient elimines
            while (j != sortedList.size() && aComp.compareTo(bComp) == 0) {
                sortedList.remove(j);
                if (j == sortedList.size())
                    break;
                bComp = (Comparable<Object>) getMethode(sortedList.get(j)).invoke(sortedList.get(j));
            }
        }
        list = sortedList;
    }

    public int card() {
        return this.list.size();
    }

    // insert while preventing doublons
    public void insertObject(Object target) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        this.list.add(target);
        removeDoublons();
    }

    // Get single value
    public ArrayList<Object> getVal()
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        ArrayList<Object> result = new ArrayList<>();
        for (int i = 0; i < this.card; i++) {
            if (this.list.get(i) == null)
                result.add(null);
            else
                result.add(getMethode(this.list.get(i)).invoke(this.list.get(i)));
        }
        return result;
    }

    // Display the content of this ensemble
    public void afficher() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        System.out.print(this.name + " = {");
        for (int i = 0; i < this.list.size(); i++) {
            if (i != 0)
                System.out.print(", ");
            System.out.print((getMethode(this.list.get(i)).invoke(this.list.get(i))));
            if (i == this.list.size() - 1)
                System.out.print(" }");
        }
    }

    public boolean appartenance(Object target)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        setMyEqualsMethod(myClass.getMethod("equals", Object.class));
        for (int i = 0; i < this.list.size(); i++) {
            if ((Boolean) this.myEqualsMethod.invoke(this.list.get(i), target))
                return true;
        }
        return false;
    }

    public static Method getEqualsMethod(Object o) throws NoSuchMethodException, SecurityException {
        return o.getClass().getDeclaredMethod("customEquals", Object.class);
    }

    public static boolean checkInArrayEnsemble(ArrayList<Object> list, Object target) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        for (int i = 0; i < list.size(); i++) {
            if ((Boolean) getEqualsMethod(target).invoke(list.get(i), target))
                return true;
        }
        return false;
    }

    public static int getObjectIndexFromList(ArrayList<Object> list, Object target) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        for (int i = 0; i < list.size(); i++) {
            if ((Boolean) getEqualsMethod(target).invoke(list.get(i), target))
                return i;
        }
        return -1;
    }

    public ArrayList<Object> intersection(Ensemble E) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                setMyEqualsMethod(myClass.getMethod("equals", Object.class));

        // We group all the values inside one single array
        ArrayList<Object> array = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            array.add(this.list.get(i));
        }
        for (int i = 0; i < E.card(); i++) {
            array.add(E.getList().get(i));
        }
        ArrayList<Object> sortedList = ReflectSort(array, this.myTarget);

        ArrayList<Object> result = new ArrayList<>();
        // Keep the values that are equals next to each other
        for (int i = 0; i < sortedList.size(); i++) {
            // 2e condition pas encore necessaire pour l'intersection de 2 ensembles
            if (i != sortedList.size() - 1 && !checkInArrayEnsemble(result, sortedList.get(i))
                    && (Boolean) this.myEqualsMethod.invoke(sortedList.get(i), sortedList.get(i + 1))) {
                result.add(sortedList.get(i));
            }
        }
        return result;
    }

    public ArrayList<Object> union(Ensemble E) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // Just insert into the result array if it still hasn't been inserted
        ArrayList<Object> array = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            if (!checkInArrayEnsemble(array, this.list.get(i)))
                array.add(this.list.get(i));
        }
        for (int i = 0; i < E.card(); i++) {
            if (!checkInArrayEnsemble(array, E.getList().get(i)))
                array.add(E.getList().get(i));
        }
        return array;
    }

    public ArrayList<Object> difference(Ensemble E) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // A - B = C
        // return C then
        ArrayList<Object> array = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            array.add(this.list.get(i));
        }
        for (int i = 0; i < E.card(); i++) {
            if (checkInArrayEnsemble(array, E.getList().get(i))) {
                array.remove(getObjectIndexFromList(array, E.getList().get(i)));
                // System.out.println("heeere");
            }
        }
        return array;
    }

    public ArrayList<Object> getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
    }

    public Class<?> getMyClass() {
        return myClass;
    }

    public void setMyClass(Class<?> myClass) {
        this.myClass = myClass;
    }

    // public Method getMyMethod() {
    // return myMethod;
    // }

    // public void setMyMethod(Method myMethod) {
    // this.myMethod = myMethod;
    // }

    public Method getMyEqualsMethod() throws NoSuchMethodException, SecurityException {
        setMyEqualsMethod(myClass.getMethod("equals", Object.class));

        return myEqualsMethod;
    }

    public void setMyEqualsMethod(Method myEqualsMethod) {
        this.myEqualsMethod = myEqualsMethod;
    }

    public String getMyTarget() {
        return myTarget;
    }

    public void setMyTarget(String myTarget) {
        this.myTarget = myTarget;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

}