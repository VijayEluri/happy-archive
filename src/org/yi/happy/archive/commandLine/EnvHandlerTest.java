package org.yi.happy.archive.commandLine;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EnvHandler}, also tests {@link CommandParseEngine}.
 */
public class EnvHandlerTest {
    private CommandParseEngine commandParseEngine;
    private EnvBuilder envBuilder;
    private EnvHandler envHandler;

    /**
     * set up.
     */
    @Before
    public void before() {
        envBuilder = new EnvBuilder();
        envHandler = new EnvHandler(envBuilder);
        commandParseEngine = new CommandParseEngine(envHandler);
    }

    /**
     * tear down.
     */
    @After
    public void after() {
        envBuilder = null;
        envHandler = null;
        commandParseEngine = null;
    }

    /**
     * parse a command line with no arguments or options.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testBareCommand() throws CommandParseException {

        commandParseEngine.parse("show-env");

        Env want = new EnvBuilder().withCommand("show-env").create();
        assertEquals(want, envBuilder.create());
    }

    /**
     * parse a command line with no arguments but with options.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testWithOption() throws CommandParseException {

        commandParseEngine.parse("show-env", "--block-store", "somewhere");

        Env want = new EnvBuilder().withCommand("show-env")
                .withBlockStore("somewhere").create();
        assertEquals(want, envBuilder.create());
    }

    /**
     * parse a command line with arguments and options.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testWithOptionAndFile() throws CommandParseException {

        commandParseEngine.parse("show-env", "--block-store", "somewhere", "file");

        Env want = new EnvBuilder().withCommand("show-env")
                .withBlockStore("somewhere").addArgument("file").create();
        assertEquals(want, envBuilder.create());
    }

    /**
     * I want an empty command line to parse successfully.
     * 
     * @throws CommandParseException
     */
    @Test
    public void testBlank() throws CommandParseException {

        commandParseEngine.parse();

        Env want = new EnvBuilder().create();
        assertEquals(want, envBuilder.create());
    }

    /**
     * I want an option with no name to not parse
     * 
     * @throws CommandParseException
     */
    @Test(expected = CommandParseException.class)
    public void testNoName() throws CommandParseException {
        commandParseEngine.parse("show-env", "--=true");
    }

    /**
     * I want an option starting with "---" to not parse
     * 
     * @throws CommandParseException
     */
    @Test(expected = CommandParseException.class)
    public void testThreeDashes() throws CommandParseException {
        commandParseEngine.parse("show-env", "---");
    }
}
