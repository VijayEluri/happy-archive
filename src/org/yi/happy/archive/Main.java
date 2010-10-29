package org.yi.happy.archive;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.yi.happy.annotate.EntryPoint;

/**
 * The top level entry point that dispatches to the sub-commands.
 */
@EntryPoint
public class Main {
    private static final class Cmd {

        private final String name;
        private final Class<?> cls;

        public Cmd(String name, Class<?> cls) {
            this.name = name;
            this.cls = cls;
        }

        public String getName() {
            return name;
        }

        public Class<?> getCls() {
            return cls;
        }
    }

    private static final List<Cmd> cmds = Collections.unmodifiableList(Arrays
            .asList(

            new Cmd("file-get", FileStoreFileGetMain.class),

            new Cmd("block-put", FileStoreBlockPutMain.class),

            new Cmd("stream-get", FileStoreStreamGetMain.class),

            new Cmd("stream-put", FileStoreStreamPutMain.class),

            new Cmd("tag-get", FileStoreTagGetMain.class),

            new Cmd("tag-put", FileStoreTagPutMain.class),

            new Cmd("decode", DecodeBlockMain.class),

            new Cmd("encode", EncodeContentMain.class),

            new Cmd("verify", VerifyMain.class),

            new Cmd("index-search", IndexSearchMain.class),

            new Cmd("volume-get", VolumeGetMain.class),

            new Cmd("store-list", FileStoreListMain.class),

            new Cmd("build-image", BuildImageMain.class),

            new Cmd("index-volume", IndexVolumeMain.class),

            new Cmd("store-remove", StoreRemoveMain.class),

            new Cmd("backup-list", LocalCandidateListMain.class),

            new Cmd("make-index-db", MakeIndexDatabaseMain.class)
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
                Method m = cmd.getCls().getMethod("main", args.getClass());
                String[] a = Arrays.copyOfRange(args, 1, args.length);
                m.invoke(null, (Object) a);
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
