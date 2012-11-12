package org.yi.happy.archive;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.yi.happy.archive.block.EncodedBlock;
import org.yi.happy.archive.block.parser.EncodedBlockParse;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.crypto.DigestFactory;
import org.yi.happy.archive.crypto.DigestProvider;
import org.yi.happy.archive.crypto.Digests;
import org.yi.happy.archive.file_system.FileSystem;

/**
 * Index a volume that has been burned.
 */
@UsesArgs({ "image-path" })
public class IndexVolumeMain implements MainCommand {

    private final FileSystem fs;
    private final PrintStream out;
    private final DigestProvider digest;
    private final PrintStream err;

    /**
     * create with a context.
     * 
     * @param fs
     *            the file system to use.
     * @param out
     *            the output stream.
     * @param err
     *            the error stream.
     */
    public IndexVolumeMain(FileSystem fs, PrintStream out, PrintStream err) {
        this.fs = fs;
        this.out = out;
        this.err = err;

        digest = DigestFactory.getProvider("sha-256");
    }

    /**
     * do the index.
     * 
     * @param args
     *            the command line ( image-path )
     * @throws IOException
     */
    @Override
    public void run(Env env) throws IOException {
        if (env.hasArgumentCount() != 1) {
            out.println("use: image");
            return;
        }

        try {
            List<String> names = fs.list(env.getArgument(0));
            Collections.sort(names);
            for (String name : names) {
                process(fs.join(env.getArgument(0), name), name);
            }
        } finally {
            out.flush();
        }
    }

    private void process(String path, String name) throws IOException {
        if (fs.isDir(path)) {
            processDir(path, name);
            return;
        }

        try {
            byte[] data = fs.load(path, Blocks.MAX_SIZE);
            EncodedBlock block = EncodedBlockParse.parse(data);

            String key = block.getKey().toString();

            data = block.asBytes();

            String hash = Base16.encode(Digests.digestData(digest, data));
            String size = Integer.toString(data.length);

            out.println(name + "\t" + "plain" + "\t" + key + "\t" + hash + "\t"
                    + size);
        } catch (Exception e) {
            e.printStackTrace(err);
        }
    }

    private void processDir(String path, String base) throws IOException {
        List<String> names = fs.list(path);
        Collections.sort(names);
        for (String name : names) {
            process(fs.join(path, name), base + "/" + name);
        }
    }
}
