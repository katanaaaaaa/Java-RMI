import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class THServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            THInterface th = new THImpl();
            Naming.rebind("THService", th);
            System.out.println("Ο server είναι έτοιμος.");
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}