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
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.lwjgl.glfw.GLFW;

public class FastMoving extends Plugin {
    private final float speed = 0.6f;

    // Store prior values to reset
    private double oldWalk;
    private double oldFly;

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_F;
    }

    @Override
    public void init() {
        super.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void enable() {
        super.enable();

        oldWalk = getPlayer().getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
        oldFly = getPlayer().getAttribute(Attributes.FLYING_SPEED).getBaseValue();

        getPlayer().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        getPlayer().getAttribute(Attributes.FLYING_SPEED).setBaseValue(speed);
    }

    @Override
    public void disable() {
        super.disable();

        getPlayer().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(oldWalk);
        getPlayer().getAttribute(Attributes.FLYING_SPEED).setBaseValue(oldFly);
    }

    public void onTick(Minecraft client) {
        if (!isEnabled()) {
            return;
        }

        getPlayer().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        getPlayer().getAttribute(Attributes.FLYING_SPEED).setBaseValue(speed);
    }
}
