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

package com.me4502.racquel.ui.panel;

import com.me4502.racquel.Racquel;
import com.me4502.racquel.plugin.Plugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import static com.me4502.racquel.util.RenderUtils.rgbToInt;

public class PluginsPanel extends Panel {

    public PluginsPanel(int x, int y, int width, int height) {
        super("Plugins", x, y, width, height);

        setPinned(true);
    }

    @Override
    public void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int renderIndex = 0;
        for (Plugin plugin : Racquel.INSTANCE.getPlugins()) {
            if (plugin.isEnabled()) {
                MinecraftClient.getInstance().textRenderer.draw(
                    matrices,
                    plugin.getName(),
                    x + 6,
                    y + (renderIndex++ * 10) + TOP_HEIGHT,
                    rgbToInt(0, 255, 0)
                );
            }
        }
    }
}
