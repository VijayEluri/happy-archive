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

    /**
     * set up
     */
    @Before
    public void before() {
        log = new LogHandler();
    }

    /**
     * tear down
     */
    @After
    public void after() {
        log = null;
    }

    /**
     * the simple first test, copy everything.
     */
    @Test
    public void testFirst() {
        RuleState state = new RuleState();
        state.add(new OnAnything(), new DoCopy(), state);

        BinaryHandler handler = new InterpretFilter(state, log);
        handler.startStream();
        handler.endStream();

        Assert.assertEquals(Arrays.asList("start", "end"), log.fetchLog());
    }

    /**
     * a purely copying state machine.
     */
    @Test
    public void testCopy() {
        RuleState state = new RuleState();
        state.add(new OnAnything(), new DoCopy(), state);

        BinaryHandler handler = new InterpretFilter(state, log);
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
        RuleState state = new RuleState();
        state.add(new OnByte('='), new DoAll(new DoStartRegion("eq"),
                new DoSend(), new DoEndRegion("eq")), state);
        state.add(new OnAnything(), new DoCopy(), state);

        BinaryHandler handler = new InterpretFilter(state, log);
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
        RuleState outside = new RuleState();
        RuleState inside = new RuleState();
        RuleState nlcr = new RuleState();
        RuleState nllf = new RuleState();
        String line = "line";
        byte cr = '\r';
        byte lf = '\n';

        outside.add(onByte(cr), doAll(doStart(line), doEnd(line), doSend()),
                nlcr);
        outside.add(onByte(lf), doAll(doStart(line), doEnd(line), doSend()),
                nllf);
        outside.add(onAnyByte(), doAll(doStart(line), doSend()), inside);
        outside.add(onStart(), doCopy(), outside);
        outside.add(onEnd(), doCopy(), outside);

        nlcr.add(onByte(cr), doAll(doStart(line), doEnd(line), doSend()),
                nlcr);
        nlcr.add(onByte(lf), doSend(), outside);
        nlcr.add(onAnyByte(), doAll(doStart(line), doSend()), inside);
        nlcr.add(onEnd(), doCopy(), outside);

        nllf.add(onByte(cr), doSend(), outside);
        nllf.add(onByte(lf), doAll(doStart(line), doEnd(line), doSend()),
                nllf);
        nllf.add(onAnyByte(), doAll(doStart(line), doSend()), inside);
        nllf.add(onEnd(), doCopy(), outside);

        inside.add(onByte(cr), doAll(doEnd(line), doSend()), nlcr);
        inside.add(onByte(lf), doAll(doEnd(line), doSend()), nllf);
        inside.add(onAnyByte(), doSend(), inside);
        inside.add(onEnd(), doAll(doEnd(line), doCopy()), outside);

        BinaryHandler handler = new InterpretFilter(outside,
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
