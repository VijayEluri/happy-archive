package org.yi.happy.archive;

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
    private static final Map<String, Provider<MainCommand>> commands;
    static {
        Map<String, Provider<MainCommand>> c;
        c = new LinkedHashMap<String, Provider<MainCommand>>();

        c.put("file-get", MyInjector.providerFileStoreFileGetMain());
        c.put("make-index-db", MyInjector.providerMakeIndexDatabaseMain());
        c.put("decode", MyInjector.providerDecodeBlockMain());
        c.put("encode", MyInjector.providerEncodeContentMain());
        c.put("verify", MyInjector.providerVerifyMain());
        c.put("block-put", MyInjector.providerFileStoreBlockPutMain());
        c.put("stream-put", MyInjector.providerFileStoreStreamPutMain());
        c.put("stream-get", MyInjector.providerFileStoreStreamGetMain());
        c.put("tag-put", MyInjector.providerFileStoreTagPutMain());
        c.put("tag-add", MyInjector.providerFileStoreTagAddMain());
        c.put("tag-get", MyInjector.providerFileStoreTagGetMain());
        c.put("backup-list", MyInjector.providerLocalCandidateListMain());
        c.put("store-list", MyInjector.providerStoreListMain());
        c.put("store-remove", MyInjector.providerStoreRemoveMain());
        c.put("index-search", MyInjector.providerIndexSearchMain());
        c.put("index-volume", MyInjector.providerIndexVolumeMain());
        c.put("build-image", MyInjector.providerBuildImageMain());
        c.put("volume-get", MyInjector.providerVolumeGetMain());
        c.put("show-env", MyInjector.providerShowEnvMain());

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
        String command = env.getCommand();
        Provider<MainCommand> c = commands.get(command);
        if (c != null) {
            c.get().run(env);
            return;
        }

        help();
    }

    private static void help() {
        for (String name : commands.keySet()) {
            System.out.println(name);
        }
    }
}
