package org.yi.happy.archive.commandLine;

import java.util.ArrayList;
import java.util.List;

public class EnvBuilder {
    private String command = null;

    public EnvBuilder withCommand(String command) {
        this.command = command;
        return this;
    }
    
    private String home = null;

    public EnvBuilder withHome(String home) {
        this.home = home;
        return this;
    }

    public boolean hasNoHome() {
        return home == null;
    }

    private String store = null;

    public EnvBuilder withStore(String store) {
        this.store = store;
        return this;
    }

    private String index = null;

    public EnvBuilder withIndex(String index) {
        this.index = index;
        return this;
    }

    private String need = null;

    public EnvBuilder withNeed(String need) {
        this.need = need;
        return this;
    }

    private List<String> arguments = new ArrayList<String>();

    public EnvBuilder addArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    public Env create() {
        return new Env(home, store, index, need, command, arguments);
    }

    public boolean hasHome() {
        return home != null;
    }

    public boolean hasNoStore() {
        return store == null;
    }

    public String getHome() {
        return home;
    }

    public boolean hasNoIndex() {
        return index == null;
    }

}
