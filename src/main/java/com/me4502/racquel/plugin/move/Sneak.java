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
import com.me4502.racquel.mixin.packet.AccessorClientCommandC2SPacket;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.server.network.packet.ClientCommandC2SPacket;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public class Sneak extends Plugin {

    @Override
    public void init() {
        super.init();

        PacketSendCallback.EVENT.register(this::onPacketSend);
    }

    @Override
    public void enable() {
        super.enable();

        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(
                new ClientCommandC2SPacket(getPlayer(), ClientCommandC2SPacket.Mode.START_SNEAKING)
        );
    }

    @Override
    public void disable() {
        super.disable();
    }

    public ActionResult onPacketSend(Packet<?> packet) {
        if (!isEnabled()) {
            return ActionResult.PASS;
        }

        if (packet instanceof ClientCommandC2SPacket) {
            ClientCommandC2SPacket pack = (ClientCommandC2SPacket) packet;
            if (pack.getMode() == ClientCommandC2SPacket.Mode.STOP_SNEAKING)
                ((AccessorClientCommandC2SPacket) pack).setMode(ClientCommandC2SPacket.Mode.START_SNEAKING);
        }

        return ActionResult.PASS;
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_RIGHT_BRACKET;
    }
}
