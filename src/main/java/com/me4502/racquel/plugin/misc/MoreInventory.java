package com.me4502.racquel.plugin.misc;

import com.me4502.racquel.event.network.PacketSendCallback;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.network.Packet;
import net.minecraft.server.network.packet.GuiCloseC2SPacket;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

/**
 * This cheat works by preventing the "Close Inventory" packet from
 * being sent to the server when the player's inventory is closed.
 *
 * This allows the player to store items in the 2x2 crafting grid,
 * as this ejection happens on the server.
 *
 * A simple "good enough" prevention of this cheat is to close the
 * inventory on the server if an impossible action occurs. For example,
 * the player mines, places a block, or uses a weapon. Moving is not
 * too safe to use as a check, as there are other possible causes to
 * the player moving that are allowed when in the inventory.
 *
 * The implementation of this cheat is rather naive, and does not check
 * that the window being closed is the correct one. Realistically this
 * has no adverse affects, however does make it easier to detect. This
 * implementation detail was omitted for simplicity as the `id` field
 * is private in the packet class.
 */
public class MoreInventory extends Plugin {

    @Override
    public void init() {
        super.init();

        PacketSendCallback.EVENT.register(this::onSend);
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_K;
    }

    public ActionResult onSend(Packet<?> packet) {
        if (!isEnabled()) {
            return ActionResult.PASS;
        }

        if (packet instanceof GuiCloseC2SPacket) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}
