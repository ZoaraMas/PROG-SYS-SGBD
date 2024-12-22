package Aff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Database.DB;
import Database.Relation;
import Database.ElementsRelation.Attribut;
import Database.ElementsRelation.Condition;
import Database.ElementsRelation.Domaine;
import Database.ElementsRelation.Nuplet;
import Database.Types.MyInt;
import Database.Types.MyString;
import Database.Types.MyType;
import Ensembles.PackageEnsemble.Ensemble;
import JDBC.Bridge;
import JDBC.MyResultSet;
import ProgSys.Message;
import ProgSys.MyPacket;
import ProgSys.Parse;
import SAVEDATA.TextReader;
import Serialization.Serialize;

public class Mai {
    // Tsinjo
    public static void main(String[] args) throws Exception {
        /// Tsinjo

        System.out.println("\n\n=======TEST DE RELATION ==========");
        // CJH
        // Attribut idCours
        ArrayList<Object> listDomaine = new ArrayList<>();
        listDomaine.add(new MyString("Archi"));
        listDomaine.add(new MyString("Algo"));
        listDomaine.add(new MyString("Syst"));

        Ensemble idCoursEnsemble = new Ensemble("idCoursEnsemble", listDomaine, "Value");
        Domaine idCoursDomaine = new Domaine(idCoursEnsemble);
        Attribut CJHidCours = new Attribut("idCours", idCoursDomaine);

        // Attribut Jour
        listDomaine = new ArrayList<>();
        listDomaine.add(new MyString("Lu"));
        listDomaine.add(new MyString("Ma"));
        listDomaine.add(new MyString("Ve"));

        Ensemble jourEnsemble = new Ensemble("jourEnsemble", listDomaine, "Value");
        Domaine jourDomaine = new Domaine(jourEnsemble);
        Attribut CJHJour = new Attribut("Jour", jourDomaine);

        // Attribut Heure
        listDomaine = new ArrayList<>();
        listDomaine.add(new MyString("9h"));
        listDomaine.add(new MyString("14h"));

        Ensemble heureEnsemble = new Ensemble("heureEnsemble", listDomaine, "Value");
        Domaine heureDomaine = new Domaine(heureEnsemble);
        Attribut CJHHeure = new Attribut("heure", heureDomaine);

        // Liste attribut
        ArrayList<Attribut> CJHattributList = new ArrayList<>();
        CJHattributList.add(CJHidCours.clone());
        CJHattributList.add(CJHJour.clone());
        CJHattributList.add(CJHHeure.clone());

        // Data
        ArrayList<MyType> CJHmyObjectList = new ArrayList<>();
        CJHmyObjectList.add(new MyString("Archi"));
        CJHmyObjectList.add(new MyString("Lu"));
        CJHmyObjectList.add(new MyString("9h"));

        ArrayList<Nuplet> CJHNupletList = new ArrayList<>();
        CJHNupletList.add(new Nuplet(CJHmyObjectList));

        CJHmyObjectList = new ArrayList<>();
        CJHmyObjectList.add(new MyString("Algo"));
        CJHmyObjectList.add(new MyString("Ma"));
        CJHmyObjectList.add(new MyString("9h"));

        CJHNupletList.add(new Nuplet(CJHmyObjectList));

        CJHmyObjectList = new ArrayList<>();
        CJHmyObjectList.add(new MyString("Algo"));
        CJHmyObjectList.add(new MyString("Ve"));
        CJHmyObjectList.add(new MyString("9h"));

        CJHNupletList.add(new Nuplet(CJHmyObjectList));

        CJHmyObjectList = new ArrayList<>();
        CJHmyObjectList.add(new MyString("Syst"));
        CJHmyObjectList.add(new MyString("Ma"));
        CJHmyObjectList.add(new MyString("14h"));

        CJHNupletList.add(new Nuplet(CJHmyObjectList));

        Relation CJH = new Relation("CJH", CJHattributList, CJHNupletList);
        CJH.display();

        // CS
        // Attribut idSalle
        listDomaine = new ArrayList<>();
        listDomaine.add(new MyString("S1"));
        listDomaine.add(new MyString("S2"));

        Ensemble idSalleEnsemble = new Ensemble("idSalleEnsemble", listDomaine, "Value");
        Domaine idSalleDomaine = new Domaine(idSalleEnsemble);
        Attribut CSidSalle = new Attribut("idSalle", idSalleDomaine);

        Attribut CSidCours = new Attribut("idCours", idCoursDomaine);

        // Liste attribut
        ArrayList<Attribut> CSattributList = new ArrayList<>();
        CSattributList.add(CSidCours.clone());
        CSattributList.add(CSidSalle.clone());

        // Data
        ArrayList<MyType> CSmyObjectList = new ArrayList<>();
        CSmyObjectList.add(new MyString("Archi"));
        CSmyObjectList.add(new MyString("S1"));

        ArrayList<Nuplet> CSNupletList = new ArrayList<>();
        CSNupletList.add(new Nuplet(CSmyObjectList));

        CSmyObjectList = new ArrayList<>();
        CSmyObjectList.add(new MyString("Algo"));
        CSmyObjectList.add(new MyString("S2"));

        CSNupletList.add(new Nuplet(CSmyObjectList));

        CSmyObjectList = new ArrayList<>();
        CSmyObjectList.add(new MyString("Syst"));
        CSmyObjectList.add(new MyString("S1"));

        CSNupletList.add(new Nuplet(CSmyObjectList));

        Relation CS = new Relation("CS", CSattributList, CSNupletList);
        CS.display();

        // ENA
        // Attribut idEtudiant
        listDomaine = new ArrayList<>();
        listDomaine.add(new MyInt(100));
        listDomaine.add(new MyInt(200));
        listDomaine.add(new MyInt(300));

        Ensemble idEtudiantEnsemble = new Ensemble("idEtudiantEnsemble", listDomaine, "Value");
        Domaine idEtudiantDomaine = new Domaine(idEtudiantEnsemble);
        Attribut ENAidEtudiant = new Attribut("idEtudiant", idEtudiantDomaine);

        // Attribut Nom
        listDomaine = new ArrayList<>();
        listDomaine.add(new MyString("Toto"));
        listDomaine.add(new MyString("Tata"));
        listDomaine.add(new MyString("Titi"));

        Ensemble nomEnsemble = new Ensemble("nomEnsemble", listDomaine, "Value");
        Domaine nomDomaine = new Domaine(nomEnsemble);
        Attribut ENAnom = new Attribut("Nom", nomDomaine);

        // Attribut Adresse
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("Nice"));
        listDomaine.add(new MyString("Paris"));
        listDomaine.add(new MyString("Rome"));

        Ensemble adresseEnsemble = new Ensemble("adresseEnsemble", listDomaine, "Value");
        Domaine adresseDomaine = new Domaine(adresseEnsemble);
        Attribut ENAadresse = new Attribut("adresse", adresseDomaine);

        // Liste attribut
        ArrayList<Attribut> ENAattributList = new ArrayList<>();
        ENAattributList.add(ENAidEtudiant.clone());
        ENAattributList.add(ENAnom.clone());
        ENAattributList.add(ENAadresse.clone());

        // Data
        ArrayList<MyType> ENAmyObjectList = new ArrayList<>();
        ENAmyObjectList.add(new MyInt(100));
        ENAmyObjectList.add(new MyString("Toto"));
        ENAmyObjectList.add(new MyString("Nice"));

        ArrayList<Nuplet> ENANupletList = new ArrayList<>();
        ENANupletList.add(new Nuplet(ENAmyObjectList));

        ENAmyObjectList = new ArrayList<>();
        ENAmyObjectList.add(new MyInt(200));
        ENAmyObjectList.add(new MyString("Tata"));
        ENAmyObjectList.add(new MyString("Paris"));

        ENANupletList.add(new Nuplet(ENAmyObjectList));

        ENAmyObjectList = new ArrayList<>();
        ENAmyObjectList.add(new MyInt(300));
        ENAmyObjectList.add(new MyString("Titi"));
        ENAmyObjectList.add(new MyString("Rome"));

        ENANupletList.add(new Nuplet(ENAmyObjectList));

        Relation ENA = new Relation("ENA", ENAattributList, ENANupletList);
        ENA.display();

        // CEN
        // Attribut Note
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("A"));
        listDomaine.add(new MyString("B"));
        listDomaine.add(new MyString("C"));

        Ensemble noteEnsemble = new Ensemble("noteEnsemble", listDomaine, "Value");
        Domaine noteDomaine = new Domaine(noteEnsemble);
        Attribut CENnote = new Attribut("Note", noteDomaine);

        Attribut CENidCours = new Attribut("idCours", idCoursDomaine);
        Attribut CENidEtudiant = new Attribut("idEtudiant", idEtudiantDomaine);

        // Liste attribut
        ArrayList<Attribut> CENattributList = new ArrayList<>();
        CENattributList.add(CENidCours.clone());
        CENattributList.add(CENidEtudiant.clone());
        CENattributList.add(CENnote.clone());

        // Data
        ArrayList<MyType> CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Archi"));
        CENmyObjectList.add(new MyInt(100));
        CENmyObjectList.add(new MyString("A"));

        ArrayList<Nuplet> CENNupletList = new ArrayList<>();
        CENNupletList.add(new Nuplet(CENmyObjectList));

        CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Archi"));
        CENmyObjectList.add(new MyInt(300));
        CENmyObjectList.add(new MyString("A"));

        CENNupletList.add(new Nuplet(CENmyObjectList));

        CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Syst"));
        CENmyObjectList.add(new MyInt(100));
        CENmyObjectList.add(new MyString("B"));

        CENNupletList.add(new Nuplet(CENmyObjectList));

        CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Syst"));
        CENmyObjectList.add(new MyInt(200));
        CENmyObjectList.add(new MyString("A"));

        CENNupletList.add(new Nuplet(CENmyObjectList));

        CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Syst"));
        CENmyObjectList.add(new MyInt(300));
        CENmyObjectList.add(new MyString("B"));

        CENNupletList.add(new Nuplet(CENmyObjectList));

        CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Algo"));
        CENmyObjectList.add(new MyInt(100));
        CENmyObjectList.add(new MyString("C"));

        CENNupletList.add(new Nuplet(CENmyObjectList));

        CENmyObjectList = new ArrayList<>();
        CENmyObjectList.add(new MyString("Algo"));
        CENmyObjectList.add(new MyInt(200));
        CENmyObjectList.add(new MyString("A"));

        CENNupletList.add(new Nuplet(CENmyObjectList));

        Relation CEN = new Relation("CEN", CENattributList, CENNupletList);

        // PROG SYS ////
        System.out.println("Prog Sys");

        String[] message = Parse.parseIt("select * from CJH where idCours = 'Archi'");
        // String[] message = new String[3];
        // message[0] = "1CJH";
        // message[1] = "idCours = 'Archi' and Jour != 'Ve' or idCours = 'Syst'";
        // message[2] = "idCours;Jour;heure";

        // Relation sys = Message.translate(message, db);

        // String disp = sys.getDisplay();
        // System.out.println(disp);
        // sys.display();
        System.out.println("====================================");

        // boolean equalsTest = CJH.getData().get(0).equals(CS.getData().get(0));
        boolean equalsTest = CJH.getData().get(1).equals(CJH.getData().get(0));
        System.out.println("la reponse est = " + equalsTest);

        CJH.display();
        // Creation d'un nuplet temporaire
        ArrayList<Nuplet> tempList = new ArrayList<>();

        ArrayList<MyType> oList = new ArrayList<>();
        oList.add(new MyString("Algo"));
        oList.add(new MyString("Ma"));

        Nuplet nupleta = new Nuplet(oList);
        CJH.insertSmallerNuplet(nupleta, "right");
        System.out.println("apres insertion de NUPLETA");
        CJH.display();

        System.out.println("QUESTIONS DE VOITURES");

        // Homme
        // Attribut Nom1
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("Dupont"));
        listDomaine.add(new MyString("Durand"));
        listDomaine.add(new MyString("Martin"));

        Ensemble Nom1Ensemble = new Ensemble("Nom1Ensemble", listDomaine, "Value");
        Domaine Nom1Domaine = new Domaine(Nom1Ensemble);
        Attribut HommeNom1 = new Attribut("Nom1", Nom1Domaine);

        // Attribut Prenom
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("Pierre"));
        listDomaine.add(new MyString("Jean"));
        listDomaine.add(new MyString("Georges"));

        Ensemble prenomEnsemble = new Ensemble("PrenomEnsemble", listDomaine, "Value");
        Domaine prenomDomaine = new Domaine(prenomEnsemble);
        Attribut HommePrenom = new Attribut("Prenom", prenomDomaine);

        // Attribut Age
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyInt(100));
        listDomaine.add(new MyString("Jean"));
        listDomaine.add(new MyString("Georges"));

        Domaine ageDomaine = new Domaine(new MyInt(18), new MyInt(80), "AgeEnsemble");
        Attribut HommeAge = new Attribut("Age", ageDomaine);

        ArrayList<Attribut> HommeAttributList = new ArrayList<>();
        HommeAttributList.add(HommeNom1);
        HommeAttributList.add(HommePrenom);
        HommeAttributList.add(HommeAge);

        ArrayList<Nuplet> HommeNupletList = new ArrayList<>();
        ArrayList<MyType> HommeMyObjectList = new ArrayList<>();
        HommeMyObjectList = new ArrayList<>();
        HommeMyObjectList.add(new MyString("Dupont"));
        HommeMyObjectList.add(new MyString("Pierre"));
        HommeMyObjectList.add(new MyInt(20));

        HommeNupletList.add(new Nuplet(HommeMyObjectList));

        HommeMyObjectList = new ArrayList<>();
        HommeMyObjectList.add(new MyString("Durand"));
        HommeMyObjectList.add(new MyString("Jean"));
        HommeMyObjectList.add(new MyInt(30));

        HommeNupletList.add(new Nuplet(HommeMyObjectList));

        HommeMyObjectList = new ArrayList<>();
        HommeMyObjectList.add(new MyString("Martin"));
        HommeMyObjectList.add(new MyString("Georges"));
        HommeMyObjectList.add(new MyInt(40));

        HommeNupletList.add(new Nuplet(HommeMyObjectList));

        Relation Homme = new Relation("Homme", HommeAttributList, HommeNupletList);
        Homme.display();

        ///

        // Voiture
        // Attribut type
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("Tesla"));
        listDomaine.add(new MyString("Citroen"));
        listDomaine.add(new MyString("Citroen"));

        Ensemble typeEnsemble = new Ensemble("TypeEnsemble", listDomaine, "Value");
        Domaine typeDomaine = new Domaine(typeEnsemble);
        Attribut VoitureType = new Attribut("Type", typeDomaine);

        // Attribut Marque
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("Model X"));
        listDomaine.add(new MyString("2 CV"));
        listDomaine.add(new MyString("3 CV"));

        Ensemble marqueEnsemble = new Ensemble("MarqueEnsemble", listDomaine, "Value");
        Domaine marqueDomaine = new Domaine(marqueEnsemble);
        Attribut VoitureMarque = new Attribut("Marque", marqueDomaine);

        // Attribut proprio
        listDomaine = new ArrayList<>();

        listDomaine.add(new MyString("Dupont"));
        listDomaine.add(new MyString("Durand"));

        Ensemble proprioEnsemble = new Ensemble("ProprioEnsemble", listDomaine, "Value");
        Domaine proprioDomaine = new Domaine(proprioEnsemble);
        Attribut VoitureProprio = new Attribut("Proprio", proprioDomaine);

        ArrayList<Attribut> VoitureAttributList = new ArrayList<>();
        VoitureAttributList.add(VoitureType);
        VoitureAttributList.add(VoitureMarque);
        VoitureAttributList.add(VoitureProprio);

        ArrayList<Nuplet> VoitureNupletList = new ArrayList<>();
        ArrayList<MyType> VoitureMyObjectList = new ArrayList<>();
        VoitureMyObjectList = new ArrayList<>();
        VoitureMyObjectList.add(new MyString("Tesla"));
        VoitureMyObjectList.add(new MyString("Model X"));
        VoitureMyObjectList.add(new MyString("Dupont"));

        VoitureNupletList.add(new Nuplet(VoitureMyObjectList));

        VoitureMyObjectList = new ArrayList<>();
        VoitureMyObjectList.add(new MyString("Citroen"));
        VoitureMyObjectList.add(new MyString("2 CV"));
        VoitureMyObjectList.add(new MyString("Durand"));

        VoitureNupletList.add(new Nuplet(VoitureMyObjectList));

        VoitureMyObjectList = new ArrayList<>();
        VoitureMyObjectList.add(new MyString("Citroen"));
        VoitureMyObjectList.add(new MyString("3 CV"));
        VoitureMyObjectList.add(null);

        VoitureNupletList.add(new Nuplet(VoitureMyObjectList));

        Relation Voiture = new Relation("Voiture", VoitureAttributList, VoitureNupletList);
        Voiture.display();

        Homme.display();

        System.out.println("PLACE A LA JOINTURE");

        // Relation test = Relation.tetaJointure(CJH, CS, "cjh", "cs", new
        // Condition("cjh.heure", "1", "=", false));
        // Relation Tetajoin = Relation.tetaJointure(Homme, Voiture, "h", "v",
        // new Condition("h.Nom1", "v.Proprio", "=", true));

        Relation join = Homme.jointure(Homme, Voiture, "h", "v",
                new Condition("h.Nom1", "v.Proprio", "=", true), "right");

        System.out.println("Homme");
        Homme.display();

        System.out.println("Voiture");
        Voiture.display();

        // join.display();
        VoitureMyObjectList = new ArrayList<>();
        VoitureMyObjectList.add(new MyString("Tesla"));
        VoitureMyObjectList.add(new MyString("2 CV"));
        VoitureMyObjectList.add(new MyString("Durand"));

        Nuplet temp = new Nuplet(VoitureMyObjectList);
        Voiture.insert(temp);

        Voiture.display();

        join.display();
        System.out.println("    =============      ");

        DB db = new DB();
        db.addRelation(CJH);
        db.addRelation(CEN);
        db.addRelation(CS);
        db.addRelation(ENA);
        db.addRelation(Voiture);

        Serialize.serialize(getPath(), db);
        // Voiture.display();
        // Test du produit cartesien
        // Relation prod = Relation.produitCartesien(Voiture, Homme, "h", "v");

        // PROG SYS ////

        // int port = Main.getPort(); // Port number the server will listen on
        // ServerSocket serverSocket = null;
        // Socket socket = null;
        // // try {
        // serverSocket = new ServerSocket(port);
        // System.out.println("Server is listening on port " + port);
        // socket = serverSocket.accept();
        // System.out.println("Client connected");

        // // Input and output streams
        // ObjectOutputStream outStream = new
        // ObjectOutputStream(socket.getOutputStream());

        // BufferedReader input = new BufferedReader(new
        // InputStreamReader(socket.getInputStream()));
        // PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        // // Communication loop
        // String clientMessage;
        // while ((clientMessage = input.readLine()) != null) {
        // System.out.println("Client: " + clientMessage);
        // if ("exit".equalsIgnoreCase(clientMessage)) {
        // System.out.println("Client disconnected");
        // break;
        // }
        // try {
        // String[] parseMessage = Parse.parseIt(clientMessage);
        // Relation temp = Message.translate(parseMessage, db);
        // String result = temp.getDisplay();
        // MyPacket pack = new MyPacket(result);

        // MyResultSet rs = Bridge.relationToResultSet(temp);
        // temp.display();
        // // rs.next();
        // // System.out.println("here = " + rs.getString(1));
        // outStream.writeObject(rs);
        // } catch (Exception e) {
        // System.out.println("Server: " + e.getMessage());
        // outStream.writeObject(
        // new MyPacket("Exception occured, please verify errors in your message and
        // retry again\nError: "
        // + e.getMessage()));
        // }
        // }
        // // } catch (IOException ex) {
        // // System.out.println("Server error: " + ex.getMessage());
        // // ex.printStackTrace();
        // // } finally {
        // try {
        // if (socket != null && !socket.isClosed()) {
        // socket.close();
        // System.out.println("Client socket closed.");
        // }
        // if (serverSocket != null && !serverSocket.isClosed()) {
        // serverSocket.close();
        // System.out.println("Server socket closed.");
        // }
        // } catch (IOException e) {
        // System.err.println("Error closing sockets: " + e.getMessage());
        // }
    }

    public static String getPath() throws FileNotFoundException, IOException {
        TextReader textReader = new TextReader();
        ArrayList<String[]> data = textReader.getFileData("Conf.txt");
        String path = data.get(1)[1];
        return path;
    }

    public static int getPort() throws FileNotFoundException, IOException {
        TextReader textReader = new TextReader();
        ArrayList<String[]> data = textReader.getFileData("Conf.txt");
        String port = data.get(0)[1];
        return Integer.parseInt(port);
    }

}
