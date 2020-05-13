package com.simple.castle.core.settings.dto;

import com.simple.castle.core.settings.dto.base.PlayerJson;

import java.util.List;

public class PlayersJson {
    private List<PlayerJson> players;

    public List<PlayerJson> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerJson> players) {
        this.players = players;
    }
}
