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
import com.me4502.racquel.mixin.packet.AccessorServerboundPlayerCommandPacket;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.InteractionResult;
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

        Minecraft.getInstance().getConnection().getConnection().send(
                new ServerboundPlayerCommandPacket(getPlayer(), ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY)
        );
    }

    @Override
    public void disable() {
        super.disable();
    }

    public InteractionResult onPacketSend(Packet<?> packet) {
        if (!isEnabled()) {
            return InteractionResult.PASS;
        }

        if (packet instanceof ServerboundPlayerCommandPacket pack) {
            if (pack.getAction() == ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY) {
                ((AccessorServerboundPlayerCommandPacket) pack).setAction(ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_RIGHT_BRACKET;
    }
}
