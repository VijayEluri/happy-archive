package org.yi.happy.archive.crypto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yi.happy.annotate.MagicLiteral;


/**
 * Factory for creating Cypher instances.
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
    @MagicLiteral
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

	// TODO specialize this exception.
	throw new UnknownAlgorithmException(name);
    }

    /**
     * create a cipher provider.
     * 
     * @param algorithm
     *            the algorithm the provider will provide.
     * @return the provider.
     */
    public static CipherProvider getProvider(String algorithm) {
	return new CipherProvider(algorithm) {
	    @Override
	    public Cipher get() {
		return create(algorithm);
	    }
	};
    }
}
