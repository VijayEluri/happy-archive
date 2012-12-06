package org.yi.happy.archive.sandbox.interpret;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yi.happy.archive.ByteString;
import org.yi.happy.archive.tag.BinaryHandler;
import org.yi.happy.archive.tag.LogHandler;

/**
 * Tests for {@link InterpretFilter}.
 */
public class ParseTest {
    private LogHandler log;
    private Rules<Object> rules;

    /**
     * set up
     */
    @Before
    public void before() {
        log = new LogHandler();
        rules = new Rules<Object>();
    }

    /**
     * tear down
     */
    @After
    public void after() {
        log = null;
        rules = null;
    }

    /**
     * the simple first test, copy everything.
     */
    @Test
    public void testFirst() {
        rules.add(null, new Rule<Object>(new OnAnything(), new DoCopy(), null));

        BinaryHandler handler = new InterpretFilter(rules.getState(null), log);
        handler.startStream();
        handler.endStream();

        Assert.assertEquals(Arrays.asList("start", "end"), log.fetchLog());
    }

    /**
     * a purely copying state machine.
     */
    @Test
    public void testCopy() {
        rules.add(null, new Rule<Object>(new OnAnything(), new DoCopy(), null));

        BinaryHandler handler = new InterpretFilter(rules.getState(null), log);
        handler.startStream();
        handler.bytes(ByteString.toBytes("ab"), 0, 2);
        handler.startRegion("eq");
        handler.bytes(ByteString.toBytes("="), 0, 1);
        handler.endRegion("eq");
        handler.endStream();

        Assert.assertEquals(Arrays.asList("start", "bytes ab", "start eq",
                "bytes =", "end eq", "end"), log.fetchLog());
    }

    /**
     * do a very simple single character marking state machine.
     */
    @Test
    public void testSimpleMark() {
        rules.add(null, new Rule<Object>(new OnByte('='), new DoAll(
                new DoStartRegion("eq"), new DoSend(), new DoEndRegion("eq")),
                null));
        rules.add(null, new Rule<Object>(new OnAnything(), new DoCopy(), null));

        BinaryHandler handler = new InterpretFilter(rules.getState(null), log);
        handler.startStream();
        handler.bytes(ByteString.toBytes("ab="), 0, 3);
        handler.endStream();

        Assert.assertEquals(Arrays.asList("start", "bytes ab", "start eq",
                "bytes =", "end eq", "end"), log.fetchLog());
    }

    /**
     * Test using the full state machine for marking lines.
     */
    @Test
    public void testLine() {
        String outside = "outside";
        String inside = "inside";
        String nlcr = "nlcr";
        String nllf = "nllf";
        String line = "line";
        byte cr = '\r';
        byte lf = '\n';

        rule(outside, onByte(cr), doAll(doStart(line), doEnd(line), doSend()),
                nlcr);
        rule(outside, onByte(lf), doAll(doStart(line), doEnd(line), doSend()),
                nllf);
        rule(outside, onAnyByte(), doAll(doStart(line), doSend()), inside);
        rule(outside, onStart(), doCopy(), outside);
        rule(outside, onEnd(), doCopy(), outside);

        rule(nlcr, onByte(cr), doAll(doStart(line), doEnd(line), doSend()),
                nlcr);
        rule(nlcr, onByte(lf), doSend(), outside);
        rule(nlcr, onAnyByte(), doAll(doStart(line), doSend()), inside);
        rule(nlcr, onEnd(), doCopy(), outside);

        rule(nllf, onByte(cr), doSend(), outside);
        rule(nllf, onByte(lf), doAll(doStart(line), doEnd(line), doSend()),
                nllf);
        rule(nllf, onAnyByte(), doAll(doStart(line), doSend()), inside);
        rule(nllf, onEnd(), doCopy(), outside);

        rule(inside, onByte(cr), doAll(doEnd(line), doSend()), nlcr);
        rule(inside, onByte(lf), doAll(doEnd(line), doSend()), nllf);
        rule(inside, onAnyByte(), doSend(), inside);
        rule(inside, onEnd(), doAll(doEnd(line), doCopy()), outside);

        BinaryHandler handler = new InterpretFilter(rules.getState(outside),
                log);
        handler.startStream();
        handler.bytes(ByteString.toBytes("ab\rc\n\rd\n\n"), 0, 9);
        handler.endStream();

        Assert.assertEquals(Arrays.asList("start", "start line", "bytes ab",
                "end line", "bytes \r", "start line", "bytes c", "end line",
                "bytes \n\r", "start line", "bytes d", "end line", "bytes \n",
                "start line", "end line", "bytes \n", "end"), log.fetchLog());
    }

    private OnCondition onStart() {
        return new OnStartStream();
    }

    private OnCondition onEnd() {
        return new OnEndStream();
    }

    private OnAnyByte onAnyByte() {
        return new OnAnyByte();
    }

    private DoSend doSend() {
        return new DoSend();
    }

    private void rule(Object inState, OnCondition onCondition,
            DoAction doAction, Object goState) {
        rules.add(inState, new Rule<Object>(onCondition, doAction, goState));
    }

    private DoAll doAll(DoAction... actions) {
        return new DoAll(actions);
    }

    private DoCopy doCopy() {
        return new DoCopy();
    }

    private DoStartRegion doStart(String name) {
        return new DoStartRegion(name);
    }

    private DoEndRegion doEnd(String name) {
        return new DoEndRegion(name);
    }

    private OnByte onByte(byte value) {
        return new OnByte(value);
    }

}
