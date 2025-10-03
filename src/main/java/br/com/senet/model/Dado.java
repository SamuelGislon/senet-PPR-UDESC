package br.com.senet.model;

import java.util.Random;

public class Dado {
    private final Random rnd = new Random();

    public int rolarD5() {
        return 1 + rnd.nextInt(5);
    }
}
