/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.mcmod.chatcontrol;

import java.util.regex.Pattern;

/**
 *
 * @author gbl
 */
public class ChatChannel {
    
    String channelName;
    String enterChannelCommand;
    String enterChannelMessage;
    Pattern enterChannelPattern;
    
    private int startx, endx, starty, endy;
    
    public ChatChannel(String name, String cmd, String msg) {
        channelName=name;
        enterChannelCommand=cmd;
        enterChannelMessage=msg;
        enterChannelPattern=Pattern.compile(msg);
        startx=endx=-1;
    }
    
    public String getName() { return channelName; }
    public String getCommand() { return enterChannelCommand; }
    public String getMessage() { return enterChannelMessage; }
    public String getDescription() { return channelName; }
    public String getSaveString() { return channelName+";"+enterChannelCommand+";"+enterChannelMessage; }
    
    public void setScreenPosition(int x1, int x2, int y1, int y2) {
        startx=x1;
        endx=x2;
        starty=y1;
        endy=y2;
    }
    
    public boolean isAtScreenPosition(int x, int y) {
        return (x>startx && x<=endx && y>=starty && y<=endy);
    }

    public boolean isEnterChannelMessage(String message) {
        return enterChannelPattern.matcher(message).matches();
    }
}
