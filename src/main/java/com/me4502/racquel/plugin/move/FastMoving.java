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
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttributes;
import org.lwjgl.glfw.GLFW;

public class FastMoving extends Plugin {

    // The original value is inlined by the compiler
    private final static float BASE_FLY_SPEED = 0.02f;

    private final float speed = 0.6f;

    // Store prior values to reset
    private double oldWalk;
    private float oldFly;

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

        oldWalk = getPlayer().getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getBaseValue();
        oldFly = BASE_FLY_SPEED;

        getPlayer().getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        getPlayer().flyingSpeed = speed;
    }

    @Override
    public void disable() {
        super.disable();

        getPlayer().getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(oldWalk);
        getPlayer().flyingSpeed = oldFly;
    }

    public void onTick(MinecraftClient client) {
        if (!isEnabled()) {
            return;
        }

        getPlayer().getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        getPlayer().flyingSpeed = speed;
    }
}
