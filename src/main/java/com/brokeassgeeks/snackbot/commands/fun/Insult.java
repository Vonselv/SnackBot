package com.brokeassgeeks.snackbot.commands.fun;

import com.brokeassgeeks.snackbot.commands.Command;
import com.google.gson.Gson;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import java.util.Random;

public class Insult extends Command {
    private Insults insults;

    private class Insults {
        String[] adj;
        String[] amt;
        String[] noun;
    }

    public Insult(GenericMessageEvent event, String[] args) {
        super(event, args);
        load();
    }

    public Insult(MessageReceivedEvent event, String[] args) {
        super(event, args);
        load();
    }
    @Override
    public void run() {

        String message =  "<B><b>%s<N> you, %s";
        String out = String.format(message, sender, getRandomInsult());

        if (args.length == 1) {
            super.respond(out);
            return;
        }

        super.respond(out);
    }

    public void load() {

        try {
            Gson gson = new Gson();
            File file = new File("data/insults.json");

            Reader jsonFile = new FileReader(file);

            this.insults= gson.fromJson(jsonFile, Insults.class);

            jsonFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public String getRandomInsult() {
        Random r = new Random();

        String adj1 = insults.adj[r.nextInt(insults.adj.length)];
        String adj2 = adj1;
        while(adj1.equals(adj2)) {
            adj2 = insults.adj[r.nextInt(insults.adj.length)];
        }

        String amt = insults.amt[r.nextInt(insults.amt.length)];
        String noun = insults.noun[r.nextInt(insults.noun.length)];

        return String.format("%s %s of %s %s", adj1, amt,adj2,noun);
    }
}
