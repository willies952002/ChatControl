/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.mcmod.chatcontrol;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

/**
 *
 * @author gbl
 */
public class ExtendedGuiChat extends GuiChat {
    ExtendedGuiChat(String string) {
        super(string);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        ChatChannel channel;
        if ((channel=ChatChannelRegistry.getFromScreenXY(mouseX, mouseY))!=null
        && channel.getCommand()!=null
        && !(channel.getCommand().isEmpty())) {
            Minecraft.getMinecraft().player.sendChatMessage(channel.getCommand());
            System.out.println("ext gui chat click "+mouseX + "/" + mouseY + "/" + mouseButton+" " +channel.getCommand());
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
