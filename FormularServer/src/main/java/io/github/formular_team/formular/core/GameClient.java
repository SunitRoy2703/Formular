package io.github.formular_team.formular.core;

public interface GameClient extends Game {
    RacerStatus getStatus();

    void addKart(final Kart kart);

    Kart removeKart(final int uniqueId);

    Kart getKart(final int uniqueId);
}
