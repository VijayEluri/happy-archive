package org.yi.happy.crypt;

import java.util.Arrays;

import org.yi.happy.metric.SimpleTimer;
import org.yi.happy.metric.SimpleTimerSummary;

/**
 * timing of the rijndael cypher
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
 */
public class RijndaelTime {
    private static final int runLength = 1024 * 1024;

    private static final int BS = 16;

    private static final int KS = 16;

    /**
     * time encryption and decryption
     * 
     * @param args
     */
    public static void main(String[] args) {
        SimpleTimerSummary se = new SimpleTimerSummary();
        SimpleTimerSummary sd = new SimpleTimerSummary();
        for (int run = 0; run < 50; run++) {
            Rijndael r = new Rijndael(BS, KS);
            byte[] key = new byte[KS];
            r.setKey(key);

            byte[] state = new byte[runLength];
            byte[] iv = new byte[BS];

            SimpleTimer t = se.getTimer();
            r.encryptCbc(state, iv);
            t.stop();

            Arrays.fill(iv, (byte) 0);
            t = sd.getTimer();
            r.decryptCbc(state, iv);
            t.stop();

            for (int i = 0; i < state.length; i++) {
                if (state[i] != 0) {
                    throw new IllegalStateException("didn't get back to base");
                }
            }

            System.out.println(

            "encrypt: " + se + "\n" +

            "decrypt: " + sd + "\n");
        }
    }
}
