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
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.network.Packet;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import net.minecraft.server.network.packet.PlayerInputC2SPacket;
import net.minecraft.server.network.packet.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.packet.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.packet.PlayerInteractItemC2SPacket;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Queue;

public class Blink extends Plugin {

    private Queue<Packet<?>> packetQueue = new ArrayDeque<>();

    @Override
    public void init() {
        super.init();

        PacketSendCallback.EVENT.register(this::onPacketSend);
    }

    @Override
    public void disable() {
        super.disable();

        Packet<?> pack;
        while ((pack = packetQueue.poll()) != null) {
            getPlayer().networkHandler.getConnection().send(pack, null);
        }
    }

    public ActionResult onPacketSend(Packet<?> packet) {
        if (!isEnabled()) {
            return ActionResult.PASS;
        }

        if (isPacketDelayed(packet)) {
            packetQueue.add(packet);
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    private static boolean isPacketDelayed(Packet<?> packet) {
        return packet instanceof PlayerMoveC2SPacket
                || packet instanceof PlayerInteractBlockC2SPacket
                || packet instanceof PlayerInteractItemC2SPacket
                || packet instanceof PlayerInteractEntityC2SPacket
                || packet instanceof PlayerActionC2SPacket
                || packet instanceof PlayerInputC2SPacket;
    }


    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_B;
    }
}
