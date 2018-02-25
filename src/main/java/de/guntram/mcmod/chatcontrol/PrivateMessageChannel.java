/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.mcmod.chatcontrol;

import java.util.regex.Matcher;

/**
 *
 * @author gbl
 */
public class PrivateMessageChannel extends ChatChannel {
    
    private String channelTarget;

    public PrivateMessageChannel(String name, String cmd, String msg) {
        super(name, cmd, msg);
        channelTarget=null;
    }
    
    @Override
    public String getDescription() { 
        if (channelTarget==null)
            return channelName;
        else
            return channelName + ":" + channelTarget;
    }
    
    @Override
    public boolean isEnterChannelMessage(String message) {
        Matcher matcher=enterChannelPattern.matcher(message);
        boolean isMatch=matcher.matches();
        if (isMatch)
            channelTarget=matcher.group(1);
        return isMatch;
    }
    
    @Override
    public String getSaveString() { return channelName+";;"+enterChannelMessage+";usetext"; }
}
