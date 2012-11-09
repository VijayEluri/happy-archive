package org.yi.happy.archive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.MyEnv;

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

    private static final List<Cmd> cmds = Collections
            .unmodifiableList(Arrays.asList(

            new Cmd("file-get") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreFileGetMain.main(env);
                }
            },

            new Cmd("make-index-db") {
                @Override
                public void run(Env env) throws Exception {
                    MakeIndexDatabaseMain.main(env);
                }
            },

            new Cmd("block-put") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreBlockPutMain.main(env);
                }
            },

            new Cmd("stream-get") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreStreamGetMain.main(env);
                }
            },

            new Cmd("stream-put") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreStreamPutMain.main(env);
                }
            },

            new Cmd("tag-add") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreTagAddMain.main(env);
                }
            },

            new Cmd("decode") {
                @Override
                public void run(Env env) throws Exception {
                    DecodeBlockMain.main(env);
                }
            },

            new Cmd("encode") {
                @Override
                public void run(Env env) throws Exception {
                    EncodeContentMain.main(env);
                }
            },

            new Cmd("verify") {
                @Override
                public void run(Env env) throws Exception {
                    VerifyMain.main(env);
                }

            },

            new Cmd("index-search") {
                @Override
                public void run(Env env) throws Exception {
                    IndexSearchMain.main(env);
                }
            },

            new Cmd("volume-get") {
                @Override
                public void run(Env env) throws Exception {
                    VolumeGetMain.main(env);
                }

            },

            new Cmd("store-list") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreListMain.main(env);
                }
            },

            new Cmd("build-image") {
                @Override
                public void run(Env env) throws Exception {
                    BuildImageMain.main(env);
                }
            },

            new Cmd("index-volume") {
                @Override
                public void run(Env env) throws Exception {
                    IndexVolumeMain.main(env);
                }
            },
            
            new Cmd("store-remove") {
                @Override
                public void run(Env env) throws Exception {
                    StoreRemoveMain.main(env);
                }

            },

            new Cmd("backup-list") {
                @Override
                public void run(Env env) throws Exception {
                    LocalCandidateListMain.main(env);
                }
            },

            new Cmd("show-env") {
                @Override
                public void run(Env env) throws Exception {
                    ShowEnvMain.main(env);
                }
            },

            new Cmd("tag-get") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreTagGetMain.main(env);
                }
            },
            
            new Cmd("tag-put") {
                @Override
                public void run(Env env) throws Exception {
                    FileStoreTagPutMain.main(env);
                }
            }

            ));

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

        help();
    }

    private static void help() {
        for (Cmd cmd : cmds) {
            System.out.println(cmd.getName());
        }
    }
}
