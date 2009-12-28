package org.yi.happy.archive.key;

public class BlobFullKey extends AbstractContentFullKey implements FullKey {
    public BlobFullKey(byte[] hash, byte[] pass) {
        super(hash, pass);
    }

    @Override
    public String getType() {
        return KeyType.BLOB;
    }

}
