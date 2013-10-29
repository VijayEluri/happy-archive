package org.yi.happy.archive.commandLine;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yi.happy.archive.BuildImageMain;
import org.yi.happy.archive.DecodeBlockMain;
import org.yi.happy.archive.EncodeContentMain;
import org.yi.happy.archive.StoreBlockPutMain;
import org.yi.happy.archive.StoreFileGetMain;
import org.yi.happy.archive.StoreListMain;
import org.yi.happy.archive.StoreStreamGetMain;
import org.yi.happy.archive.StoreStreamPutMain;
import org.yi.happy.archive.StoreTagAddMain;
import org.yi.happy.archive.StoreTagGetMain;
import org.yi.happy.archive.StoreTagPutMain;
import org.yi.happy.archive.IndexSearchMain;
import org.yi.happy.archive.IndexVolumeMain;
import org.yi.happy.archive.LocalCandidateListMain;
import org.yi.happy.archive.MainCommand;
import org.yi.happy.archive.MakeIndexDatabaseMain;
import org.yi.happy.archive.ShowEnvMain;
import org.yi.happy.archive.StoreRemoveMain;
import org.yi.happy.archive.VerifyMain;
import org.yi.happy.archive.VolumeGetMain;

/**
 * Print out the full usage of all the commands using reflection. I am doing
 * this so that I can validate the command line and print out help without
 * actually instantiating the command class.
 */
public class MainAnnotateSyntax {
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
        c.put("backup-list", LocalCandidateListMain.class);
        c.put("store-list", StoreListMain.class);
        c.put("store-remove", StoreRemoveMain.class);
        c.put("index-search", IndexSearchMain.class);
        c.put("index-volume", IndexVolumeMain.class);
        c.put("build-image", BuildImageMain.class);
        c.put("volume-get", VolumeGetMain.class);
        c.put("show-env", ShowEnvMain.class);

        commands = Collections.unmodifiableMap(c);
    }

    /**
     * Print out the usage of all the commands.
     * 
     * @param args
     *            takes no arguments.
     */
    public static void main(String[] args) {
        for (String name : commands.keySet()) {
            System.out.println(name + ":");
            Requirement req = process(commands.get(name));

            if (req.getUsesIndex()) {
                System.out.println("  option --store store-path");
            }
            if (req.getUsesIndex()) {
                System.out.println("  option --index index-path");
            }
            if (req.getUsesNeed()) {
                System.out.println("  option --need block-list");
            }

            for (String arg : req.getUsesArgs()) {
                System.out.println("  argument " + arg);
            }

            if (req.isVarArgs()) {
                System.out.println("  at least " + req.getMinArgs()
                        + " arguments");
            } else {
                System.out.println("  exactly " + req.getMinArgs()
                        + " arguments");
            }
        }
    }

    private static Requirement process(Class<? extends MainCommand> cls) {
        RequirementBuilder req = new RequirementBuilder();

        for (Annotation a : cls.getAnnotations()) {
            if (a.annotationType() == UsesArgs.class) {
                req.withUsesArgs(((UsesArgs) a).value());
                continue;
            }

            if (a.annotationType() == UsesStore.class) {
                req.withUsesStore();
                continue;
            }

            if (a.annotationType() == UsesIndex.class) {
                req.withUsesIndex();
                continue;
            }

            if (a.annotationType() == UsesNeed.class) {
                req.withUsesNeed();
                continue;
            }

            System.out.println("  other: " + a);
        }

        return req.create();
    }
}
