package me.camm.productions.wizardrydemo.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class WandGenerator
{
    static ItemStack wand = null;
    static ShapedRecipe recipe = null;
    final static String name = ChatColor.BLUE+""+ChatColor.BOLD+"Magic Wand";

    public static ItemStack getWand(){
        if (wand != null)
            return wand;

        wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        if (meta == null) throw new IllegalStateException("Meta is null");

        meta.setDisplayName(name);
        wand.setItemMeta(meta);
        return wand;
    }


    public static ShapedRecipe getRecipe(Plugin p){

        if (recipe != null)
            return recipe;
        ShapedRecipe r = new ShapedRecipe(new NamespacedKey(p, "Wand"),getWand());
        r.shape("001",
                "020",
                "300");

        RecipeChoice cap = new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_NUGGET));
        RecipeChoice core = new RecipeChoice.ExactChoice(new ItemStack(Material.STICK));
        RecipeChoice end = new RecipeChoice.ExactChoice(new ItemStack(Material.DIAMOND));

        r.setIngredient('1',cap);
        r.setIngredient('2',core);
        r.setIngredient('3',end);

        recipe = r;
        return r;
    }
}
