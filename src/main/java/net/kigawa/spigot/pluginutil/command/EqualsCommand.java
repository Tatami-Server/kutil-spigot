package net.kigawa.spigot.pluginutil.command;

public class EqualsCommand {
    String command;
    public EqualsCommand(String command){
        this.command=command;
    }
    @Override
    public  boolean equals(Object o){
        return  ((Command)o).getName().equals(command);
    }
}
