package net.kigawa.spigot.pluginutil;

public enum Parents {
    LESS_GREATER("<", ">");

    private final String start;
    private final String end;

    Parents(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String start() {
        return start;
    }

    public String end() {
        return end;
    }
}
