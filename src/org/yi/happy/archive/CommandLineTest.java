package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Experimental tests for the commons-cli library.
 */
public class CommandLineTest {
    /**
     * Show what happens when an invalid option is given.
     * 
     * @throws ParseException
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testBadOption() throws ParseException {
        Options o = new Options().addOption("a", "apple", true, "Apples");

        String[] arguments = { "--orange", "5" };
        new GnuParser().parse(o, arguments);
    }

    /**
     * End the argument list to catch an option with dashes.
     * 
     * @throws ParseException
     */
    @Test
    public void testArguments() throws ParseException {
        Options o = new Options().addOption("a", "apple", true, "Apples");

        String[] arguments = { "--", "--orange", "5" };
        CommandLine c = new GnuParser().parse(o, arguments);

        assertArrayEquals(new String[] { "--orange", "5" }, c.getArgs());
    }

    /**
     * An option that may be specified more than once.
     * 
     * @throws ParseException
     */
    @Test
    public void testMultipleInstance() throws ParseException {
        Options o = new Options().addOption("a", "apple", true, "Apples");

        String[] args = { "-a", "one", "-a", "two" };
        CommandLine c = new GnuParser().parse(o, args);

        assertArrayEquals(new String[] { "one", "two" }, c
                .getOptionValues("apple"));

        assertEquals("one", c.getOptionValue("apple"));
    }
}
