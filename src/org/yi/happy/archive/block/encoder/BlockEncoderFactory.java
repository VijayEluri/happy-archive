package org.yi.happy.archive.block.encoder;

import org.yi.happy.archive.CipherFactory;
import org.yi.happy.archive.DigestFactory;

/**
 * A factory for common configurations of the block encoder.
 */
public class BlockEncoderFactory {
    /**
     * Create a default content encoder. This default is historical.
     * 
     * @return create a content block encoder configured with sha-256 and
     *         rijndael256-256-cbc.
     */
    public static BlockEncoder getContentDefault() {
	return new BlockEncoderContent(DigestFactory.create("sha-256"),
		CipherFactory.createNamed("rijndael256-256-cbc"));
    }
}
