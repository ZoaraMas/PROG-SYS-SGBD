package JDBC;

import java.sql.ResultSet;
import java.util.ArrayList;

import Database.Relation;
import Database.ElementsRelation.Nuplet;

public class Bridge {
    public static MyResultSet relationToResultSet(Relation r) throws Exception {
        ArrayList<Nuplet> list = r.getData();
        if(list.size() == 0) throw new Exception("There is no data in the database");
        Object[][] tab = new Object[list.size()][list.get(0).getCount()];

        for(int i = 0; i < list.size(); i ++) {
            Nuplet temp = list.get(i);
            for(int j = 0; j < temp.getCount(); j ++) {
                tab[i][j] = temp.getList().get(j);
            }
        }
        return new MyResultSet(tab);
    }
}
