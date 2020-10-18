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

import com.google.common.collect.Lists;
import com.me4502.racquel.ui.control.Control;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public abstract class Panel extends AbstractParentElement implements Drawable, TickableElement {

    protected static final int TOP_HEIGHT = MinecraftClient.getInstance().textRenderer.fontHeight + 4;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private boolean pinned;
    private boolean open;

    private double dragXOffset;
    private double dragYOffset;
    private boolean hasMoved;
    private boolean resizing;

    private final String name;
    private final List<Control> children = Lists.newArrayList();

    public Panel(String name, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height + TOP_HEIGHT;
        this.name = name;

        this.open = true;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public boolean isOpen() {
        return this.open;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + TOP_HEIGHT) {
            if (mouseX > x + width - 16) {
                setPinned(!isPinned());
            } else {
                setDragging(true);
                dragXOffset = x - mouseX;
                dragYOffset = y - mouseY;
                hasMoved = false;
            }
        } else if (mouseX >= x + width - 8 && mouseX <= x + width && mouseY >= y + height - 8 && mouseY <= y + height) {
            resizing = true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging()) {
            x = (int) (mouseX + dragXOffset);
            y = (int) (mouseY + dragYOffset);
            hasMoved = true;
        } else if (resizing) {
            width = (int) mouseX - x;
            height = (int) mouseY - y;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging()) {
            setDragging(false);
            if (!hasMoved) {
                open = !open;
            } else {
                x = (int) (mouseX + dragXOffset);
                y = (int) (mouseY + dragYOffset);
                hasMoved = false;
            }
        } else if (resizing) {
            resizing = false;
            width = (int) mouseX - x;
            height = (int) mouseY - y;
        }

        return false;
    }

    @Override
    public final void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Top bar
        fill(matrices, x, y, x + width, y + TOP_HEIGHT, 0x88444444);
        drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, name, x + 2, y + 2, 0xFFFFFFFF);
        drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, pinned ? "-" : "+", x + width - 8, y + 2, 0xFFFFFFFF);

        // Contents
        if (open) {
            fill(matrices, x, y + TOP_HEIGHT, x + width, y + height, 0x88444444);
            renderContents(matrices, mouseX, mouseY, delta);
        }
    }

    public void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (Control child : children) {
            child.updateParentOffsets(this.x, this.y + TOP_HEIGHT);
            child.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public void tick() {
    }

    public void addChild(Control child) {
        children.add(child);
    }

    @Override
    public List<? extends Element> children() {
        return this.children;
    }
}
