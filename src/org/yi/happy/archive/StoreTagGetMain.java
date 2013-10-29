package org.yi.happy.archive;

import java.io.IOException;
import java.io.InputStream;

import org.yi.happy.annotate.RestoreLoop;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.commandLine.UsesInput;
import org.yi.happy.archive.commandLine.UsesNeed;
import org.yi.happy.archive.commandLine.UsesStore;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.restore.RestoreEngine;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagStreamIterator;

/**
 * A program to restore tags.
 */
@UsesStore
@UsesNeed
@UsesInput("tag-list")
public class StoreTagGetMain implements MainCommand {
    private final InputStream in;
    private final ClearBlockSource source;
    private final FragmentSave target;
    private final NotReadyHandler notReady;

    /**
     * set up.
     * 
     * @param source
     *            the block source to use.
     * @param target
     *            where to save the fragments.
     * @param notReady
     *            what to do when no needed blocks are ready.
     * @param in
     *            what to use for standard input.
     * @param env
     *            the invocation environment.
     */
    public StoreTagGetMain(ClearBlockSource source, FragmentSave target,
            NotReadyHandler notReady, InputStream in) {
        this.source = source;
        this.target = target;
        this.notReady = notReady;
        this.in = in;
    }

    /**
     * run the program.
     * 
     * @param args
     *            store base path; request list.
     * @throws IOException
     */
    @Override
    @RestoreLoop
    public void run() throws IOException {
        RestoreEngine engine = new RestoreEngine();

        for (Tag i : new TagStreamIterator(in)) {
            String name = i.get("name");
            if (name == null) {
                continue;
            }

            String type = i.get("type");
            if (type == null) {
                continue;
            }

            String data = i.get("data");
            if (data == null) {
                continue;
            }

            FullKey key;
            try {
                key = FullKeyParse.parseFullKey(data);
            } catch (IllegalArgumentException e) {
                continue;
            }

            if (type.equals("file")) {
                engine.add(name, key);
                continue;
            }

            /*
             * TODO there is also a type=dir where the content is a tag list.
             */
        }

        try {
            /*
             * do the work
             */
            while (true) {
                boolean progress = false;

                engine.start();
                while (engine.findReady()) {
                    Block block = source.get(engine.getKey());
                    if (block == null) {
                        engine.skip();
                        continue;
                    }

                    Fragment part = engine.step(block);
                    progress = true;

                    if (part != null) {
                        target.save(engine.getJobName(), part);
                    }
                }

                if (engine.isDone()) {
                    break;
                }

                target.close();
                notReady.onNotReady(engine, progress);
            }
        } finally {
            target.close();
        }
    }
}
