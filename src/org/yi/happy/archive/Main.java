package org.yi.happy.archive;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.MyEnv;
import org.yi.happy.archive.commandLine.Requirement;
import org.yi.happy.archive.commandLine.RequirementLoader;

/**
 * The top level entry point that dispatches to the sub-commands.
 */
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
        c.put("backup-critical-list", CriticalListMain.class);
        c.put("store-list", FileStoreListMain.class);
        c.put("store-remove", StoreRemoveMain.class);
        c.put("index-search", IndexSearchMain.class);
        c.put("index-volume", IndexVolumeMain.class);
        c.put("build-image", BuildImageMain.class);
        c.put("volume-get", VolumeGetMain.class);
        c.put("show-env", ShowEnvMain.class);
        c.put("help", HelpMain.class);

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
        Env env = MyEnv.init(args);
        Class<? extends MainCommand> cls = commands.get(env.getCommand());
        if (cls == null) {
            help();
            return;
        }

        Requirement req = RequirementLoader.load(cls);
        if (!RequirementLoader.check(req, env)) {
            explain(req, env);
            return;
        }

        /*
         * inject
         */
        ApplicationScope scope = new ApplicationScope(commands, env);
        MainCommand cmd = getCommandObject(cls, scope);
        cmd.run();
    }

    private static void explain(Requirement req, Env env) {
        if (req.getUsesStore() && env.hasNoStore()) {
            System.err.println("missing option --store store-path");
        }

        if (req.getUsesIndex() && env.hasNoIndex()) {
            System.err.println("missing option --index index-path");
        }

        if (req.getUsesNeed() && env.hasNoNeed()) {
            System.err.println("missing option --need need-file");
        }

        if (env.hasArgumentCount() < req.getMinArgs()) {
            System.err.println("not enough arguments");
        }

        if (!req.isVarArgs() && env.hasArgumentCount() > req.getMinArgs()) {
            System.err.println("too many arguments");
        }

        System.err.print("use: " + env.getCommand());
        for (String arg : req.getUsesArgs()) {
            System.err.print(" " + arg);
        }
        if (req.getUsesInput() != null) {
            System.err.print(" < " + req.getUsesInput());
        }
        if (req.getUsesOutput() != null) {
            System.err.print(" > " + req.getUsesOutput());
        }
        System.err.println();
    }

    private static MainCommand getCommandObject(
            Class<? extends MainCommand> cls, ApplicationScope scope)
            throws Exception {
        String name = cls.getSimpleName();
        Method injectMethod = MyInjector.class.getMethod("inject" + name,
                ApplicationScope.class);
        Object out = injectMethod.invoke(null, scope);
        return cls.cast(out);
    }

    private static void help() {
        for (String name : commands.keySet()) {
            System.out.print(name);

            Requirement req = RequirementLoader.load(commands.get(name));
            for (String arg : req.getUsesArgs()) {
                System.out.print(" " + arg);
            }
            if (req.getUsesInput() != null) {
                System.out.print(" < " + req.getUsesInput());
            }
            if (req.getUsesOutput() != null) {
                System.out.print(" > " + req.getUsesOutput());
            }
            System.out.println();
        }
    }
}
