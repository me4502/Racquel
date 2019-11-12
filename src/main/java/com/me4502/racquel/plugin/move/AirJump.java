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

import com.me4502.racquel.plugin.Plugin;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/**
 * This cheat works by setting the "onGround" value of
 * the player to true at all times.
 *
 * This allows the player to always be able to jump,
 * even when in the air. Due to the client using this
 * value as an "allowance" for jumping, just simply
 * setting this value works fine.
 *
 * The best way to patch this is to check for sudden
 * changes in upwards vertical velocity caused by the
 * client, when the player is not directly above any
 * blocks.
 */
public class AirJump extends Plugin {

    @Override
    public void init() {
        super.init();

        ClientTickCallback.EVENT.register(this::onTick);
    }

    public void onTick(MinecraftClient client) {
        if (!isEnabled()) {
            return;
        }

        getPlayer().onGround = true;
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_J;
    }
}
