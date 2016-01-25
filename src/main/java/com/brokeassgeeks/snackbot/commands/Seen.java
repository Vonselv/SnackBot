package com.brokeassgeeks.snackbot.commands;

import com.brokeassgeeks.snackbot.SnackBot;
import com.brokeassgeeks.snackbot.Seen.UserDB;
import com.brokeassgeeks.snackbot.Utils.Utils;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.Date;

public class Seen extends Command{

    public Seen(GenericMessageEvent event, String[] args) {
        super(event, args);
    }
    @Override
    public void init() {
        triggers.add("seen");
    }

    @Override
    public void run() {
        if (!(event instanceof MessageEvent))
            return;
        String out;
        User user = event.getUser();
        String sender = user.getNick();

        if (args.length == 1) {
            super.respond(String.format("<B><b>USAGE:<N> %s <USER>" ,args[0]));
            return;
        }

        String target = args[1];

        if (sender.equalsIgnoreCase(target)) {
            super.respond(String.format("<B><b>[%s]<N> you need too look in the mirror.", sender));
            return;
        }

        if (target.equalsIgnoreCase(event.getBot().getNick())) {
            super.respond(String.format("<B><b>[%s]<N> of course I am here.", sender));
            return;
        }

        if (((MessageEvent) event).getChannel().getUsersNicks().contains(target)) {
            User fetchUser = event.getBot().getUserChannelDao().getUser(target);

            if(SnackBot.getSeenDataBase().isUserInDB(fetchUser.getHostname()) == 0)
                SnackBot.getSeenDataBase().processUserSeenRecord(fetchUser,System.currentTimeMillis());

            super.respond( String.format("<B><b>%s<N> is right here!", args));
            return;
        }

        UserDB userDB = SnackBot.getSeenDataBase().getUserbyNick(target);

        if (userDB != null) {
            if (((MessageEvent) event).getChannel().getUsersNicks().contains(userDB.getLastNick())) {
                super.respond(String.format("<B><b>%s<N> is right here! Disguised as <B><b>%s<N>", target, userDB.getLastNick()));
            } else {
                Date currentDate = new Date(userDB.getTimeSeen());
                super.respond(String.format("I last saw <B><b>%s<N> in %s on: <B><b>%s<N>",target,((MessageEvent) event).getChannel().getName(), Utils.getTime(currentDate)));
            }
        } else {
            super.respond(String.format("I have not seen <B><b>%s<N>",target));

        }
    }



}
