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

import com.me4502.racquel.plugin.Plugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.lwjgl.glfw.GLFW;

public class Overclock extends Plugin {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void enable() {
        super.enable();
        Minecraft.getInstance().timer = new Timer(20f * 6.67f, 0, f -> f);
    }

    @Override
    public void disable() {
        super.disable();
        Minecraft.getInstance().timer = new Timer(20, 0, Minecraft.getInstance()::getTickTargetMillis);
    }

    @Override
    public int getKeyCode() {
        return GLFW.GLFW_KEY_O;
    }

}
