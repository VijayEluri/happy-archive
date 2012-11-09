package org.yi.happy.archive;

import java.io.PrintStream;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.BlockParse;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * Verify that a set of blocks in files load, parse, and validate.
 */
public class VerifyMain implements MainCommand {

    private final FileSystem fileSystem;
    private final PrintStream out;

    /**
     * create, injecting the dependencies.
     * 
     * @param fileSystem
     *            the file system to use.
     * @param out
     *            where to send the output.
     */
    public VerifyMain(FileSystem fileSystem, PrintStream out) {
        this.fileSystem = fileSystem;
        this.out = out;
    }

    /**
     * verify that a set of blocks in files load, parse, and validate.
     * 
     * @param env
     *            the list of files to verify.
     * @throws Exception
     */
    public void run(Env env) throws Exception {
        for (String arg : env.getArguments()) {
            String line;

            try {
                /*
                 * load the file
                 */
                byte[] data = fileSystem.load(arg, Blocks.MAX_SIZE);

                /*
                 * parse into a block
                 */
                Block block = BlockParse.parse(data);

                /*
                 * try to parse into an encoded block
                 */
                EncodedBlock b = EncodedBlockParse.parse(block);

                /*
                 * on success print ok key arg
                 */
                line = "ok " + b.getKey() + " " + arg + "\n";
            } catch (Exception e) {
                /*
                 * on failure print fail arg
                 */
                line = "fail " + arg + "\n";
            }

            out.print(line);
        }

        out.flush();
    }
}
