package org.yi.happy.archive.test_data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;

import org.yi.happy.archive.Streams;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.LocatorKey;

/**
 * Various test data files, these represent the existing data formats in use and
 * are used to verify encoding and decoding operations.
 */
public enum TestData {
    /**
     * The empty file.
     */
    FILE_EMPTY,

    /**
     * Some basic content, the string "hello\n" in a plain data block.
     */
    CLEAR_CONTENT,

    /**
     * a blob block encoded with sha-256 and rijndael256-256-cbc.
     */
    @Full("blob:200cf5031a53e822c3a29726b73a401600faacf2875f420dfe34"
            + "bf87db03e5b0:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa"
            + "38a5673a87e820b40164e930edeac")
    @Locator("blob:200cf5031a53e822c3a29726b73a401600faacf2875f420dfe34"
            + "bf87db03e5b0")
    @Clear(CLEAR_CONTENT)
    KEY_BLOB,

    /**
     * A blob block encoded with sha-1 and aes-192-cbc.
     */
    @Full("blob:8130021dcf770532dfd0502c5c59475ea4d79e3f:bb3"
            + "507d7611785dc392cbcab77af0c3cae14dd8b6f2f5011")
    @Locator("blob:8130021dcf770532dfd0502c5c59475ea4d79e3f")
    @Clear(CLEAR_CONTENT)
    KEY_BLOB_SHA1_AES192,

    /**
     * a blob block encoded with sha-256 and aes-128-cbc.
     */
    @Full("blob:936401d0ffc5e8fb8bf5de8d9cfa15b1d8a4daa10136bf37290f"
            + "b2a8311a681c:f6bd9f3b01b4ee40f60df2dc622f9d6f")
    @Locator("blob:936401d0ffc5e8fb8bf5de8d9cfa15b1d8a4daa10136bf37290f"
            + "b2a8311a681c")
    @Clear(CLEAR_CONTENT)
    KEY_BLOB_AES128,

    /**
     * version two content block, encoded with the sha-256 digest and
     * rijndael256-256-cbc cipher.
     */
    @Full("content-hash:87c5f6fe4ea801c8eb227b8b218a0659c18ece76b7c2"
            + "00c645ab4364becf68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa"
            + "38a5673a87e820b40164e930edeac")
    @Locator("content-hash:87c5f6fe4ea801c8eb227b8b218a0659c18ece76b7c2"
            + "00c645ab4364becf68d5")
    @Clear(CLEAR_CONTENT)
    KEY_CONTENT,

    /**
     * version two name block, encoded with sha-256 and rijndael256-256-cbc.
     */
    @Full("name-hash:sha-256:blah")
    @Locator("name-hash:8b7df143d91c716ecfa5fc1730022f6b421b05cedee8fd5"
            + "2b1fc65a96030ad52")
    @Clear(CLEAR_CONTENT)
    KEY_NAME,

    /**
     * version one content block, encoded with sha-256 and rijndael256-256-cbc.
     */
    @Full("content-hash:87c5f6fe4ea801c8eb227b8b218a0659c18ece76b7c2"
            + "00c645ab4364becf68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa"
            + "38a5673a87e820b40164e930edeac")
    @Locator("content-hash:87c5f6fe4ea801c8eb227b8b218a0659c18ece76b7c2"
            + "00c645ab4364becf68d5")
    @Clear(CLEAR_CONTENT)
    KEY_OLD_CONTENT,

    /**
     * a content block, encoded with sha-256 and aes-128-cbc.
     */
    @Full("content-hash:d7859e105484ff5af15fc35365043e92531402b23168"
            + "246b2cfca4932bf27d14:f6bd9f3b01b4ee40f60df2dc622f9d6f")
    @Locator("content-hash:d7859e105484ff5af15fc35365043e92531402b23168"
            + "246b2cfca4932bf27d14")
    @Clear(CLEAR_CONTENT)
    KEY_CONTENT_AES128,

    /**
     * a content block, encoded with sha-256 and rijndael-128-cbc.
     */
    @Full("content-hash:d7859e105484ff5af15fc35365043e92531402b23168"
            + "246b2cfca4932bf27d14:f6bd9f3b01b4ee40f60df2dc622f9d6f")
    @Locator("content-hash:d7859e105484ff5af15fc35365043e92531402b23168"
            + "246b2cfca4932bf27d14")
    @Clear(CLEAR_CONTENT)
    KEY_CONTENT_RIJNDAEL,

    /**
     * a block that will not verify because of a short content section
     */
    BAD_KEY_SHORT_CONTENT,

    /**
     * small clear block. a -> c, b -> d, c -> e, "body\ndata\n".
     */
    OK_SMALL,

    /**
     * a block that will not verify because of a short content section.
     */
    BAD_KEY_SHORT_NAME,

    /**
     * short name key using md5 hashing and a longer cipher, good test for key
     * expansion.
     */
    @Full("name-hash:md5:blah")
    @Locator("name-hash:6f1ed002ab5595859014ebf0951522d9")
    @Clear(CLEAR_CONTENT)
    KEY_NAME_MD5_AES192,

    /**
     * the clear version of a map block for 2048 'a's.
     */
    CLEAR_CONTENT_MAP_2048A,

    /**
     * the base key for a map of "a" x 2048 in 512 byte chunks
     */
    @Full("content-hash:9d059a2c0445ced47d5e8766e613c2156e4ed683b861"
            + "6a3459bc54d2d0c9fbfd:c09c1708ab42a4550674610870c44d121c7"
            + "0a823172ad3c47221ab71dd597df9")
    @Locator("content-hash:9d059a2c0445ced47d5e8766e613c2156e4ed683b861"
            + "6a3459bc54d2d0c9fbfd")
    @Clear(CLEAR_CONTENT_MAP_2048A)
    KEY_CONTENT_MAP_2048A,

    /**
     * a clear block with 512 'a's in it.
     */
    CLEAR_CONTENT_512A,

    /**
     * A content encoded block with 512 'a's in it.
     */
    @Full("content-hash:61a0ef49ca8d2e54454bfb1a55d34570666ef71820fa1a33ba19ce"
            + "d94fdea2fb:d7004f5a0efeab8bafc9df8a4dbf1573bb07d846d8fa788e51ce"
            + "86ee44750f58")
    @Locator("content-hash:61a0ef49ca8d2e54454bfb1a55d34570666ef71820fa1a33ba1"
            + "9ced94fdea2fb")
    @Clear(CLEAR_CONTENT_512A)
    KEY_CONTENT_512A,

    /**
     * the block that is created by writing "01234" x 8
     */
    CLEAR_CONTENT_40,

    /**
     * the block that is created by writing "01234" x 8
     */
    @Full("content-hash:7bd236857bc1af3cc3c65e1d0695175ceee65238b250"
            + "0c0d20237d3b7312afbf:c4e8a7d6e546c0e57489732a9c0c4e1743d"
            + "ac7dc0799bdcd1cda968e0f716b2b")
    @Locator("content-hash:7bd236857bc1af3cc3c65e1d0695175ceee65238b250"
            + "0c0d20237d3b7312afbf")
    @Clear(CLEAR_CONTENT_40)
    KEY_CONTENT_40,

    /**
     * a data block containing "01234"
     */
    CLEAR_CONTENT_1,

    /**
     * a data block containing "01234"
     */
    @Full("content-hash:cf5d4bb60ae7992a5569ce577f40fa66e8909ead5ba1"
            + "70ff1b9d136ca69bc8f1:a8e78f3de5a65a673628b01ea18bf80c91d"
            + "3e160e8c47274224c71552534bc63")
    @Locator("content-hash:cf5d4bb60ae7992a5569ce577f40fa66e8909ead5ba1"
            + "70ff1b9d136ca69bc8f1")
    @Clear(CLEAR_CONTENT_1)
    KEY_CONTENT_1,

    /**
     * a data block containing "56789"
     */
    CLEAR_CONTENT_2,

    /**
     * a data block containing "56789"
     */
    @Full("content-hash:b4f02ef9f3c7dec4cd7604627ffc901fd2030c869779"
            + "301a3e8b39af7e6151cb:66674c78cfdeff1e86d1b72010b616b2015"
            + "5b01586027de6a0d19bf292af5615")
    @Locator("content-hash:b4f02ef9f3c7dec4cd7604627ffc901fd2030c869779"
            + "301a3e8b39af7e6151cb")
    @Clear(CLEAR_CONTENT_2)
    KEY_CONTENT_2,

    /**
     * a two part map for "01234" + "56789".
     */
    CLEAR_CONTENT_MAP,

    /**
     * a two part map for "01234" + "56789". {@link #KEY_CONTENT_1},
     * {@link #KEY_CONTENT_2}.
     */
    @Full("content-hash:87bed19266dc934236c132b9538cd8ec833aa85856b04be7b90f52"
            + "03f51aebf2:e43a2194e64835a3a90562c7497d1f70390"
            + "d406322c5d3c773e2d2f6c883d26d")
    @Locator("content-hash:87bed19266dc934236c132b9538cd8ec833aa85856b0"
            + "4be7b90f5203f51aebf2")
    @Clear(CLEAR_CONTENT_MAP)
    KEY_CONTENT_MAP,

    /**
     * clear text version of KEY_CONTENT_MAP_OVERLAP
     */
    CLEAR_CONTENT_MAP_OVERLAP,

    /**
     * a map with an overlap
     */
    @Full("content-hash:f6319daaac6b24331301ab8315e946395d250fabe39c"
            + "b1ef7577c42df284ab3b:fcc7229ac6b6913b31179efa2e4612c61f7"
            + "94c0cc8189edf1662df1d51f1b498")
    @Locator("content-hash:f6319daaac6b24331301ab8315e946395d250fabe39c"
            + "b1ef7577c42df284ab3b")
    @Clear(CLEAR_CONTENT_MAP_OVERLAP)
    KEY_CONTENT_MAP_OVERLAP,

    /**
     * clear text version of KEY_CONTENT_MAP_PAD
     */
    CLEAR_CONTENT_MAP_PAD,

    /**
     * a map with a gap
     */
    @Full("content-hash:467955babd84179b7ca3b9ac0462eb68592251677cc1"
            + "846a60fb579219e074e8:0491cc799e2b3dbae6c7475c4dab3cc3e49"
            + "96045374a1b66ccb36d956fab872e")
    @Locator("content-hash:467955babd84179b7ca3b9ac0462eb68592251677cc1"
            + "846a60fb579219e074e8")
    @Clear(CLEAR_CONTENT_MAP_PAD)
    KEY_CONTENT_MAP_PAD,

    /**
     * clear version of key-name-split
     */
    CLEAR_NAME_SPLIT,

    /**
     * a classic split block
     */
    @Full("name-hash:sha-256:split")
    @Locator("name-hash:ad1a64057f9ab34fecfe3f4ee78660bb0316dbda9370581ffbeb1e"
            + "8bddf3d598")
    @Clear(CLEAR_NAME_SPLIT)
    KEY_NAME_SPLIT,

    /**
     * an indirect block in the split for part 1
     */
    CLEAR_NAME_SPLIT_1,

    /**
     * an indirect block in the split for part 1
     */
    @Full("name-hash:sha-256:split/0")
    @Locator("name-hash:83013ce3b08bc6ef779a2dfae872c0171bcf463b646c1f6ff72062"
            + "ff93a28a32")
    @Clear(CLEAR_NAME_SPLIT_1)
    KEY_NAME_SPLIT_1,

    /**
     * an indirect block in the split for part 1
     */
    CLEAR_NAME_SPLIT_2,

    /**
     * an indirect block in the split for part 2
     */
    @Full("name-hash:sha-256:split/1")
    @Locator("name-hash:ba69e8638346563154fc571245edb331fbb71f420e97bce423c8ee"
            + "616eba279d")
    @Clear(CLEAR_NAME_SPLIT_2)
    KEY_NAME_SPLIT_2,

    /**
     * a block list, precursor to map
     */
    CLEAR_CONTENT_LIST,

    /**
     * a block list, precursor to map
     */
    @Full("content-hash:430a7bc21e3f95c443ec2151fc9e727119b21cc5878a"
            + "af1361971445bf8b1d0c:a879203e66ad42df67797f6639908d8d9fa"
            + "85ed8af32d6d304fd61a50b12c5fd")
    @Locator("content-hash:430a7bc21e3f95c443ec2151fc9e727119b21cc5878a"
            + "af1361971445bf8b1d0c")
    @Clear(CLEAR_CONTENT_LIST)
    KEY_CONTENT_LIST,

    /**
     * A tag list with just a single file in it. The entry named "test.txt" is
     * {@link #FILE_CONTENT}.
     */
    TAG_FILE,

    /**
     * A tag list with two files in it. The entry named "test.txt" is
     * {@link #FILE_CONTENT}, the entry named "test.dat" is
     * {@link #FILE_CONTENT_40}.
     */
    TAG_FILES,

    /**
     * The body of {@link #CLEAR_CONTENT}.
     */
    FILE_CONTENT,

    /**
     * The body of {@link #CLEAR_CONTENT_40}.
     */
    FILE_CONTENT_40,

    /**
     * A stored version of {@link #TAG_FILES}.
     */
    @Full("content-hash:36248dd4bf041f6ee767724e95de768cb3c82fdb25e41349f61e57"
            + "b7c4ad7c25:12497e09c4d7d5a8fd8890838d38f68c034c9eaeb38c41ef8899"
            + "4ad59283fcba")
    @Locator("content-hash:36248dd4bf041f6ee767724e95de768cb3c82fdb25e41349f61"
            + "e57b7c4ad7c25")
    KEY_TAG_FILES,

    /**
     * A tag list with files and a directory.
     */
    TAG_MIXED,

    /**
     * An index for a volume that has the three blocks of
     * {@link #KEY_CONTENT_MAP}. '00.dat' => {@link #KEY_CONTENT_MAP}, '01.dat'
     * => {@link #KEY_CONTENT_1}, '02.dat' => {@link #KEY_CONTENT_2}.
     */
    INDEX_MAP;

    /**
     * get the file name for this enumeration constant, relative to the
     * declaring class.
     * 
     * @return the file name of the data for this item.
     */
    public String getFileName() {
        return name().toLowerCase().replace('_', '-') + ".bin";
    }

    /**
     * get the address for this enumeration constant.
     * 
     * @return the address for this enumeration constant.
     */
    public URL getUrl() {
        return TestData.class.getResource(getFileName());
    }

    /**
     * get the bytes for this enumeration constant.
     * 
     * @return the bytes for this enumeration constant.
     * @throws IOException
     *             on error.
     */
    public byte[] getBytes() throws IOException {
        InputStream in = getUrl().openStream();
        try {
            return Streams.load(in);
        } finally {
            in.close();
        }
    }

    /**
     * get the full key for a block as provided by an annotation
     * 
     * @return the full key annotation value
     * @throws UnsupportedOperationException
     *             if the annotation is not present or not available
     */
    public FullKey getFullKey() throws UnsupportedOperationException {
        try {
            String raw = getAnnotation(Full.class).value();
            return KeyParse.parseFullKey(raw);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * get the locator key for a block as provided by an annotation.
     * 
     * @return the locator key annotation value
     * @throws UnsupportedOperationException
     *             if the annotation is not present or not available
     */
    public LocatorKey getLocatorKey() throws UnsupportedOperationException {
        try {
            String raw = getAnnotation(Locator.class).value();
            return KeyParse.parseLocatorKey(raw);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * get an annotation on the declaration of this enumeration constant.
     * 
     * @param <T>
     *            the type of the annotation
     * @param annotation
     *            the annotation class to read
     * @return the found annotation
     * @throws UnsupportedOperationException
     *             if the annotation can not be found or access controls do not
     *             allow it to be looked up.
     */
    private <T extends Annotation> T getAnnotation(Class<T> annotation)
            throws UnsupportedOperationException {
        try {
            Field field = getDeclaringClass().getField(name());
            T found = field.getAnnotation(annotation);
            if (found != null) {
                return found;
            }
        } catch (SecurityException e) {
            throw new UnsupportedOperationException(e);
        } catch (NoSuchFieldException e) {
            throw new UnsupportedOperationException(e);
        }
        throw new UnsupportedOperationException("not found");
    }

    /**
     * fetch the test data and parse it as an encoded block.
     * 
     * @return the test data as an encoded block.
     * @throws IOException
     *             on error.
     */
    public EncodedBlock getEncodedBlock() throws IOException {
        return EncodedBlockParse.load(getUrl());
    }

    /**
     * fetch the test data and parse it as a block.
     * 
     * @return the test data as a block.
     * @throws IOException
     *             on error.
     */
    public Block getBlock() throws IOException {
        return BlockParse.load(getUrl());
    }
}
