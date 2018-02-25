package de.guntram.mcmod.chatcontrol;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ChatControl.MODID, 
        version = ChatControl.VERSION,
	clientSideOnly = true, 
	guiFactory = "de.guntram.mcmod.chatcontrol.GuiFactory",
	acceptedMinecraftVersions = "[1.12]"
)

public class ChatControl
{
    static final String MODID="chatcontrol";
    static final String VERSION="1.0";

    private boolean turnedOn=true;
    private boolean didDebug=false;
    private Minecraft mc;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        mc=Minecraft.getMinecraft();
        ConfigurationHandler confHandler = ConfigurationHandler.getInstance();
        File configFile;
        confHandler.load(configFile=event.getSuggestedConfigurationFile());
        String channelFileName=configFile.getAbsolutePath();
        channelFileName=channelFileName.substring(0, channelFileName.length()-4)+"-channels.txt";
        ChatChannelRegistry.readConfigFile(channelFileName);
        MinecraftForge.EVENT_BUS.register(confHandler);
    }
    

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (!turnedOn
        ||  event.isCanceled()
        ||  event.getType()!=RenderGameOverlayEvent.ElementType.CHAT)
            return;
        
        GuiNewChat chat = mc.ingameGUI.getChatGUI();
        
        int x=0;
        int y=new ScaledResolution(mc).getScaledHeight()-8;

        float scale = chat.getChatScale();
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F); 
        GlStateManager.enableBlend();
        ChatChannel currentChannel=ChatChannelRegistry.getCurrentChannel();
        if (chat.getChatOpen()) {
            x+=3;
            y-=18;
            if (!didDebug) {
                System.out.println("DEBUG CHATCONTROL render at y= "+y);
                didDebug=true;
            }
            for (ChatChannel channel:ChatChannelRegistry.getAllChannels()) {
                if ((channel.getCommand() != null && !channel.getCommand().isEmpty()) || channel==currentChannel) {
                    int size=mc.fontRenderer.getStringWidth(channel.getDescription());
                    if (channel == currentChannel) {
                        GuiIngame.drawRect(x, y-6, x+size+6, y+8, 0xffff00ff);
                        GuiIngame.drawRect(x+1, y-5, x+size+5, y+7, 0xff000000);
                    } else {
                        GuiIngame.drawRect(x, y-6, x+size+6, y+8, 0xff000000);
                    }
                    mc.fontRenderer.drawString(channel.getDescription(), x+3, y-2, channel == currentChannel ? 0x80ff80 : 0xffffff);
                    channel.setScreenPosition(x, x+size+8, y-6, y+8);
                    x+=size+8;
                } else {
                    channel.setScreenPosition(-1, -1, -1, -1);
                }
            }
        } else {
            mc.fontRenderer.drawString(currentChannel.getDescription(), x, y, 0xffffff);
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onChatMessage(final ClientChatReceivedEvent event) {
        String message=event.getMessage().getUnformattedText();
        boolean foundChat=ChatChannelRegistry.parseChatMessage(message);
        if (foundChat) {
            message=event.getMessage().getFormattedText();
            System.out.println("Switched chat channel to '"+ChatChannelRegistry.getCurrentChannel().getDescription()+ "' because of : "+message);
        }
    }
    
    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiChat) {
            GuiChat guiChat=(GuiChat)(event.getGui());
            // need reflection to get current text from guiChat 
            ExtendedGuiChat egc = new ExtendedGuiChat("");
            event.setGui(egc);
        }
    }
}
