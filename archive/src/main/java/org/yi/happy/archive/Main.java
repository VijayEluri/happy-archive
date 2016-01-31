package org.yi.happy.archive;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yi.happy.annotate.EntryPoint;
import org.yi.happy.annotate.GlobalOutput;
import org.yi.happy.archive.commandLine.Env;
import org.yi.happy.archive.commandLine.MyEnv;
import org.yi.happy.archive.commandLine.Requirement;
import org.yi.happy.archive.commandLine.RequirementLoader;
import org.yi.happy.archive.gui.RestoreGuiMain;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.TypeLiteral;

/**
 * The top level entry point that dispatches to the sub-commands.
 */
@GlobalOutput
public class Main {
    private static final Map<String, Class<? extends MainCommand>> commands;

    static {
        Map<String, Class<? extends MainCommand>> c;
        c = new LinkedHashMap<String, Class<? extends MainCommand>>();

        c.put("file-get", StoreFileGetMain.class);
        c.put("make-index-db", MakeIndexDatabaseMain.class);
        c.put("decode", DecodeBlockMain.class);
        c.put("encode", EncodeContentMain.class);
        c.put("verify", VerifyMain.class);
        c.put("block-put", StoreBlockPutMain.class);
        c.put("stream-put", StoreStreamPutMain.class);
        c.put("stream-get", StoreStreamGetMain.class);
        c.put("tag-put", StoreTagPutMain.class);
        c.put("tag-add", StoreTagAddMain.class);
        c.put("tag-get", StoreTagGetMain.class);
        c.put("tag-get-step", StoreTagGetStepMain.class);
        c.put("backup-list", LocalCandidateListMain.class);
        c.put("backup-critical-list", CriticalListMain.class);
        c.put("store-list", StoreListMain.class);
        c.put("store-remove", StoreRemoveMain.class);
        c.put("index-search", IndexSearchMain.class);
        c.put("index-search-first", IndexSearchFirstMain.class);
        c.put("index-search-next", IndexSearchNextMain.class);
        c.put("index-search-one", IndexSearchOneMain.class);
        c.put("index-search-prev", IndexSearchPrevMain.class);
        c.put("index-search-last", IndexSearchLastMain.class);
        c.put("index-volume", IndexVolumeMain.class);
        c.put("index-check", IndexCheckMain.class);
        c.put("build-image", BuildImageMain.class);
        c.put("volume-get", VolumeGetMain.class);
        c.put("show-env", ShowEnvMain.class);
        c.put("restore-gui", RestoreGuiMain.class);
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
        if (req.getUsesBlockStore() && env.hasNoBlockStore()) {
            System.err.println("missing option --block-store path");
        }

        if (req.getUsesIndexStore() && env.hasNoIndexStore()) {
            System.err.println("missing option --index-store path");
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

    private static MainCommand getCommandObject(Class<? extends MainCommand> type, final ApplicationScope scope) {
        /*
         * TODO create the Guice Module and Injector here, set it on a static
         * field of MyInjector, and from there you can start to have it take
         * over the inject methods in there.
         * 
         * what about using a static block? There are some injected variables in
         * the ApplicationScope.
         */
        MyInjector.guice = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<List<String>>() {
                }).annotatedWith(EnvArgs.class).toInstance(scope.getArgs());

                bind(String.class).annotatedWith(EnvIndex.class).toInstance(scope.getIndex());
            }
        });

        return MyInjector.inject(type, type.getSimpleName(), scope);
    }

    private static void help() {
        for (String name : commands.keySet()) {
            System.out.print(name);

            Requirement req = RequirementLoader.load(commands.get(name));
            if (req.getUsesNeed()) {
                System.out.print(" --need=key-list");
            }
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
