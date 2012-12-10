package org.yi.happy.archive.binary_stream;

import java.util.ArrayList;
import java.util.List;

import org.yi.happy.archive.ByteString;

/**
 * Log the events sent to a binary handler.
 */
public class LogHandler implements BinaryHandler {
    private List<String> log = new ArrayList<String>();

    private String data = null;

    @Override
    public void startStream() {
        if (data != null) {
            log.add("bytes " + data);
            data = null;
        }

        log.add("start");
    }

    @Override
    public void startRegion(String name) {
        if (data != null) {
            log.add("bytes " + data);
            data = null;
        }

        log.add("start " + name);
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        if (data == null) {
            data = "";
        }

        data = data + ByteString.toString(buff, offset, length);
    }

    @Override
    public void endRegion(String name) {
        if (data != null) {
            log.add("bytes " + data);
            data = null;
        }

        log.add("end " + name);
    }

    @Override
    public void endStream() {
        if (data != null) {
            log.add("bytes " + data);
            data = null;
        }

        log.add("end");
    }

    /**
     * Get the log and reset it.
     * 
     * @return the log.
     */
    public List<String> fetchLog() {
        if (data != null) {
            log.add("bytes " + data);
            data = null;
        }

        List<String> out = log;
        log = new ArrayList<String>();
        return out;
    }
}
