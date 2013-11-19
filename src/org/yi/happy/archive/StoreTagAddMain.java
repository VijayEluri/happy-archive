package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

import org.yi.happy.annotate.GlobalInput;
import org.yi.happy.annotate.GlobalOutput;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.commandLine.UsesBlockStore;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagBuilder;
import org.yi.happy.archive.tag.TagIterator;
import org.yi.happy.archive.tag.TagOutputStream;

/**
 * A filter that takes a tag stream, and for files where the data field is not
 * filled in, the file is stored and the field filled in.
 */
@UsesBlockStore
@UsesInput("tag-list")
@UsesOutput("tag-list")
@GlobalInput
@GlobalOutput
public class StoreTagAddMain implements MainCommand {
    private final BlockStore blocks;
    private final FileStore files;

    /**
     * @param blocks
     *            the block store to use.
     * @param files
     *            the file system to use.
     */
    public StoreTagAddMain(BlockStore blocks, FileStore files) {
        this.blocks = blocks;
        this.files = files;
    }

    @Override
    public void run() throws IOException {
        /*
         * read a stream of tags of standard input, for the file tags that lack
         * a data attribute, store the file and fill in the data and hash
         * attribute.
         */
        try {
            /*
             * do the work
             */

            ClearBlockTargetStore s = new ClearBlockTargetStore(
                    BlockEncoderFactory.getContentDefault(), blocks);

            TagOutputStream out = new TagOutputStream(System.out);

            for (Tag tag : new TagIterator(System.in)) {
                tag = process(tag, s, files);
                out.write(tag);
            }
        } finally {
            System.out.flush();
        }
    }

    private static Tag process(Tag tag, ClearBlockTargetStore s, FileStore fs)
            throws IOException {
        if (tag.get("data") != null) {
            return tag;
        }

        String name = tag.get("name");
        if (name == null) {
            return tag;
        }

        String type = tag.get("type");
        if (type != null && !type.equals("file")) {
            return tag;
        }

        if (!fs.isFile(name)) {
            return tag;
        }

        KeyOutputStream o1 = new KeyOutputStream(s);
        DigestOutputStream o2 = new DigestOutputStream(DigestFactory
                .getProvider("sha-256").get());
        TeeOutputStream o = new TeeOutputStream(o1, o2);
        InputStream in = fs.getStream(name);
        try {
            Streams.copy(in, o);
        } finally {
            in.close();
        }
        o.close();

        return new TagBuilder(tag).put("type", "file")
                .put("size", Long.toString(o2.getSize()))
                .put("data", o1.getFullKey().toString())
                .put("hash", "sha-256:" + o2.getHash()).create();
    }
}
