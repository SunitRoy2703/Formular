package io.github.formular_team.formular.state;

import io.github.formular_team.formular.server.Race;

public interface InTournamentState extends GameState {
    interface State {}

    interface RaceState extends State {
        Race race();
    }

    interface ReadCourseState extends State {
    }
}
