package org.yi.happy.archive;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.MyEnv;
import org.yi.happy.archive.file_system.RealFileSystem;

/**
 * The top level entry point that dispatches to the sub-commands.
 */
@EntryPoint
public class Main {
    private abstract static class Cmd {

        private final String name;

        public Cmd(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void run(Env env) throws Exception;
    }

    private static final List<Cmd> cmds = Collections.unmodifiableList(Arrays
            .asList(

            new Cmd("file-get") {
                @Override
                public void run(Env env) throws Exception {
                    new FileStoreFileGetMain(new RealFileSystem(),
                            new WaitHandlerProgressiveDelay()).run(env);
                }
            },

            new Cmd("make-index-db") {
                @Override
                public void run(Env env) throws Exception {
                    new MakeIndexDatabaseMain().run(env);
                }
            },

            new Cmd("block-put") {
                @Override
                public void run(Env env) throws Exception {
                    new FileStoreBlockPutMain(new RealFileSystem()).run(env);
                }
            },

            new Cmd("stream-get") {
                @Override
                public void run(Env env) throws Exception {
                    new FileStoreStreamGetMain(new RealFileSystem(),
                            System.out, new WaitHandlerProgressiveDelay())
                            .run(env);
                }
            },

            new Cmd("stream-put") {
                @Override
                public void run(Env env) throws Exception {
                    new FileStoreStreamPutMain(new RealFileSystem(), System.in,
                            new OutputStreamWriter(System.out, "UTF-8"))
                            .run(env);
                }
            },

            new Cmd("tag-add") {
                @Override
                public void run(Env env) throws Exception {
                    new FileStoreTagAddMain().run(env);
                }
            },

            new Cmd("decode") {
                @Override
                public void run(Env env) throws Exception {
                    new DecodeBlockMain(new RealFileSystem(), System.out)
                            .run(env);
                }
            },

            new Cmd("encode") {
                @Override
                public void run(Env env) throws Exception {
                    new EncodeContentMain(new RealFileSystem(),
                            new OutputStreamWriter(System.out)).run(env);
                }
            },

            new Cmd("verify") {
                @Override
                public void run(Env env) throws Exception {
                    new VerifyMain(new RealFileSystem(), new PrintWriter(
                            System.out, true)).run(env);
                }
            },

            new Cmd("volume-get") {
                @Override
                public void run(Env env) throws Exception {
                    new VolumeGetMain(new RealFileSystem(),
                            new InputStreamReader(System.in, "UTF-8"),
                            new OutputStreamWriter(System.out, "UTF-8"),
                            System.err).run(env);
                }
            },

            new Cmd("build-image") {
                @Override
                public void run(Env env) throws Exception {
                    new BuildImageMain(new RealFileSystem(),
                            new OutputStreamWriter(System.out), System.err)
                            .run(env);
                }
            },

            new Cmd("index-volume") {
                @Override
                public void run(Env env) throws Exception {
                    new IndexVolumeMain(new RealFileSystem(),
                            new OutputStreamWriter(System.out), System.err)
                            .run(env);
                }
            }

            ));

    private static final Map<String, Provider<MainCommand>> commands;
    static {
        Map<String, Provider<MainCommand>> c;
        c = new LinkedHashMap<String, Provider<MainCommand>>();

        c.put("tag-put", MyInjector.providerFileStoreTagPutMain());
        c.put("tag-get", MyInjector.providerFileStoreTagGetMain());
        c.put("show-env", MyInjector.providerShowEnvMain());
        c.put("backup-list", MyInjector.providerLocalCandidateListMain());
        c.put("store-list", MyInjector.providerStoreListMain());
        c.put("store-remove", MyInjector.providerStoreRemoveMain());
        c.put("index-search", MyInjector.providerIndexSearchMain());

        commands = c;
    }

    /**
     * Run the given sub command, from the command line.
     * 
     * @param args
     *            the arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            help();
            return;
        }

        Env env = MyEnv.init(args);
        String command = env.getCommand();
        for (Cmd cmd : cmds) {
            if (cmd.getName().equals(command)) {
                cmd.run(env);
                return;
            }
        }

        Provider<MainCommand> c = commands.get(command);
        if (c != null) {
            c.get().run(env);
            return;
        }

        help();
    }

    private static void help() {
        for (Cmd cmd : cmds) {
            System.out.println(cmd.getName());
        }
        for (String name : commands.keySet()) {
            System.out.println(name);
        }
    }
}
