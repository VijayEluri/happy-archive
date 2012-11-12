package org.yi.happy.archive;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.MyEnv;

/**
 * The top level entry point that dispatches to the sub-commands.
 */
@EntryPoint
public class Main {

    private static final Map<String, Class<? extends MainCommand>> commands;
    static {
        Map<String, Class<? extends MainCommand>> c;
        c = new LinkedHashMap<String, Class<? extends MainCommand>>();

        c.put("file-get", FileStoreFileGetMain.class);
        c.put("make-index-db", MakeIndexDatabaseMain.class);
        c.put("decode", DecodeBlockMain.class);
        c.put("encode", EncodeContentMain.class);
        c.put("verify", VerifyMain.class);
        c.put("block-put", FileStoreBlockPutMain.class);
        c.put("stream-put", FileStoreStreamPutMain.class);
        c.put("stream-get", FileStoreStreamGetMain.class);
        c.put("tag-put", FileStoreTagPutMain.class);
        c.put("tag-add", FileStoreTagAddMain.class);
        c.put("tag-get", FileStoreTagGetMain.class);
        c.put("backup-list", LocalCandidateListMain.class);
        c.put("store-list", FileStoreListMain.class);
        c.put("store-remove", StoreRemoveMain.class);
        c.put("index-search", IndexSearchMain.class);
        c.put("index-volume", IndexVolumeMain.class);
        c.put("build-image", BuildImageMain.class);
        c.put("volume-get", VolumeGetMain.class);
        c.put("show-env", ShowEnvMain.class);

        commands = Collections.unmodifiableMap(c);
    }

    /**
     * Run the given sub command, from the command line.
     * 
     * @param args
     *            the arguments
     * @throws Exception
     */
    @EntryPoint
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            help();
            return;
        }

        Env env = MyEnv.init(args);
        Class<? extends MainCommand> cls = commands.get(env.getCommand());
        if (cls == null) {
            help();
            return;
        }

        MainCommand c = getCommandObject(cls);
        if (c != null) {
            c.run(env);
            return;
        }

        help();
    }

    private static MainCommand getCommandObject(Class<? extends MainCommand> cls)
            throws Exception {
        String name = cls.getSimpleName();
        Method injectMethod = MyInjector.class.getMethod("inject" + name);
        if (injectMethod == null) {
            return null;
        }

        Object out = injectMethod.invoke(null);
        return cls.cast(out);
    }

    private static void help() {
        for (String name : commands.keySet()) {
            System.out.println(name);
        }
    }
}
