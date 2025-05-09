import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class THClient extends UnicastRemoteObject implements THInterface.NotificationClient {
    protected THClient() throws Exception {
        super();
    }

    @Override
    public void notifyAvailable(String seatType) {
        System.out.println("📢 Διαθέσιμη θέση στον τύπο: " + seatType + "!");
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Όνομα χρήστη: ");
            String name = scanner.nextLine();

            THInterface th = (THInterface) Naming.lookup("rmi://localhost/THService");
            THClient client = new THClient();

            while (true) {
                System.out.println("\nΕπιλογές: list | book | cancel | guests | exit");
                System.out.print("Εντολή: ");
                String cmd = scanner.nextLine();

                if (cmd.equals("exit"))
                    break;
                switch (cmd) {
                    case "list":
                        Map<String, Integer> seats = th.listAvailableSeats();
                        seats.forEach((k, v) -> {
                            String label = switch (k) {
                                case "A" -> "ΠΑ (Πλατεία - Ζώνη Α)";
                                case "B" -> "ΠΒ (Πλατεία - Ζώνη Β)";
                                case "C" -> "ΠΓ (Πλατεία - Ζώνη Γ)";
                                case "E" -> "ΚΕ (Κεντρικός Εξώστης)";
                                case "T" -> "ΠΘ (Πλαϊνά Θεωρεία)";
                                default -> "Άγνωστος τύπος";
                            };
                            int price = switch (k) {
                                case "A" -> 50;
                                case "B" -> 40;
                                case "C" -> 30;
                                case "E" -> 35;
                                case "T" -> 25;
                                default -> 0;
                            };
                            System.out.println(
                                    v + " θέσεις τύπου " + label + " που κοστίζουν " + price + " Ευρώ η κάθε μία");
                        });
                        break;
                    case "book":
                        System.out.print("Τύπος θέσης (A/B/C/E/T): ");
                        String type = scanner.nextLine();
                        System.out.print("Αριθμός θέσεων: ");
                        int num = Integer.parseInt(scanner.nextLine());
                        String result = th.bookSeats(type, num, name);
                        System.out.println(result);
                        if (result.contains("Δεν υπάρχουν διαθέσιμες")) {
                            System.out.print("Θέλετε να ειδοποιηθείτε όταν ελευθερωθεί θέση; (Y/): ");
                            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                                th.registerForNotification(type, client);
                                System.out.println("Εγγραφήκατε για ειδοποιήσεις.");
                            }
                        }
                        break;
                    case "cancel":
                        System.out.print("Τύπος θέσης: ");
                        String ctype = scanner.nextLine();
                        System.out.print("Αριθμός θέσεων: ");
                        int cnum = Integer.parseInt(scanner.nextLine());
                        String cres = th.cancelSeats(ctype, cnum, name);
                        System.out.println(cres);
                        break;
                    case "guests":
                        Map<String, List<String>> bookings = th.getGuestBookings();
                        bookings.forEach((k, v) -> {
                            System.out.println("Πελάτης: " + k);
                            v.forEach(System.out::println);
                        });
                        break;
                    default:
                        System.out.println("Άγνωστη εντολή.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}