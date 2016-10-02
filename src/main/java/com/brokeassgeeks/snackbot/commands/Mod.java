package com.brokeassgeeks.snackbot.commands;

import com.brokeassgeeks.snackbot.SnackBot;
import com.brokeassgeeks.snackbot.Utils.MinecraftServerUtils;
import com.brokeassgeeks.snackbot.commands.mcserver.*;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;

public class Mod extends Command {


    public Mod(GenericMessageEvent event, String[] args) {
        super(event, args);
    }

    @Override
    public void run() {
        if (args.length <= 2) {
            super.respond(String.format("<B><b>USAGE:<N> %s <SERVER> <MOD>" , args[0]));
            return;
        }

        MinecraftServer s = MinecraftServerUtils.getServerbyName(SnackBot.getServers(),args[1]);

        if (s == null) {
            super.respond(String.format("<B><b>Invalid server:<N> %s" , args[1]));
            return;
        }

        StatusResponse response = null;
        try {
            response = new ServerConnection(s).getResponse();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response == null) {
            super.respond(String.format("<B><r>%s is down!<N>", s.getName()));
            return;
        }

        String out;
        Mods.ModInfo mod = response.getMod(args[2]);

        if (mod == null) {
           out = String.format("<B><b>%s<N> is not using <B><b>%s<N>" , s.getName(),args[2]);
        } else {
            out = String.format("<B><b>%s<N> is using version <B><b>%s<N> of <B><b>%s<N>" , s.getName(),mod.getVersion(),mod.getModid());
        }


        super.respond(out);
    }

}
