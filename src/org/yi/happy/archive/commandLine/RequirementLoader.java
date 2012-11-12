package org.yi.happy.archive.commandLine;

import java.lang.annotation.Annotation;

import org.yi.happy.archive.MainCommand;

/**
 * Load the requirements from the annotations on a class. Used to allow the
 * command line to be verified and the command usage to be printed without
 * instantiating the command object.
 */
public class RequirementLoader {
    /**
     * Load the requirements from the given command class.
     * 
     * @param cls
     *            the class to load the requirements from.
     * @return the invocation requirements.
     */
    public static Requirement load(Class<? extends MainCommand> cls) {
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
        }

        return req.create();
    }

    /**
     * check an invocation requirement against an invocation environment.
     * 
     * @param requirement
     *            the invocation requirement.
     * @param env
     *            the invocation environment.
     * @return true if the requirements are met.
     */
    public static boolean check(Requirement requirement, Env env) {
        if (requirement.getUsesIndex() && env.hasNoIndex()) {
            return false;
        }

        if (requirement.getUsesStore() && env.hasNoStore()) {
            return false;
        }

        if (requirement.getUsesNeed() && env.hasNoNeed()) {
            return false;
        }

        if (requirement.isVarArgs()) {
            if (env.hasArgumentCount() < requirement.getMinArgs()) {
                return false;
            }
        } else {
            if (env.hasArgumentCount() != requirement.getMinArgs()) {
                return false;
            }
        }

        return true;
    }

    /**
     * check an invocation requirement from a class against an invocation
     * environment.
     * 
     * @param cls
     *            the class to get the invocation requirement from.
     * @param env
     *            the invocation environment.
     * @return true if the requirements are met.
     */
    public static boolean check(Class<? extends MainCommand> cls, Env env) {
        Requirement requirement = load(cls);
        return check(requirement, env);
    }
}
