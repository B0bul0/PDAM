package me.bobulo.mine.pdam.command;

import com.google.common.base.Joiner;
import me.bobulo.mine.pdam.util.ClipboardUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

        Joiner joiner = Joiner.on(" ");
        ClipboardUtils.copyToClipboard(joiner.join(args));

        sender.addChatMessage(
          new ChatComponentTranslation("pdam.general.copied_to_clipboard")
            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))
        );

    }
}