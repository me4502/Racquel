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

package com.me4502.racquel.plugin;

import static com.me4502.racquel.Racquel.IDENTIFIER_ID;
import static com.me4502.racquel.Racquel.KEYBINDING_CATEGORY;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.Optional;

/**
 * Represents a plugin to extend the functionality of Racquel.
 */
public abstract class Plugin {

    private boolean enabled;
    private KeyMapping keybind;

    /**
     * Called when the plugin is first loaded.
     */
    public void init() {
        if (getKeyCode() > 0) {
            this.keybind = new KeyMapping(IDENTIFIER_ID + "." + getName(), InputConstants.Type.KEYSYM, getKeyCode(), KEYBINDING_CATEGORY);
            KeyBindingHelper.registerKeyBinding(keybind);
        }
    }

    public String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Convenience method to grab the player.
     *
     * @return The player
     */
    public LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    /**
     * Get the keycode to use for key bindings, or 0 for none.
     *
     * @return The key binding.
     */
    public abstract int getKeyCode();

    /**
     * Gets the registered keybind, if present.
     *
     * @return The keybind
     */
    public Optional<KeyMapping> getKeybind() {
        return Optional.ofNullable(keybind);
    }

    /**
     * Gets whether this plugin is currently impacting the game.
     *
     * @return If enabled
     */
    public boolean isEnabled() {
        return this.enabled && Minecraft.getInstance().player != null;
    }

    /**
     * Toggles the enabled state of this plugin.
     */
    public void toggle() {
        if (isEnabled()) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Enable this plugin.
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Disable this plugin.
     */
    public void disable() {
        this.enabled = false;
    }
}
