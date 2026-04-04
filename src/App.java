public class App {
    public static void main(String[] args) throws Exception {
        MultiServerMajuscule serv = new MultiServerMajuscule(10000);

        // Création clients
        ClientMajuscule c1 = new ClientMajuscule("client 1");
        ClientMajuscule c2 = new ClientMajuscule("client 2");
        ClientMajuscule c3 = new ClientMajuscule("client 3");

        //Création group 1
        Groupe g1 = new Groupe(1, "groupe1", 5);

        ThreadMajuscule th1 =
        //ajout clients au groupe 1
        serv.addClient(g1, c1);
        serv.addClient(g1, c2);
        serv.addClient(g1, c3);
        serv.start();
        c1.linkServer();
        c2.linkServer();
        c3.linkServer();
    }
}
