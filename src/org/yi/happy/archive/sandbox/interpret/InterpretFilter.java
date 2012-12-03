package org.yi.happy.archive.sandbox.interpret;

import org.yi.happy.archive.tag.BinaryHandler;

public class InterpretFilter implements BinaryHandler {

    private final BinaryHandler handler;
    private final Rules rules;
    private final ActionCallback callback = new ActionCallback() {
        @Override
        public void startStream() {
            sendStartStream();
        }

        @Override
        public void endStream() {
            sendEndStream();
        }

        @Override
        public void send() {
            sendData();
        }

        @Override
        public void startRegion(String name) {
            sendStartRegion(name);
        }

        @Override
        public void endRegion(String name) {
            sendEndRegion(name);
        }
    };

    private Object state;

    public InterpretFilter(Rules rules, BinaryHandler handler) {
        this.rules = rules;
        this.handler = handler;
        this.state = rules.getStartState();
    }

    protected void sendEndRegion(String name) {
        flush();
        handler.endRegion(name);
    }

    protected void sendStartRegion(String name) {
        flush();
        handler.startRegion(name);
    }

    private byte[] sendBuff;
    private int sendStart;
    private int sendCurrent;
    private int sendEnd;

    protected void sendData() {
        if (sendBuff == null) {
            throw new UnsupportedOperationException();
        }

        if (sendCurrent != sendEnd) {
            flush();
        }

        sendEnd++;
    }

    private void flush() {
        if (sendBuff == null) {
            return;
        }

        if (sendStart != sendEnd) {
            handler.bytes(sendBuff, sendStart, sendEnd - sendStart);
        }
        sendStart = sendCurrent;
        sendEnd = sendCurrent;
    }

    protected void sendEndStream() {
        flush();
        handler.endStream();
    }

    protected void sendStartStream() {
        flush();
        handler.startStream();
    }

    @Override
    public void startStream() {
        Rule rule = rules.startStream(state);
        if (rule == null) {
            throw new IllegalStateException();
        }
        rule.getAction().startStream(callback);
        state = rule.getGo();
    }

    @Override
    public void startRegion(String name) {
        Rule rule = rules.startRegion(state, name);
        if (rule == null) {
            throw new IllegalStateException();
        }
        rule.getAction().startRegion(callback, name);
        state = rule.getGo();
    }

    @Override
    public void bytes(byte[] buff, int offset, int length) {
        sendStart = offset;
        sendEnd = offset;
        sendBuff = buff;
        try {
            for (sendCurrent = offset; sendCurrent < offset + length; sendCurrent++) {
                Rule rule = rules.data(state, buff[sendCurrent]);
            if (rule == null) {
                throw new IllegalStateException();
            }
                rule.getAction().data(callback, buff[sendCurrent]);
                state = rule.getGo();
            }
            flush();
        } finally {
            sendBuff = null;
        }
    }

    @Override
    public void endRegion(String name) {
        Rule rule = rules.endRegion(state, name);
        if (rule == null) {
            throw new IllegalStateException();
        }
        rule.getAction().endRegion(callback, name);
        state = rule.getGo();
    }

    @Override
    public void endStream() {
        Rule rule = rules.endStream(state);
        if (rule == null) {
            throw new IllegalStateException();
        }
        rule.getAction().endStream(callback);
        state = rule.getGo();
    }

}
