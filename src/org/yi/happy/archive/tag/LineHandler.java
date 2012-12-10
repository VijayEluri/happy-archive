package org.yi.happy.archive.tag;

import org.yi.happy.annotate.ExternalValue;
import org.yi.happy.archive.binary_stream.BinaryHandler;
import org.yi.happy.archive.binary_stream.DoAll;
import org.yi.happy.archive.binary_stream.DoCopy;
import org.yi.happy.archive.binary_stream.DoEndRegion;
import org.yi.happy.archive.binary_stream.DoStartRegion;
import org.yi.happy.archive.binary_stream.OnAnyByte;
import org.yi.happy.archive.binary_stream.OnAnything;
import org.yi.happy.archive.binary_stream.OnByte;
import org.yi.happy.archive.binary_stream.RuleState;
import org.yi.happy.archive.binary_stream.StateByteFilter;

/**
 * Mark lines inside the stream or regions.
 * 
 * Regions labeled line, with the newline characters between them. There is
 * always a line before any newline. After the final newline does not count as a
 * line unless there is something on it. Between pairs of newlines there are
 * empty lines marked.
 */
public class LineHandler extends StateByteFilter {
    /**
     * The line label.
     */
    public static final String LINE = "line";

    /**
     * one of the end of line characters.
     */
    @ExternalValue
    public static final byte CR = '\r';

    /**
     * one of the end of line characters.
     */
    @ExternalValue
    public static final byte LF = '\n';

    /**
     * outside of a line and a newline sequence, right before the start of a
     * line.
     */
    private static final RuleState OUTSIDE = new RuleState();

    /**
     * inside a line.
     */
    private static final RuleState INSIDE = new RuleState();

    /**
     * after the first carrier-return of a newline sequence.
     */
    private static final RuleState NL_CR = new RuleState();

    /**
     * after the first line-feed of a newline sequence.
     */
    private static final RuleState NL_LF = new RuleState();

    static {
        OUTSIDE.add(new OnByte(CR), new DoAll(new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy()), NL_CR);
        OUTSIDE.add(new OnByte(LF), new DoAll(new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy()), NL_LF);
        OUTSIDE.add(new OnAnyByte(), new DoAll(new DoStartRegion(LINE),
                new DoCopy()), INSIDE);
        OUTSIDE.add(new OnAnything(), new DoCopy(), OUTSIDE);

        INSIDE.add(new OnByte(CR), new DoAll(new DoEndRegion(LINE),
                new DoCopy()), NL_CR);
        INSIDE.add(new OnByte(LF), new DoAll(new DoEndRegion(LINE),
                new DoCopy()), NL_LF);
        INSIDE.add(new OnAnyByte(), new DoCopy(), INSIDE);
        INSIDE.add(new OnAnything(), new DoAll(new DoEndRegion(LINE),
                new DoCopy()), OUTSIDE);

        NL_CR.add(new OnByte(CR), new DoAll(new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy()), NL_CR);
        NL_CR.add(new OnByte(LF), new DoCopy(), OUTSIDE);
        NL_CR.add(new OnAnyByte(), new DoAll(new DoStartRegion(LINE),
                new DoCopy()), INSIDE);
        NL_CR.add(new OnAnything(), new DoCopy(), OUTSIDE);

        NL_LF.add(new OnByte(CR), new DoCopy(), OUTSIDE);
        NL_LF.add(new OnByte(LF), new DoAll(new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy()), NL_LF);
        NL_LF.add(new OnAnyByte(), new DoAll(new DoStartRegion(LINE),
                new DoCopy()), INSIDE);
        NL_LF.add(new OnAnything(), new DoCopy(), OUTSIDE);
    }

    /**
     * set up {@link LineHandler} with a handler to accept the events.
     * 
     * @param handler
     *            the handler that accepts the events.
     */
    public LineHandler(BinaryHandler handler) {
        super(OUTSIDE, handler);
    }
}
