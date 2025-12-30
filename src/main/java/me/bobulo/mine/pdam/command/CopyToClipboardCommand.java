package me.bobulo.mine.pdam.command;

import me.bobulo.mine.pdam.util.ClipboardUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Base64;

@SideOnly(Side.CLIENT)
public class CopyToClipboardCommand extends CommandBase {

    public static final String COMMAND_NAME = "pdamcopy";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + COMMAND_NAME + " <text>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(args[0]);
            String decodedText = new String(decodedBytes);
            ClipboardUtils.copyToClipboard(decodedText);
            sender.addChatMessage(
              new ChatComponentText(EnumChatFormatting.GREEN + "Texto copiado para a área de transferência!")
            );
        } catch (IllegalArgumentException e) {
            sender.addChatMessage(
              new ChatComponentText(EnumChatFormatting.RED + "Erro ao decodificar o texto.")
            );
        }
    }
}