package org.yi.happy.archive;

import static org.junit.Assert.assertArrayEquals;

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

}
