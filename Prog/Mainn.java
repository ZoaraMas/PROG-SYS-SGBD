package Prog;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import JDBC.MyConnection;
import JDBC.MyDriver;
import JDBC.MyStatement;

public class Mainn {
    public static void main(String[] args) throws InterruptedException {
        /// Client
        // Informations de connexion à la base de données Oracle
        String url = "jdbc:mysgbd://127.0.0.1:12356/db"; // URL exemple: jdbc:mysgbd://local:12345/db
        String user = "root"; // Votre nom d'utilisateur
        String password = "admin"; // Votre mot de passe

        // Appel du driver
        MyDriver my = new MyDriver();

        MyConnection conn = null;
        MyStatement stmt = null;
        try {
            // Etablir une connexion à la base de données
            System.out.println("gonna connect");
            conn = (MyConnection) DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la sgbd!");

            // Créer un objet Statement pour exécuter des requêtes SQL
            stmt = (MyStatement) conn.createStatement();

            // Exemple de requête SQL
            // String query = "insert into CS values ('Archi', 'S1')"; // Remplacez par
            // votre table
            String select = "select * from Voiture"; // Remplacez par votre table
            String query = "insert into Voiture values (Citroen, 3 CV, Durand)"; // Remplacez par votre table
            //
            // int result = stmt.executeUpdate(query);
            ResultSet resultSet = stmt.executeQuery(select);
            // ResultSet resultSet = null;
            int count = 0;
            if (resultSet != null) {
                System.out.println("result set not null");
                while (resultSet.next()) {
                    count++;
                    System.out.println(count + " \n");
                    System.out.println(resultSet.getString(1));
                    System.out.println(resultSet.getString(2));
                    System.out.println(resultSet.getString(3));
                    System.out.println("=====================");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur loresultSet de l'exécution de la requête");
            e.printStackTrace();
        } finally {
            Thread.sleep(5000);
            // Fermer les ressources
            try {
                if (conn != null)
                    conn.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
