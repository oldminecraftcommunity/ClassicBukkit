package met.realfreehij.classicbukkit.commands.custom;

import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.utils.ChatColor;

import java.io.FileOutputStream;
import java.io.IOException;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save", "Save the map", new String[] {"save-all"}, true);
    }

    @Override
    public boolean onExecution(String[] args, PlayerInstance player) {
        try {
            LevelIO.save(ClassicBukkit.getServer().level, new FileOutputStream("server_level.dat"));
            player.sendChatMessage(ChatColor.GREEN + "Successfully saved the map!");
        } catch (IOException e) {
            player.sendChatMessage(ChatColor.RED + "Error saving level");
            e.printStackTrace();
        }
        return false;
    }
}
