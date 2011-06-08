package org.yi.happy.archive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.yi.happy.annotate.EntryPoint;

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

        public abstract void run(String[] args) throws Exception;
    }

    private static final List<Cmd> cmds = Collections.unmodifiableList(Arrays
            .asList(

            new Cmd("file-get") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreFileGetMain.main(args);
                }
            },

            new Cmd("block-put") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreBlockPutMain.main(args);
                }
            },

            new Cmd("stream-get") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreStreamGetMain.main(args);
                }
            },

            new Cmd("stream-put") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreStreamPutMain.main(args);
                }
            },

            new Cmd("tag-get") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreTagGetMain.main(args);
                }
            },

            new Cmd("tag-put") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreTagPutMain.main(args);
                }
            },
            
            new Cmd("tag-add") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreTagAddMain.main(args);
                }
            },

            new Cmd("decode") {
                @Override
                public void run(String[] args) throws Exception {
                    DecodeBlockMain.main(args);
                }
            },

            new Cmd("encode") {
                @Override
                public void run(String[] args) throws Exception {
                    EncodeContentMain.main(args);
                }
            },

            new Cmd("verify") {
                @Override
                public void run(String[] args) throws Exception {
                    VerifyMain.main(args);
                }
            },

            new Cmd("index-search") {
                @Override
                public void run(String[] args) throws Exception {
                    IndexSearchMain.main(args);
                }
            },

            new Cmd("volume-get") {
                @Override
                public void run(String[] args) throws Exception {
                    VolumeGetMain.main(args);
                }
            },

            new Cmd("store-list") {
                @Override
                public void run(String[] args) throws Exception {
                    FileStoreListMain.main(args);
                }
            },

            new Cmd("build-image") {
                @Override
                public void run(String[] args) throws Exception {
                    BuildImageMain.main(args);
                }
            },

            new Cmd("index-volume") {
                @Override
                public void run(String[] args) throws Exception {
                    IndexVolumeMain.main(args);
                }
            },

            new Cmd("store-remove") {
                @Override
                public void run(String[] args) throws Exception {
                    StoreRemoveMain.main(args);
                }
            },

            new Cmd("backup-list") {
                @Override
                public void run(String[] args) throws Exception {
                    LocalCandidateListMain.main(args);
                }
            },

            new Cmd("make-index-db") {
                @Override
                public void run(String[] args) throws Exception {
                    MakeIndexDatabaseMain.main(args);
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

        for (Cmd cmd : cmds) {
            if (cmd.getName().equals(args[0])) {
                String[] a = Arrays.copyOfRange(args, 1, args.length);
                cmd.run(a);
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
