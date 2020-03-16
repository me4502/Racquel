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

package com.me4502.racquel.plugin.move;

import com.me4502.racquel.event.network.PacketSendCallback;
import com.me4502.racquel.mixin.packet.AccessorPlayerMoveC2SPacket;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

/**
 * This cheat works by setting the "onGround" value of
 * the player to true in all movement packets to the
 * server.
 *
 * This allows the player to never take fall damage,
 * due the client being the absolute authority with
 * this value.
 *
 * The best way to patch this is to keep track of how
 * much downwards vertical movement the player has made
 * since the last time a block was below them. Then
 * apply this damage to the player if they receive it
 * naturally after a few ticks of hitting the ground.
 * You can also check if they consistently are on ground
 * despite falling in mid-air.
 */
public class NoFall extends Plugin {

    @Override
    public void init() {
        super.init();

        PacketSendCallback.EVENT.register(this::onSend);
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_N;
    }

    public ActionResult onSend(Packet<?> packet) {
        if (!isEnabled()) {
            return ActionResult.PASS;
        }

        if (packet instanceof PlayerMoveC2SPacket) {
            ((AccessorPlayerMoveC2SPacket) packet).setOnGround(true);
        }
        return ActionResult.PASS;
    }
}
