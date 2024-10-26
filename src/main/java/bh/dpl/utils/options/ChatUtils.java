package bh.dpl.utils.options;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * @author KevinLeft
 */

public class ChatUtils {
    public static void sendMessageClientSide(String string) {
        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(string));
    }
}
