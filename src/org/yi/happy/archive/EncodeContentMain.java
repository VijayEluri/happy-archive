package org.yi.happy.archive;

import java.io.IOException;
import java.io.PrintStream;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.encoder.BlockEncoder;
import org.yi.happy.archive.block.encoder.BlockEncoderFactory;
import org.yi.happy.archive.block.encoder.BlockEncoderResult;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * command line tool to encode a content block. The clear data is read from the
 * file named in the first argument, the encoded block is written to the file
 * named in the second argument, the full key for decoding is printed on
 * standard output.
 */
public class EncodeContentMain implements MainCommand {
    private final FileSystem fs;
    private final PrintStream out;

    /**
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            where to send output.
     */
    public EncodeContentMain(FileSystem fs, PrintStream out) {
        this.fs = fs;
        this.out = out;
    }

    /**
     * encode a block.
     * 
     * @param env
     *            the file name of the block.
     * @throws IOException
     */
    @EntryPoint
    public void run(Env env) throws IOException {
        if (env.hasArgumentCount() != 2) {
            out.println("use: input output");
            return;
        }

        BlockEncoder encoder = BlockEncoderFactory.getContentDefault();

        Block block = BlockParse.parse(fs.load(env.getArgument(0),
                Blocks.MAX_SIZE));
        BlockEncoderResult e = encoder.encode(block);
        fs.save(env.getArgument(1), e.getBlock().asBytes());

        out.println(e.getKey());
    }
}
