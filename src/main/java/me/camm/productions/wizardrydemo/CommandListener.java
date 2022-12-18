package me.camm.productions.wizardrydemo;

import me.camm.productions.wizardrydemo.Items.SpellSelector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

//listener for commands
public class CommandListener implements CommandExecutor, TabCompleter {


    //whenever the player sends a command that we have set, this is called
    /*
    return true: don't send any message to the player
    return false: send a usage message to the player
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        //we check to make sure it isn't the server console that is sending the command
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command");
            return true;
        }

        /*
        we check to ensure that it is a certain command label first.
          this is because multiple commands can have 1 executor
         */
        if (!label.equalsIgnoreCase("chspell")) {
            return true;
        }


        //we check the arguments
        if (args.length != 1) {
            commandSender.sendMessage("Must have 1 positive integer argument");
            return true;
        }

        int result;
        try {

            //try to parse the value as an int, and check it.
            result = Integer.parseInt(args[0]);
            if (result < 0 || result >= 4) {
                commandSender.sendMessage("Number must be positive, n >= 0, n < 4");
                return true;
            }
        }
        catch (NumberFormatException e) {
            commandSender.sendMessage("Argument is not an int");
            return true;
        }


        //store the slot the player wanted, and open the spell selector inventory
        Player player = (Player)commandSender;
        SpellSelector.store(player.getUniqueId(), result);
        player.openInventory(SpellSelector.getSelector());

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        //to keep it simple, we just return the options 0-3 whenever the player tab completes
        List<String> values = new ArrayList<>();

            values.add("0");
            values.add("1");
            values.add("2");
            values.add("3");

        return values;

    }
}
