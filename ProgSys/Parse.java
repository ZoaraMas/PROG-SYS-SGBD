package ProgSys;

public class Parse {
    public static String[] parseIt(String query) throws Exception {
        String[] tab = query.split(" ");
        String operationType = getOperationType(tab);
        String[] result = new String[3];
        if (operationType.equals("1")) {
            result[0] = getTableName(tab);
            result[1] = getConditions(tab);
            result[2] = getProjection(tab);
        } else if (operationType.equals("2")) {
            result[0] = getTableNameInsert(tab); // nom de table
            result[1] = getInsertValues(query); // Valeurs de l'insert
        } else if (operationType.equals("3")) {
            if(tab.length < 7) throw new Exception("syntax error");
            result[0] = getCreateTableName(tab); // nom de table
            result[1] = getCreatedTableColumns(tab);
        }
        return result;
    }

    // Obtenir les colonnes de la table a creer
    public static String getCreatedTableColumns(String[] tab) {
        int index = getIndex(tab, "(") + 1;
        String result = new String();
        for(int i = index; i < tab.length - 2; i ++) {
            result += (tab[i] + " ");
        }
        result = result.replaceAll(",", "");
        return result;
    }

    // Get the name of the table to create
    public static String getCreateTableName(String[] tab) {
        return "" + 3 + "" + tab[2];
    }
    public static String getInsertValues(String s) {
        int valuesIndex = s.indexOf("values");
        String temp = s.substring(valuesIndex + 6);
        // Enlever tout les parenteses et espaces
        temp = temp.replaceAll("\\)", "");
        temp = temp.replaceAll("\\(", "");
        temp = temp.replaceAll("'", "");
        temp = temp.replaceAll("(?<![a-zA-Z0-9]) ", "");
        temp = temp.replaceAll(" (?![a-zA-Z0-9])", "");

        // Remplacer les virgules par des espaces simples
        // temp = temp.replaceAll(",", " ");
        return temp;
    }

    // Obtenir la table d'insert
    public static String getTableNameInsert(String[] tab) throws Exception {
        int intoIndex = getIndex(tab, "into");
        if (intoIndex == -1)
            throw new Exception("into not found");
        String operationType = getOperationType(tab);
        System.out.println("panda = " + operationType);
        String result = operationType + tab[intoIndex + 1];
        return result;
    }

    // Obtenir les elements de projections
    public static String getProjection(String[] tab) throws Exception {
        int fromIndex = getIndex(tab, "from");
        if (fromIndex == -1)
            throw new Exception("from not found");
        if (tab[1].equals("*"))
            return "*";
        String result = new String();
        for (int i = 1; i < fromIndex; i++) {
            result += tab[i];
        }
        return result;
    }

    // Obtenir les conditions
    public static String getConditions(String[] tab) throws Exception {
        int whereIndex = getIndex(tab, "where");
        if (whereIndex == -1)
            return null;
        String result = new String();
        for (int i = whereIndex + 1; i < tab.length; i++) {
            result += (tab[i] + " ");
        }
        return result;
    }

    // Obtenir la table d'operation et le type d'operation
    public static String getTableName(String[] tab) throws Exception {
        int fromIndex = getIndex(tab, "from");
        if (fromIndex == -1)
            throw new Exception("from not found");
        String operationType = getOperationType(tab);
        String result = operationType + tab[fromIndex + 1];
        return result;
    }

    // Obtenir le type d'operation
    public static String getOperationType(String[] tab) throws Exception {
        if (tab[0].equals("select"))
            return "1";
        else if (tab[0].equals("insert"))
            return "2";
        else if (tab[0].equals("create"))
            return "3";
        else
            throw new Exception("Operation must be select or insert");
    }

    // get String index
    public static int getIndex(String[] tab, String target) {
        for (int i = 0; i < tab.length; i++) {
            if (tab[i].equals(target))
                return i;
        }
        return -1;
    }
}
