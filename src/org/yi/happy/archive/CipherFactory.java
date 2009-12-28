package org.yi.happy.archive;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yi.happy.archive.key.UnknownAlgorithmException;

/**
 * Factory for creating Cypher instances.
 * 
 * @author sarah dot a dot happy at gmail dot com
 * 
 */
public class CipherFactory {
    /**
     * create an instance.
     * 
     * @param name
     *            the name of the cipher in the form "algo-keySize-mode", for
     *            example "aes-128-cbc".
     * @return an object implementing the Cipher interface.
     * @throws UnknownAlgorithmException
     *             if the name is not known
     */
    public static Cipher create(String name) {
        Matcher m = Pattern.compile("(aes|rijndael)-(128|192|256)-cbc")
                .matcher(name);
        if (m.matches()) {
            int ks = Integer.parseInt(m.group(2)) / 8;
            return new CipherRijndael(16, ks);
        }

        m = Pattern.compile("rijndael(192|256)-(128|192|256)-cbc")
                .matcher(name);
        if (m.matches()) {
            int bs = Integer.parseInt(m.group(1)) / 8;
            int ks = Integer.parseInt(m.group(2)) / 8;
            return new CipherRijndael(bs, ks);
        }

        throw new UnknownAlgorithmException(name);
    }

    /**
     * create an instance, only give back a named cipher.
     * 
     * @param algorithm
     *            the name of the cipher in the form "algo-keySize-mode", for
     *            example "aes-128-cbc".
     * @return a NamedCipher wrapping an object implementing the Cipher
     *         interface.
     * @throws UnknownAlgorithmException
     *             if the name is not known
     */
    public static NamedCipher createNamed(String algorithm) {
        return new NamedCipher(algorithm, create(algorithm));
    }
}
