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

package com.me4502.racquel.plugin.combat;

import com.me4502.racquel.event.network.PacketHandleCallback;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.InteractionResult;
import org.lwjgl.glfw.GLFW;

/**
 * This cheat works by refusing to handle packets that update
 * the player's velocity from the server.
 *
 * This prevents knockback, or any other server-caused velocity
 * changes from affecting the player. Due to the client being
 * the absolute authority on "local movement" on the Minecraft
 * server, this cheat is possible.
 *
 * Like most movement-related cheats, this one is harder to
 * patch. A simple approach would be to check if the player
 * has moved in the "correct" direction over the next few
 * player position update packets, however cheat clients
 * can easily adapt to perform small but non-disruptive
 * movements of the player. This is one of the cheats that
 * is best solved by looking for smaller inconsistencies
 * over a longer period of time, as well as blocking obvious
 * cases.
 */
public class NoKnockback extends Plugin {

    @Override
    public void init() {
        super.init();

        PacketHandleCallback.EVENT.register(this::onReceive);
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_K;
    }

    public InteractionResult onReceive(Packet<?> packet) {
        if (!isEnabled()) {
            return InteractionResult.PASS;
        }

        if (packet instanceof ClientboundSetEntityMotionPacket) {
            if (((ClientboundSetEntityMotionPacket) packet).getId() == getPlayer().getId()) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }
}
