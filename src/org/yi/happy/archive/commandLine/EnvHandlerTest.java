package org.yi.happy.archive.commandLine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link EnvHandler}, also tests {@link CommandParseEngine}.
 */
public class EnvHandlerTest {
    /**
     * parse a command line with no arguments or options.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testBareCommand() throws CommandParseException {
        EnvBuilder env = new EnvBuilder();

        new CommandParseEngine(new EnvHandler(env)).parse("show-env");

        Env want = new EnvBuilder().withCommand("show-env").create();
        assertEquals(want, env.create());
    }

    /**
     * parse a command line with no arguments but with options.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testWithOption() throws CommandParseException {
        EnvBuilder env = new EnvBuilder();

        new CommandParseEngine(new EnvHandler(env)).parse("show-env",
                "--store", "somewhere");

        Env want = new EnvBuilder().withCommand("show-env")
                .withStore("somewhere").create();
        assertEquals(want, env.create());
    }

    /**
     * parse a command line with arguments and options.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testWithOptionAndFile() throws CommandParseException {
        EnvBuilder env = new EnvBuilder();

        new CommandParseEngine(new EnvHandler(env)).parse("show-env",
                "--store", "somewhere", "file");

        Env want = new EnvBuilder().withCommand("show-env")
                .withStore("somewhere").addArgument("file").create();
        assertEquals(want, env.create());
    }

    /**
     * I want an empty command line to parse successfully.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testBlank() throws CommandParseException {
        EnvBuilder env = new EnvBuilder();

        new CommandParseEngine(new EnvHandler(env)).parse();

        Env want = new EnvBuilder().create();
        assertEquals(want, env.create());
    }

    /**
     * I want an option with no name to not parse
     * 
     * @throws CommandParseException
     */
    @Test(expected = CommandParseException.class)
    public void testNoName() throws CommandParseException {
        EnvBuilder env = new EnvBuilder();

        new CommandParseEngine(new EnvHandler(env))
                .parse("show-env", "--=true");
    }

    /**
     * I want an option starting with "---" to not parse
     * 
     * @throws CommandParseException
     */
    @Test(expected = CommandParseException.class)
    public void testThreeDashes() throws CommandParseException {
        EnvBuilder env = new EnvBuilder();

        new CommandParseEngine(new EnvHandler(env)).parse("show-env", "---");
    }

}
