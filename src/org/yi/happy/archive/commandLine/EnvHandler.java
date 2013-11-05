package org.yi.happy.archive.commandLine;

/**
 * Command line parsing handler for the invocation environment object.
 */
public class EnvHandler implements CommandParseHandler {
    private final EnvBuilder env;

    /**
     * Create with a new environment builder.
     */
    public EnvHandler() {
        this(new EnvBuilder());
    }

    /**
     * Create with an existing environment builder, the builder is used
     * directly.
     * 
     * @param envBuilder
     *            the environment builder to add to.
     */
    public EnvHandler(EnvBuilder envBuilder) {
        this.env = envBuilder;
    }

    /**
     * @return the environment builder.
     */
    public EnvBuilder getEnvBuilder() {
        return env;
    }

    private boolean needCommand = true;

    @Override
    public void onArgument(String argument) {
        if (needCommand) {
            env.withCommand(argument);
            needCommand = false;
            return;
        }
        env.addArgument(argument);
    }

    @Override
    public void onOption(String name, String value)
            throws CommandParseException {
        if (needCommand) {
            throw new CommandParseException(
                    "the first argument must be a command");
        }

        if (name.equals("archive-home")) {
            env.withHome(value);
            return;
        }

        if (name.equals("block-store")) {
            env.withBlockStore(value);
            return;
        }

        if (name.equals("index-store")) {
            env.withIndexStore(value);
            return;
        }

        if (name.equals("need")) {
            env.withNeed(value);
            return;
        }

        throw new CommandParseException("unrecognized option: " + name);
    }

    @Override
    public void onFinished() throws CommandParseException {
    }
}
