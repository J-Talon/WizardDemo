package me.camm.productions.wizardrydemo;

import me.camm.productions.wizardrydemo.Items.WandGenerator;
import me.camm.productions.wizardrydemo.Listener.InteractionListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class WizardryDemo extends JavaPlugin {

    static Plugin plugin;


    //constructor
    public WizardryDemo() {
        plugin = this;
    }

    @Override
    public void onEnable() {


        //we are adding a recipe here right when the server starts
        ShapedRecipe r = WandGenerator.getRecipe(this);
        Bukkit.addRecipe(r);


        //register the interaction listener
        getServer().getPluginManager().registerEvents(new InteractionListener(),this);


        //register the command chspell
        /*
          it would be better to have a final constant for the command name,
          but this works too.
         */
        PluginCommand command = getCommand("chspell");
        CommandListener listener = new CommandListener();

        /*
        this can return null if you forgot to register in the plugin.yml file
         */
        if (command == null)
            throw new IllegalStateException("Command chspell is null");

        //set the executor and tab completer
          command.setExecutor(listener);
          command.setTabCompleter(listener);


    }

    //method to get the plugin
    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        //thing about recipes is that you should always remove them when the server stops.
        /*
        why: when the server stops, the recipes are actually stored in the world file.
         if the server runs without the plugin, it may get warnings about some recipes not existing from the
         previous startup
         */
        ShapedRecipe r = WandGenerator.getRecipe(this);
        Bukkit.removeRecipe(r.getKey());
    }
}
