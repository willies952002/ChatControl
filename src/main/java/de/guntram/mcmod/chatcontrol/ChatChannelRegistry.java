/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.mcmod.chatcontrol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 *
 * @author gbl
 */
public class ChatChannelRegistry {
    
    private static Set<ChatChannel> channels;
    private static ChatChannel current;
    
    static {
        channels=getNewChannelSet();
        
        channels.add(current=
                     new ChatChannel("Community",  "/chat c", "Chat focus set to channel Community"));
        channels.add(new ChatChannel("Market",     "/chat m", "Chat focus set to channel Market"));
        channels.add(new ChatChannel("Local",      "/chat l", "Chat focus set to channel Local"));
        channels.add(new ChatChannel("Residence",  "/chat r", "Chat focus set to channel Residence"));
        channels.add(new ChatChannel("Group",      "/chat g", "Chat focus set to channel Group"));
        channels.add(new ChatChannel("Supporter",  "/chat s", "Chat focus set to channel Supporter"));
        channels.add(new PrivateMessageChannel("Private Message", null, "Started private conversation with (.*)"));
    }
    
    static boolean parseChatMessage(String message) {
        for (ChatChannel check: channels) {
            if (check.isEnterChannelMessage(message)) {
                System.out.println("found pattern for "+check.getDescription());
                current=check;
                return true;
            }
        }
        return false;
    }
    
    static ChatChannel getCurrentChannel() {
        return current;
    }
    
    static Set<ChatChannel> getAllChannels() {
        return channels;
    }
    
    static void readConfigFile(String configFileName) {
        File channelFile= new File(configFileName);
        try (BufferedReader reader=new BufferedReader(new FileReader(channelFile))) {
            String separator=";";
            String line;
            SortedSet<ChatChannel> newChannels = getNewChannelSet();
            while ((line=reader.readLine())!=null) {
                if (line.startsWith("separator=")) {
                    separator=line.substring(10);
                    continue;
                }
                String name, command, chattext;
                boolean isPMC;
                String[] parts=line.split(separator);
                if (parts.length<3)
                    continue;
                name=parts[0];
                command=parts[1];
                chattext=parts[2];
                if (parts.length>3 && parts[3].equalsIgnoreCase("usetext"))
                    newChannels.add(new PrivateMessageChannel(name, command, chattext));
                else
                    newChannels.add(new ChatChannel(name, command, chattext));
            }
            channels=newChannels;
            current=newChannels.first();
        } catch (FileNotFoundException ex) {
            try (FileWriter writer=new FileWriter(channelFile)) {
                for (ChatChannel channel: channels) {
                    writer.append(channel.getSaveString());
                    writer.append("\r\n");
                }
            } catch (IOException ex1) {
                System.out.println("error writing example file "+channelFile.getAbsolutePath()+" "+ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("error reading "+channelFile.getAbsolutePath()+" "+ex.getMessage());
        }
    }
    
    static TreeSet<ChatChannel> getNewChannelSet() {
        return new TreeSet<>(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((ChatChannel)o1).channelName.compareTo(((ChatChannel)o2).channelName);
                }
            });
    }
    
    static ChatChannel getFromScreenXY(int x, int y) {
        for  (ChatChannel channel: channels) {
            if (channel.isAtScreenPosition(x, y))
                return channel;
        }
        return null;
    }
}
