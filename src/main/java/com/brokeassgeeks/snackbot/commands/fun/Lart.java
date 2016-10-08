package com.brokeassgeeks.snackbot.commands.fun;

import com.brokeassgeeks.snackbot.Utils.Utils;
import com.brokeassgeeks.snackbot.commands.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.File;
import java.io.FileNotFoundException;

public class Lart extends Command{

    public Lart(GenericMessageEvent event, String[] args) {
        super(event, args);
    }
    public Lart(MessageReceivedEvent event, String[] args) {
        super(event, args);
    }

    @Override
    public void run() {
        if (isFromDiscord()) {
            return;
        }

        String target = sender;
        String s="";

        try {
            s = Utils.chooseRandomLine(new File("data/fun/lart.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (args.length > 1) {
            if (ircEvent.getBot().getUserChannelDao().containsUser(args[1]))
                target = args[1];
            else {
                super.respond(String.format("<B><b>%s<N> is not even here", args[1]));
                return;
            }
        }

        if (target.equalsIgnoreCase(ircEvent.getBot().getNick()))
            target = sender;

        ((MessageEvent)ircEvent).getChannel().send().action(Utils.replaceTags(replaceNick(s,target)));
    }
    public static String replaceNick(String string,String target) {

        return  string.replaceAll("<user>","<B><b>"+target+"<N>");

    }

    }
