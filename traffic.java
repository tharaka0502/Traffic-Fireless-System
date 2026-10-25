import java.util.Scanner;

public class traffic {
    public static volatile boolean pedestrianWaiting = false;

    public static void main(String[] args) {
        Scanner kal = new Scanner(System.in);

        System.out.println("///Traffic Light System///");
        System.out.println("1. Manual Mode");
        System.out.println("2. Auto mode");
        System.out.println("Select mode (1-2)");
        int mode = kal.nextInt();

        if (mode == 1) {
            manualMode(kal);
        } else if (mode == 2) {
            autoMode(kal);
        } else {
            System.out.println("Invalid choice! Please enter 1 or 2");
        }
        kal.close();
    }

    // Manual mode
    public static void manualMode(Scanner bbb) {
        System.out.println("\nManual mode Activated");
        System.out.println("1. Red light");
        System.out.println("2. Yellow Light");
        System.out.println("3. Green light");
        System.out.println("Enter your choice (1-3)");
        int choice = bbb.nextInt();
        showLightMessage(choice);
    }

    // Auto mode with pedestrian crossing handled immediately
    public static void autoMode(Scanner scanner) {
        System.out.println("/// Auto mode Activated ///");

        // Thread to listen for pedestrian input
        Thread pedestrianThread = new Thread(() -> {
            while (true) {
                System.out.print("\nPress 1 to request pedestrian crossing: ");
                int input = scanner.nextInt();
                if (input == 1) {
                    pedestrianWaiting = true;
                    System.out.println("[PEDESTRIAN] Pedestrian crossing requested!");
                }
            }
        });

        pedestrianThread.setDaemon(true);
        pedestrianThread.start();

        while (true) {
            if (pedestrianWaiting) {
                handlePedestrianCrossing();
            } else {
                changeLightWithCheck("GREEN", 8);
                changeLightWithCheck("YELLOW", 4);
                changeLightWithCheck("RED", 6);
            }
        }
    }

    // Handle pedestrian crossing immediately
    public static void handlePedestrianCrossing() {
        System.out.println("\n[SYSTEM] INTERRUPTING for PEDESTRIAN...");
        changeLight("YELLOW (slowing down)", 2);
        changeLight("RED (PEDESTRIAN CROSSING)", 5);
        System.out.println("[SYSTEM] Pedestrian has crossed.");
        pedestrianWaiting = false;
    }

    // Show light message in manual mode
    public static void showLightMessage(int choice) {
        switch (choice) {
            case 1:
                System.out.println("STOP. The light is Red.");
                break;
            case 2:
                System.out.println("GET READY. The light is Yellow.");
                break;
            case 3:
                System.out.println("GO! The light is Green.");
                break;
            default:
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
        }
    }

    // Light changer that checks for pedestrian during light time
    public static void changeLightWithCheck(String light, int seconds) {
        for (int i = 0; i < seconds; i++) {
            if (pedestrianWaiting) {
                handlePedestrianCrossing();
                return;
            }
            System.out.println("[TRAFFIC] " + light + " ON... (" + (seconds - i) + "s left)");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error in sleep.");
            }
        }
    }

    public static void changeLight(String light, int seconds) {
        System.out.println("[TRAFFIC] " + light + " ON for " + seconds + " seconds.");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("Error in sleep function.");
        }
    }
}