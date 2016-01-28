package org.yi.happy.archive;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yi.happy.archive.block.Block;
import org.yi.happy.archive.commandLine.UsesArgs;
import org.yi.happy.archive.commandLine.UsesBlockStore;
import org.yi.happy.archive.key.FullKey;
import org.yi.happy.archive.key.FullKeyParse;
import org.yi.happy.archive.restore.DuplicateJobName;
import org.yi.happy.archive.restore.RestoreEngine;
import org.yi.happy.archive.restore.RestoreState;
import org.yi.happy.archive.tag.Tag;
import org.yi.happy.archive.tag.TagIterator;

@UsesBlockStore
@UsesArgs({ "state", "tag-list..." })
public class StoreTagGetStepMain implements MainCommand {
    private ClearBlockSource source;
    private List<String> args;
    private FragmentSave target;
    private String stateDir;

    /**
     * set up
     * 
     * @param source
     *            the block store to read from
     * @param target
     *            where to save the fragments
     * @param args
     *            the non-option arguments
     */
    public StoreTagGetStepMain(ClearBlockSource source, FragmentSave target, List<String> args) {
        this.source = source;
        this.target = target;
        this.args = args;
    }

    @Override
    public void run() throws Exception {
        args = new ArrayList<>(args);
        stateDir = args.remove(0);

        new File(stateDir).mkdirs();

        RestoreEngine engine = new RestoreEngine();

        /*
         * load the existing state
         */
        loadState(engine);

        /*
         * load the new tag lists
         */
        for (String tagList : args) {
            try {
                InputStream in = new BufferedInputStream(new FileInputStream(tagList));
                try {
                    for (Tag i : new TagIterator(in)) {
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
                            try {
                                engine.add(name, key);
                            } catch (DuplicateJobName e) {
                                System.err.println("warning: already working on " + name);
                            }
                            continue;
                        }

                        /*
                         * XXX there is also a type=dir where the content is a
                         * tag list.
                         */
                    }
                } finally {
                    in.close();
                }
            } catch (FileNotFoundException e) {
                System.err.println("failed to open: " + tagList + ", skipping");
            }
        }

        /*
         * step the engine
         */
        try {
            /*
             * do the work
             */
            engine.start();
            while (engine.findReady()) {
                Block block = source.get(engine.getKey());
                if (block == null) {
                    engine.skip();
                    continue;
                }

                Fragment part = engine.step(block);

                if (part != null) {
                    target.save(engine.getJobName(), part);
                }

                if (engine.isJobDone()) {
                    logFinished(engine);
                }
            }
        } finally {
            target.close();
        }

        saveNeed(engine);

        /*
         * save the new state
         */
        saveState(engine);
    }

    private void saveNeed(RestoreEngine engine) throws IOException {
        FileWriter out = new FileWriter(new File(stateDir, "need.lst"));
        try {
            for (FullKey key : engine.getNeeded()) {
                out.append(key.toLocatorKey().toString()).append("\n");
            }
        } finally {
            out.close();
        }
    }

    private void logFinished(RestoreEngine engine) throws IOException {
        Writer out = new FileWriter(new File(stateDir, "finised.lst"), true);
        try {
            out.append(engine.getJobName() + "\n");
        } finally {
            out.close();
        }
    }

    private void loadState(RestoreEngine engine) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try {
            Reader stateIn = new BufferedReader(new FileReader(new File(stateDir, "state.yml")));
            RestoreState state = yaml.loadAs(stateIn, RestoreState.class);
            engine.setState(state);
        } catch (FileNotFoundException e) {
            // ignore
        }
    }

    private void saveState(RestoreEngine engine) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        RestoreState state = engine.getState();
        FileWriter out = new FileWriter(new File(stateDir, "state-new.yml"));
        try {
            yaml.dump(state, out);
        } finally {
            out.close();
        }

        if (!new File(stateDir, "state-new.yml").renameTo(new File(stateDir, "state.yml"))) {
            throw new IOException("rename failed");
        }
    }

}
