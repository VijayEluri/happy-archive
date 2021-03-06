package org.yi.happy.archive;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.yi.happy.annotate.SmellsMessy;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesOutput;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;

/**
 * A command line tool to decode a single block. The block is read from the
 * named file and the given full key is used to decode it. The decoded block is
 * sent to standard output.
 */
@UsesArgs({ "input", "key" })
@UsesOutput("output")
public class DecodeBlockMain implements MainCommand {
    private final FileStore files;
    private final OutputStream out;
    private final List<String> args;

    /**
     * create the procedure.
     * 
     * @param files
     *            the file system to use.
     * @param out
     *            the output stream to use.
     * @param args
     *            the non-option arguments.
     */
    public DecodeBlockMain(FileStore files, OutputStream out, List<String> args) {
        this.files = files;
        this.out = out;
        this.args = args;
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
    public void run() throws IOException {
        EncodedBlock b = EncodedBlockParse.parse(files.get(args.get(0),
                Blocks.MAX_SIZE));
        FullKey k = FullKeyParse.parseFullKey(args.get(1));
        Block d = b.decode(k);
        out.write(d.asBytes());
        out.flush();
    }
}
