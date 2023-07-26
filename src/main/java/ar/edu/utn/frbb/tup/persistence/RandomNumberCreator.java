package ar.edu.utn.frbb.tup.persistence;

import java.util.Random;

public class RandomNumberCreator {
    private static RandomNumberCreator instance;
    private Random random;

    private RandomNumberCreator() {
        random = new Random();
    }

    public static synchronized RandomNumberCreator getInstance() {
        if (instance == null) {
            instance = new RandomNumberCreator();
        }
        return instance;
    }

    public int generateRandomNumber (int numMaximo) {
        return random.nextInt(numMaximo);
    }
}
