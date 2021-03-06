package org.yi.happy.archive;

import java.io.PrintStream;
import java.util.List;

import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.block.parser.GenericBlockParse;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesOutput;

/**
 * Verify that a set of blocks in files load, parse, and validate.
 */
@UsesArgs({ "file..." })
@UsesOutput("result")
public class VerifyMain implements MainCommand {

    private final FileStore files;
    private final PrintStream out;
    private final List<String> args;

    /**
     * create, injecting the dependencies.
     * 
     * @param files
     *            the file store to use.
     * @param out
     *            where to send the output.
     * @param args
     *            the non-option arguments on the command line.
     */
    public VerifyMain(FileStore files, PrintStream out, List<String> args) {
        this.files = files;
        this.out = out;
        this.args = args;
    }

    /**
     * verify that a set of blocks in files load, parse, and validate.
     * 
     * @param env
     *            the list of files to verify.
     * @throws Exception
     */
    @Override
    public void run() throws Exception {
        for (String arg : args) {
            String line;

            try {
                /*
                 * load the file
                 */
                byte[] data = files.get(arg, Blocks.MAX_SIZE);

                /*
                 * parse into a block
                 */
                Block block = GenericBlockParse.parse(data);

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
