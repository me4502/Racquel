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

package com.me4502.racquel;

import com.me4502.racquel.event.render.PostGuiRenderCallback;
import com.me4502.racquel.plugin.Plugin;
import com.me4502.racquel.plugin.combat.NoKnockback;
import com.me4502.racquel.plugin.misc.MoreInventory;
import com.me4502.racquel.plugin.misc.Overclock;
import com.me4502.racquel.plugin.move.AirJump;
import com.me4502.racquel.plugin.move.Blink;
import com.me4502.racquel.plugin.move.FastMoving;
import com.me4502.racquel.plugin.move.NoFall;
import com.me4502.racquel.plugin.move.NoSlow;
import com.me4502.racquel.plugin.move.Sneak;
import com.me4502.racquel.plugin.move.WallClimb;
import com.me4502.racquel.ui.panel.Panel;
import com.me4502.racquel.ui.screen.ConsoleScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Racquel implements ModInitializer {

	public static Racquel INSTANCE;

	public static final String IDENTIFIER_ID = "racquel";
	public static final String KEYBINDING_CATEGORY = "Racquel";

	private final List<Plugin> plugins = new ArrayList<>();

	private KeyMapping consoleKeybind;
	private ConsoleScreen consoleScreen;

	public static int lastPacketTime = 0;

	public Racquel() {
		INSTANCE = this;
	}

	@Override
	public void onInitialize() {
		consoleKeybind = new KeyMapping(IDENTIFIER_ID + ".console", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, KEYBINDING_CATEGORY);
		KeyBindingHelper.registerKeyBinding(consoleKeybind);

		System.out.println("Registering plugins");
		registerPlugins();

		System.out.println("Initialising core-events");
		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
		PostGuiRenderCallback.EVENT.register(this::onPostRender);
		ClientLifecycleEvents.CLIENT_STARTED.register(this::clientStarted);

		plugins.forEach(Plugin::init);
	}

	public void clientStarted(Minecraft minecraftClient) {
		consoleScreen = new ConsoleScreen();
	}

	public void registerPlugins() {
		plugins.add(new FastMoving());
		plugins.add(new AirJump());
		plugins.add(new NoFall());
		plugins.add(new WallClimb());
		plugins.add(new Sneak());
		plugins.add(new Blink());
		plugins.add(new NoKnockback());
		plugins.add(new MoreInventory());
		plugins.add(new NoSlow());
		plugins.add(new Overclock());
	}

	public void onTick(Minecraft client) {
		// Handle Keybind changes.
		for (Plugin plugin : plugins) {
			if (plugin.getKeybind().isPresent() && plugin.getKeybind().get().consumeClick()) {
				try {
					plugin.toggle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (consoleKeybind.consumeClick()) {
			if (Minecraft.getInstance().screen == consoleScreen) {
				Minecraft.getInstance().setScreen(null);
			} else {
				Minecraft.getInstance().setScreen(consoleScreen);
			}
		}

		if (Minecraft.getInstance().screen == null && consoleScreen != null) {
			for (Panel panel : consoleScreen.getPanels()) {
				if (panel.isPinned()) {
					panel.tick();
				}
			}
		}

		lastPacketTime ++;
	}

	public void onPostRender(GuiGraphics guiGraphics, Gui gui) {
		if (consoleScreen != null && Minecraft.getInstance().screen != consoleScreen) {
			for (Panel panel : consoleScreen.getPanels()) {
				if (panel.isPinned()) {
					panel.render(guiGraphics, 0, 0, 0);
				}
			}
		}
	}

	public List<Plugin> getPlugins() {
		return this.plugins;
	}
}
