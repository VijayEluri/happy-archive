package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.file_system.FileSystem;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * load a list of files from a volume into a store.
 */
public class VolumeGetMain {

    private final FileSystem fs;
    private final Reader in;
    private final Writer out;
    private final PrintStream err;

    /**
     * create with context.
     * 
     * @param fs
     *            the file system.
     * @param in
     *            the standard input.
     * @param out
     *            the standard output.
     * @param err
     *            the standard error.
     */
    public VolumeGetMain(FileSystem fs, Reader in, Writer out, PrintStream err) {
        this.fs = fs;
        this.in = in;
        this.out = out;
        this.err = err;
    }

    /**
     * run the body.
     * 
     * @param env
     *            the command line.
     * @throws IOException
     */
    public void run(Env env) throws IOException {
        if (env.hasNoStore() || env.hasArgumentCount() != 1) {
            out.write("use: store base < list\n");
            return;
        }

        FileBlockStore s = new FileBlockStore(fs, env.getStore());

        LineCursor in = new LineCursor(this.in);
        while (in.next()) {
            try {
                byte[] data = fs.load(fs.join(env.getArgument(0), in.get()),
                        Blocks.MAX_SIZE);
                EncodedBlock b = EncodedBlockParse.parse(data);
                s.put(b);
            } catch (Exception e) {
                e.printStackTrace(err);
            }
        }
    }

    /**
     * Invoke from the command line.
     * 
     * @param env
     *            the arguments.
     * @throws IOException
     */
    @EntryPoint
    public static void main(Env env) throws IOException {
        FileSystem fs = new RealFileSystem();
        Reader in = new InputStreamReader(System.in, "UTF-8");
        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        PrintStream err = System.err;

        new VolumeGetMain(fs, in, out, err).run(env);

        out.flush();
    }
}
