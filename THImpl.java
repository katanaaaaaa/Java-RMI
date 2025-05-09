import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class THImpl extends UnicastRemoteObject implements THInterface {

    private Map<String, Integer> availableSeats;
    private Map<String, List<String>> guestBookings;
    private Map<String, List<NotificationClient>> notificationLists;

    public THImpl() throws RemoteException {
        super();
        availableSeats = new HashMap<>();
        availableSeats.put("A", 100);
        availableSeats.put("B", 200);
        availableSeats.put("C", 300);
        availableSeats.put("E", 250);
        availableSeats.put("T", 50);

        guestBookings = new HashMap<>();
        notificationLists = new HashMap<>();
        for (String type : availableSeats.keySet()) {
            notificationLists.put(type, new ArrayList<>());
        }
    }

    @Override
    public synchronized String bookSeats(String type, int number, String name) throws RemoteException {
        type = type.trim().toUpperCase();
        if (!availableSeats.containsKey(type)) {
            return "Ο τύπος θέσης δεν είναι έγκυρος.";
        }

        int available = availableSeats.get(type);

        if (available >= number) {
            availableSeats.put(type, available - number);
            guestBookings.putIfAbsent(name, new ArrayList<>());
            guestBookings.get(name).add(number + " θέσεις τύπου " + type);

            int price = getPricePerType(type);
            return "Κράτηση επιτυχής. Κόστος: " + (number * price) + " Ευρώ.";
        } else if (available > 0) {
            return "Υπάρχουν μόνο " + available + " διαθέσιμες θέσεις τύπου " + type + ".";
        } else {
            return "Δεν υπάρχουν διαθέσιμες θέσεις τύπου " + type + ".";
        }
    }

    private int getPricePerType(String type) {
        switch (type) {
            case "A":
                return 50;
            case "B":
                return 40;
            case "C":
                return 30;
            case "D":
                return 35;
            case "E":
                return 25;
            default:
                return 0;
        }
    }

    @Override
    public synchronized String cancelSeats(String type, int number, String name) throws RemoteException {
        type = type.trim().toUpperCase();
        if (!guestBookings.containsKey(name)) {
            return "Δεν υπάρχει κράτηση για τον χρήστη " + name;
        }

        List<String> bookings = guestBookings.get(name);
        int found = 0;
        List<String> toRemove = new ArrayList<>();

        for (String entry : bookings) {
            if (entry.contains("θέσεις τύπου " + type)) {
                int n = Integer.parseInt(entry.split(" ")[0]);
                found += n;
                toRemove.add(entry);
                if (found >= number)
                    break;
            }
        }

        if (found < number) {
            return "Ο χρήστης " + name + " δεν έχει " + number + " θέσεις τύπου " + type + " για ακύρωση.";
        }

        for (String entry : toRemove) {
            bookings.remove(entry);
        }
        availableSeats.put(type, availableSeats.get(type) + number);

        if (bookings.isEmpty()) {
            guestBookings.remove(name);
        }

        List<NotificationClient> clients = notificationLists.getOrDefault(type, new ArrayList<>());
        Iterator<NotificationClient> iterator = clients.iterator();

        while (iterator.hasNext()) {
            try {
                NotificationClient client = iterator.next();
                client.notifyAvailable(type);
            } catch (Exception e) {
                iterator.remove();
            }
        }

        return "Ακύρωση επιτυχής.";
    }

    @Override
    public synchronized Map<String, Integer> listAvailableSeats() throws RemoteException {
        return availableSeats;
    }

    @Override
    public synchronized Map<String, List<String>> getGuestBookings() throws RemoteException {
        return guestBookings;
    }

    @Override
    public synchronized void registerForNotification(String seatType, NotificationClient client)
            throws RemoteException {
        notificationLists.get(seatType).add(client);
    }
}