package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * A command line tool to decode a single block. The block is read from the
 * named file and the given full key is used to decode it. The decoded block is
 * sent to standard output.
 */
public class DecodeBlockMain implements MainCommand {
    private final FileSystem fs;
    private final OutputStream out;

    /**
     * create the procedure.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            the output stream to use.
     */
    public DecodeBlockMain(FileSystem fs, OutputStream out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * decode a single block.
     * 
     * @param env
     *            file name of the block to decode, full key of the block.
     * @throws IOException
     */
    @Override
    @SmellsMessy
    public void run(Env env) throws IOException {
        if (env.hasArgumentCount() != 2) {
            System.err.println("use: input key");
            return;
        }

        EncodedBlock b = EncodedBlockParse.parse(fs.load(env.getArgument(0),
                Blocks.MAX_SIZE));
        FullKey k = FullKeyParse.parseFullKey(env.getArgument(1));
        Block d = b.decode(k);
        out.write(d.asBytes());
        out.flush();
    }
}
