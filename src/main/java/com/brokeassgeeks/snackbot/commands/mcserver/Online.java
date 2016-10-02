package com.brokeassgeeks.snackbot.commands.mcserver;

import com.brokeassgeeks.snackbot.SnackBot;
import com.brokeassgeeks.snackbot.Utils.Utils;
import com.brokeassgeeks.snackbot.commands.Command;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;

public class Online extends Command {
    public Online(GenericMessageEvent event, String[] args) {
        super(event, args);
    }

    @Override
    public void run() {
        if (!(event instanceof MessageEvent))
            return;

        String out;
        String serverPlayers;

        for (MinecraftServer s : SnackBot.getServers()) {
            serverPlayers ="";

            StatusResponse response = null;

            try {
                response = new ServerConnection(s).getResponse();
            }
            catch (IOException e) {  }
            catch (InvalidResponseException e) { }

            if (response != null) {
                out = String.format("<B><b>%s<N> - %s %s: ", s.getName(), s.getPack(), s.getVersion());
                out += String.format("<B><b>Num Mods:<N> %s ", response.modCount());

               if (response.getOnlinePlayers() > 0) {
                    for (Player player : response.getOnlinePlayersName()) {
                        serverPlayers += String.format("%s ",player.getName());
                    }
                }
                if (!Utils.isEmpty(serverPlayers)) {
                    out += String.format("<B><b>Users Online: <N><g>%s<N>", serverPlayers);
                }

            } else {
                out = String.format("<B><r>%s is down!<N>", s.getName());
            }


            super.respond(out);

        }

    }
}
