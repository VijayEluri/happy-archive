package org.yi.happy.archive.tag;

import org.yi.happy.annotate.ExternalValue;
import org.yi.happy.archive.binary_stream.BinaryHandler;
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
        OUTSIDE.add(new OnByte(CR), NL_CR, new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy());
        OUTSIDE.add(new OnByte(LF), NL_LF, new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy());
        OUTSIDE.add(new OnAnyByte(), INSIDE, new DoStartRegion(LINE),
                new DoCopy());
        OUTSIDE.add(new OnAnything(), OUTSIDE, new DoCopy());

        INSIDE.add(new OnByte(CR), NL_CR, new DoEndRegion(LINE), new DoCopy());
        INSIDE.add(new OnByte(LF), NL_LF, new DoEndRegion(LINE), new DoCopy());
        INSIDE.add(new OnAnyByte(), INSIDE, new DoCopy());
        INSIDE.add(new OnAnything(), OUTSIDE, new DoEndRegion(LINE),
                new DoCopy());

        NL_CR.add(new OnByte(CR), NL_CR, new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy());
        NL_CR.add(new OnByte(LF), OUTSIDE, new DoCopy());
        NL_CR.add(new OnAnyByte(), INSIDE, new DoStartRegion(LINE),
                new DoCopy());
        NL_CR.add(new OnAnything(), OUTSIDE, new DoCopy());

        NL_LF.add(new OnByte(CR), OUTSIDE, new DoCopy());
        NL_LF.add(new OnByte(LF), NL_LF, new DoStartRegion(LINE),
                new DoEndRegion(LINE), new DoCopy());
        NL_LF.add(new OnAnyByte(), INSIDE, new DoStartRegion(LINE),
                new DoCopy());
        NL_LF.add(new OnAnything(), OUTSIDE, new DoCopy());
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
