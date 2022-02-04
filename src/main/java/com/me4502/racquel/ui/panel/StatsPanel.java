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
import com.me4502.racquel.mixin.AccessorMinecraft;
import com.me4502.racquel.ui.control.Label;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class StatsPanel extends Panel {
    private final Label lastPacketLabel;
    private final Label latencyLabel;

    public StatsPanel(int x, int y, int width, int height) {
        super("Stats", x, y, width, height);

        addChild(lastPacketLabel = new Label(10, 2, "Last Packet: 0ms"));
        addChild(latencyLabel = new Label(10, lastPacketLabel.getHeight() + 4, "Latency: 0ms"));
    }

    @Override
    public void tick() {
        if (isOpen()) {
            lastPacketLabel.setText("Last Packet: " + (Racquel.lastPacketTime / ((AccessorMinecraft) Minecraft.getInstance()).getTimer().msPerTick) * 1000 + "ms");
            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                latencyLabel.setText("Latency: " + connection.getPlayerInfo(connection.getLocalGameProfile().getId()).getLatency() + "ms");
            } else {
                latencyLabel.setText("Latency: No Server");
            }
        }
    }
}
