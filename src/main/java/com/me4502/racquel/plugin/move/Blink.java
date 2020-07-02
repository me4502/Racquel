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
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * This cheat works by holding back a select group
 * of movement-related packets until the cheat is
 * disabled. Some variants also have a "reset"
 * button that clears the held packets and sets the
 * player back to where they were at the start.
 *
 * This allows for the player to seemingly
 * "teleport" a short distance, when in reality they
 * have actually moved the distance with the server
 * processing all happening at the end. With a reset
 * mode implemented, this allows for players to try
 * a complex jump or similar over and over again
 * without having to worry about messing up, as
 * they can make only the successful attempt count.
 *
 * One way to detect this cheat is to check what
 * sorts of packets are being delayed. If some packets
 * are coming through fine while others are consistently
 * delayed, it's possible blink is in use. If the
 * implementation is however holding all packets, the
 * cheat will look almost identical to a player with
 * a very intermittent internet connection. These
 * cases are generally not worth worrying about as
 * it's not a very powerful cheat.
 */
public class Blink extends Plugin {

    private final Queue<Packet<?>> packetQueue = new ArrayDeque<>();

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
