package org.kitteh.vanish.listeners;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.destroystokyo.paper.event.server.GS4QueryEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.kitteh.vanish.VanishManager;

public final class ListenServerPing implements Listener {
    private final VanishManager manager;

    public ListenServerPing(VanishManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void ping(ServerListPingEvent event) {
        if (!(event instanceof Iterable)) {
            return; // Pre-API server
        }
        final Set<String> invisibles = this.manager.getVanishedPlayers();
        final Iterator<Player> players;
        try {
            players = event.iterator();
        } catch (final UnsupportedOperationException e) {
            return;
            // NOOP
        }
        Player player;
        while (players.hasNext()) {
            player = players.next();
            if (invisibles.contains(player.getName())) {
                players.remove();
            }
        }
    }

    @EventHandler
    public void onGS4Query(GS4QueryEvent event) {
        if (event.getQueryType() == GS4QueryEvent.QueryType.FULL) {
            GS4QueryEvent.QueryResponse response = event.getResponse();
            Collection<String> playerList = response.getPlayers();
            playerList.removeAll(manager.getVanishedPlayers());
        }
    }

}
