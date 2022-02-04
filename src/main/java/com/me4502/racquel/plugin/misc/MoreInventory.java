/*
 * MIT License
 *
 * Copyright (c) 2019 Madeline Miller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.me4502.racquel.plugin.misc;

import com.me4502.racquel.event.network.PacketSendCallback;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.InteractionResult;
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

    public InteractionResult onSend(Packet<?> packet) {
        if (!isEnabled()) {
            return InteractionResult.PASS;
        }

        if (packet instanceof ServerboundContainerClosePacket) {
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
