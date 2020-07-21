package org.example.smartpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

/**
 * Class with main method to demonstration work of Object Pool.
 *
 * @author Ivan Batura
 */
public class Application {
    //    private final static Set<String> command = new HashSet<>();
    private static String HELP = "Please use one of the next commands to manipulate with POOL(size 10)\n"
            + "list - to view all LOCK elements\n" + "obtain - to obtain element from POOL\n"
            + "release - to release element to POOL\n" + "ReleaseAll - to release element all to POOL\n"
            + "release_all - to release element all to POOL\n"
            + "release_all_now - to release element all now to POOL\n" + "quit - to quit";

    /**
     * Max number of objects in Pool.
     */
    private static int MAX = 10;
    /**
     * {@link ObjectPool}.
     */
    private static final ObjectPool POOL = new ObjectPool(MAX);
    /**
     * Random for random elements in Pool.
     */
    private static final Random RANDOM = new Random();
    /**
     * {@link List} with obtains {@link PriceQuotation} elements.
     */
    private static List<PriceQuotation> LOCKS = new ArrayList<>();

    public static void main(String[] args) {
        try {

            for (int i = 0; i < MAX; i++) {
                try {
                    POOL.push(new PriceQuotation(RANDOM.nextDouble(), RANDOM.nextDouble(), System.currentTimeMillis(),
                            UUID.randomUUID().toString()));
                } catch (final ObjectPoolException e) {
                    System.out.println(String.format("Failed to add element %d to pool with exception message %s", i,
                            e.getMessage()));
                }
            }
            System.out.println(HELP);
            final Scanner scanner = new Scanner(System.in);
            String command = "";
            while (!command.equals("quit")) {
                System.out.println("...");
                command = scanner.next();
                switch (command) {
                    case "list":
                        if (LOCKS.isEmpty()) {
                            System.out.println("Empty. Please use obtain before, to list locks elements");
                            break;
                        }
                        for (int i = 0; i < LOCKS.size(); i++)
                            System.out.println(String.format("%d - %s", i, LOCKS.get(i)));
                        break;
                    case "obtain":
                        try {
                            final PriceQuotation priceQuotation = POOL.obtain();
                            LOCKS.add(priceQuotation);
                            System.out.println(String.format("Obtained element from POOL %s", priceQuotation));
                        } catch (final ObjectPoolException e) {
                            System.out.println(String.format("Failed to obtain from pool with exception message %s",
                                    e.getMessage()));
                        }
                        break;
                    case "release":
                        if (LOCKS.isEmpty()) {
                            System.out.println("Empty. Please use obtain before, to list locks elements");
                            break;
                        }
                        for (int i = 0; i < LOCKS.size(); i++)
                            System.out.println(String.format("%d - %s", i, LOCKS.get(i)));
                        System.out.println("Enter number element to release (from list command)");
                        int number = scanner.nextInt();
                        try {
                            POOL.release(LOCKS.get(number));
                            LOCKS.remove(number);
                            System.out.println(String.format("Element %d released.", number));
                        } catch (ObjectPoolException e) {
                            System.out.println(String.format("Failed to release from pool with exception message %s",
                                    e.getMessage()));
                        }
                        break;
                    case "release_all":
                        System.out.println("Enter timeout in milles");
                        long timeout = scanner.nextLong();
                        LOCKS = new ArrayList<>();
                        POOL.releaseAll(timeout);
                        System.out.println("ReleaseAll process started. And maybe done already, who knows.");
                        break;
                    case "release_all_now":
                        try {
                            LOCKS = new ArrayList<>();
                            POOL.releaseAllNow();
                            System.out.println("ReleaseAlNow done");
                        } catch (ObjectPoolException e) {
                            System.out.println(
                                    String.format("Failed to releaseAllNow in pool with exception message %s",
                                            e.getMessage()));
                        }
                        break;
                    case "quit":
                        break;
                    default:
                        System.out.println(HELP);
                }

            }
        } finally {
            POOL.tierDown();
        }
    }
}
