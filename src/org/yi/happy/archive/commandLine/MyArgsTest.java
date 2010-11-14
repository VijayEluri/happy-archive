package org.yi.happy.archive.commandLine;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yi.happy.archive.commandLine.MyArgs.CommandLineException;


public class MyArgsTest {
    @Test
    public void basicUseStore() throws CommandLineException {
        String[] args = { "--store", "/store/path", "test.txt" };

        MyArgs cmd = MyArgs.parse(args);

        assertEquals("/store/path", cmd.getStore());
        assertArrayEquals(new String[] { "test.txt" }, cmd.getFiles());
    }

}
