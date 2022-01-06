package net.kigawa.spigot.pluginutil.command;

import net.kigawa.interfaces.Named;
import net.kigawa.log.LogSender;
import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class TabList implements Named, LogSender {
    List<TabList> tabLists;

    /**
     * @deprecated
     */
    public TabList(PluginBase pluginBase) {
        this();
    }

    public TabList() {
        List<TabList> tabLists;
        tabLists = new ArrayList<>();
        this.tabLists = tabLists;
    }

    public abstract int getWordNumber();

    public abstract List<String> getTabStrings(CommandSender sender, Command command, String label, String[] strings);

    public void addTabLists(TabList tabList) {
        tabLists.add(tabList);
    }

    public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        //new instance
        List<String> tabListStr = null;
        //check null

        if (tabLists != null) {
            //when send here
            if (strings.length == getWordNumber() + 1) {
                //get tab list
                tabListStr = getTabStrings(commandSender, command, s, strings);
                //when null
                if (tabListStr == null) {
                    //new list
                    tabListStr = new ArrayList<>();
                    for (TabList tabList : tabLists) {
                        tabListStr.add(tabList.getName());
                    }
                }
            }


            //when do not send here
            if (strings.length > getWordNumber() + 1) {
                //new list
                tabListStr = new ArrayList<>();
                //check contain tabList
                if (tabLists.contains(new EqualsCommand(strings[getWordNumber()]))) {
                    TabList tabList = tabLists.get(tabLists.indexOf(new EqualsCommand(strings[getWordNumber()])));
                    tabListStr = tabList.tabComplete(commandSender, command, s, strings);
                }
            }
        }

        return tabListStr;
    }
}
