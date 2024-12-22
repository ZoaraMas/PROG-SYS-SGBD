package Database.ElementsRelation;

import java.io.Serializable;
import java.util.ArrayList;

import Database.Relation;
import Database.Types.MyType;

public class Nuplet implements Cloneable, Serializable {
    private ArrayList<MyType> list;
    private int count;

    public Nuplet() {
        this.list = new ArrayList<>();
        this.count = 0;
    }

    public void addFirst(MyType temp) {
        this.list.addFirst(temp);
    }

    public void addLast(MyType temp) {
        this.list.addLast(temp);
    }

    public Nuplet clone() {
        try {
            Nuplet result = (Nuplet) super.clone();
            ArrayList<MyType> temp = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < this.list.size(); i++) {

                if (this.list.get(i) == null)
                    temp.add(null);
                else
                    temp.add(this.list.get(i).clone());
                count++;
            }
            result.setList(temp);
            result.setCount(count);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Check si un nuplet appartient a une relation avec un nombre de colonnes plus
    // grand
    public boolean insideRelation(Relation r, String compSide) throws Exception {
        for (int i = 0; i < r.getData().size(); i++) {
            Nuplet temp = r.getData().get(i);
            if (this.equalsInside(temp, compSide))
                return true;
        }
        return false;
    }

    // Comparer un nuplet a un nuplet plus grand qui pourrait le contenir soit
    // depuis le debut soit depuis la fin
    public boolean equalsInside(Nuplet target, String compSide) throws Exception {
        if (target.getCount() < this.count)
            throw new Exception("Le nuplet qui contient ce nuplet doit etre plus grand");
        if (compSide.equals("left")) {
            for (int i = 0; i < this.list.size(); i++) {
                // Si l'un des elements est nul
                if (this.getList().get(i) == null || target.getList().get(i) == null) {
                    if (this.getList().get(i) == null && target.getList().get(i) == null) {
                    } else
                        return false;
                } else if (!this.getList().get(i).getClass().equals(target.getList().get(i).getClass())
                        || !this.getList().get(i).equals(target.getList().get(i)))
                    return false;
            }
        } else if (compSide.equals("right")) {
            int lastIndex = this.getCount() - 1;
            int targetLastIndex = target.getCount() - 1;
            for (int i = 0; i < this.getCount(); i++) {
                if (this.getList().get(lastIndex - i) == null || target.getList().get(targetLastIndex - i) == null) {
                    if (this.getList().get(lastIndex - i) == null
                            && target.getList().get(targetLastIndex - i) == null) {
                    } else
                        return false;
                } else if (!this.getList().get(lastIndex - i).getClass()
                        .equals(target.getList().get(targetLastIndex - i).getClass())
                        || !this.getList().get(lastIndex - i).equals(target.getList().get(targetLastIndex - i)))
                    return false;
            }
        }
        return true;
    }

    public String getDisplay() {
        String result = new String();
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i) == null)
                result += "null - ";
            else
                result += (this.list.get(i).getValue() + " - ");
        }
        return result;
    }

    public boolean equals(Nuplet target) {
        if (target.getCount() == this.count) {
            for (int i = 0; i < this.list.size(); i++) {
                if (this.getList().get(i) == null || target.getList().get(i) == null) {
                    if (this.getList().get(i) == null && target.getList().get(i) == null) {
                    } else
                        return false;
                } else if (!this.getList().get(i).getClass().equals(target.getList().get(i).getClass())
                        || !this.getList().get(i).equals(target.getList().get(i)))
                    return false;
            }
        } else
            return false;
        return true;
    }

    public Nuplet(ArrayList<MyType> list) {
        this.list = list;
        this.count = this.list.size();
    }

    public void display() {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i) == null)
                System.out.print("null - ");
            else
                System.out.print(this.list.get(i).getValue() + " - ");
        }
    }

    public void addMyObjects(ArrayList<MyType> myList) {
        for (int k = 0; k < myList.size(); k++) {
            this.list.add(myList.get(k));
        }
    }

    public void add(MyType temp) throws Exception {
        this.list.add(temp);
        this.count++;
    }

    public ArrayList<MyType> getList() {
        return list;
    }

    public int getCount() {
        return count;
    }

    public void setList(ArrayList<MyType> list) {
        this.list = list;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
