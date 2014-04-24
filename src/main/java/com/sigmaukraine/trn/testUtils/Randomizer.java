package com.sigmaukraine.trn.testUtils;

import java.util.Random;

public class Randomizer {
    private static Random generator = new Random(System.currentTimeMillis());
    
    public static String generateId(int length) {
        if (length>19)
            throw new IllegalArgumentException("Randomizer.generateId(): length="+length);
        
        long positiveLong = ((long)(generator.nextInt(Integer.MAX_VALUE)) << 32) + generator.nextInt();
        return String.format("%019d", positiveLong).substring(19-length);
    }
}
