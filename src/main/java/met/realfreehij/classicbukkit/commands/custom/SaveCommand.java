package met.realfreehij.classicbukkit.commands.custom;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.commands.CommandIssuer;
import met.realfreehij.classicbukkit.utils.ChatColor;

import java.io.FileOutputStream;
import java.io.IOException;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save", "Save the map", new String[] {"save-all"}, true);
    }

    @Override
    public boolean onExecution(String[] args, CommandIssuer player) {
        try {
        	for(Level level : ClassicBukkit.getServer().levels.values()) { //TODO use Server.saveAll or whatever is it called
        		level.save();
        	}
            
            player.sendChatMessage(ChatColor.GREEN + "Successfully saved the maps!");
        } catch (IOException e) {
            player.sendChatMessage(ChatColor.RED + "Error saving levels");
            e.printStackTrace();
        }
        return false;
    }
}
