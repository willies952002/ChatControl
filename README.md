# ChatControl
Does your server have different chat channels? A main chat, a party chat, an admin chat, and you keep sending stuff to the wrong one because you don't remember which one you switched to 5 minutes ago?

 

Then this mod may be for you.

 

It detects channel switch messages sent to you from the server, remembers the last one, and shows the corresponding channel in the bottom left of the screen, just where the chat input box is. And as soon as you open chat, you'll get a list of channels, with the one you're currently in highlighted. If you notice you're about to post to the wrong one, just switch channels by clicking the one you wanted!

 

Please note: this mod does *not* add chat channels to your multiplayer world. It's intended to improve your control over existing channels your server gives to you.

 

Configuration

 

If you're playing on Empire Minecraft (emc.gs), the mod will work for you right out of the box. If you're playing somewhere else where chat commands and responses are different, you'll need to change the configuration to match your server. For each chat channel, you need to know the command to enter it, and the message the server sends you when you enter.

 

Create a file named chatcontrol-channels.txt in your config folder. If you don't, the mod will create it itself the first time you run it. Then, configure your channels like by line, like this:

 

§2Community;/chat c;Chat focus set to channel Community

 

Each line consists of three columns, separated by semicolons (;) :

    the channel name you want to display - this can include formatting codes, in the example, "Community" will be shown in green
    the command the server needs to enter this chat channel
    the string the server sends back when you enter that channel. No formatting codes here, even if the server sends them!

You can omit the second column if there's no specific command to enter that channel, but that means you won't have the clickable button.

 

If you have message channels to PM to people (not using /msg or /r, instead, temporarily sending all chat to someone), you can include their name in the channel name. For example, EMC sends "Started private conversation with DavisAtDavis" when I enter a conversation with him. To grab his name, I use "Started private conversation with (.*)" in the third column, and add a fourth one that has just the text string "usetext" in it. This will grab the name from the server message, and append it to the channel name.
