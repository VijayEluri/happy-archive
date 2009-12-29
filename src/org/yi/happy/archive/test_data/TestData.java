package org.yi.happy.archive.test_data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;

import org.yi.happy.archive.Streams;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.KeyParse;
import org.yi.happy.archive.key.LocatorKey;

/**
 * Various test data files, these represent the existing data formats in use and
 * are used to verify encoding and decoding operations.
 */
public enum TestData {
	/**
	 * The empty file, it does not parse as a block.
	 */
	BAD_EMPTY,

	/**
	 * Some basic content, the string "hello\n" in a block.
	 */
	CLEAR_CONTENT,

	/**
	 * blob block
	 */
	@Full("blob:200cf5031a53e822c3a29726b73a401600faacf2875f420dfe34"
			+ "bf87db03e5b0:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa"
			+ "38a5673a87e820b40164e930edeac")
	@Locator("blob:200cf5031a53e822c3a29726b73a401600faacf2875f420dfe34"
			+ "bf87db03e5b0")
	@Clear(CLEAR_CONTENT)
	KEY_BLOB,

	/**
	 * A blob block encoded with different settings.
	 */
	@Full("blob:8130021dcf770532dfd0502c5c59475ea4d79e3f:bb3"
			+ "507d7611785dc392cbcab77af0c3cae14dd8b6f2f5011")
	@Locator("blob:8130021dcf770532dfd0502c5c59475ea4d79e3f")
	@Clear(CLEAR_CONTENT)
	KEY_BLOB_SHA1_AES192,

	/**
	 * a blob block encrypted using a different cipher
	 */
	@Full("blob:936401d0ffc5e8fb8bf5de8d9cfa15b1d8a4daa10136bf37290f"
			+ "b2a8311a681c:f6bd9f3b01b4ee40f60df2dc622f9d6f")
	@Locator("blob:936401d0ffc5e8fb8bf5de8d9cfa15b1d8a4daa10136bf37290f"
			+ "b2a8311a681c")
	@Clear(CLEAR_CONTENT)
	KEY_BLOB_AES128,

	/**
	 * version two content block, encoded with the sha-256 diesst and
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
	 * version two name block
	 */
	@Full("name-hash:sha-256:blah")
	@Locator("name-hash:8b7df143d91c716ecfa5fc1730022f6b421b05cedee8fd5"
			+ "2b1fc65a96030ad52")
	@Clear(CLEAR_CONTENT)
	KEY_NAME,

	/**
	 * version one content block
	 */
	@Full("content-hash:87c5f6fe4ea801c8eb227b8b218a0659c18ece76b7c2"
			+ "00c645ab4364becf68d5:f6bd9f3b01b4ee40f60df2dc622f9d6f3aa"
			+ "38a5673a87e820b40164e930edeac")
	@Locator("content-hash:87c5f6fe4ea801c8eb227b8b218a0659c18ece76b7c2"
			+ "00c645ab4364becf68d5")
	@Clear(CLEAR_CONTENT)
	KEY_OLD_CONTENT,

	/**
	 * a content block encrypted using a different cipher
	 */
	@Full("content-hash:d7859e105484ff5af15fc35365043e92531402b23168"
			+ "246b2cfca4932bf27d14:f6bd9f3b01b4ee40f60df2dc622f9d6f")
	@Locator("content-hash:d7859e105484ff5af15fc35365043e92531402b23168"
			+ "246b2cfca4932bf27d14")
	@Clear(CLEAR_CONTENT)
	KEY_CONTENT_AES128,

	/**
	 * a content block encrypted using a different cipher by another name
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
	 * small clear block with a repeated header
	 */
	OK_SMALL,

	/**
	 * a block that will not verify because of a short content section
	 */
	BAD_KEY_SHORT_NAME,

	/**
	 * short name key using md5 hashing and a longer cipher, good test for key
	 * expansion.
	 */
	@Full("name-hash:md5:blah")
	@Locator("name-hash:6f1ed002ab5595859014ebf0951522d9")
	@Clear(CLEAR_CONTENT)
	KEY_NAME_MD5_AES192;

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
			return new KeyParse().parseFullKey(raw);
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
			return new KeyParse().parseLocatorKey(raw);
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
}
