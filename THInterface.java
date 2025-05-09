import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.List;

public interface THInterface extends Remote {
    Map<String, Integer> listAvailableSeats() throws RemoteException;
    String bookSeats(String type, int number, String name) throws RemoteException;
    String cancelSeats(String type, int number, String name) throws RemoteException;
    Map<String, List<String>> getGuestBookings() throws RemoteException;
    void registerForNotification(String seatType, NotificationClient client) throws RemoteException;

    interface NotificationClient extends Remote {
        void notifyAvailable(String seatType) throws RemoteException;
    }
}