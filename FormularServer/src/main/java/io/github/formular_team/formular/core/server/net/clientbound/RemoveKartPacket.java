package io.github.formular_team.formular.core.server.net.clientbound;

import java.nio.ByteBuffer;

import io.github.formular_team.formular.core.GameClient;
import io.github.formular_team.formular.core.Kart;
import io.github.formular_team.formular.core.server.net.Packet;
import io.github.formular_team.formular.core.server.net.PacketHandler;

public class RemoveKartPacket implements Packet {
    private Kart kart;

    public RemoveKartPacket() {

    }

    @Override
    public void write(final ByteBuffer buf) {

    }

    @Override
    public void read(final ByteBuffer buf) {

    }

    public static class Handler implements PacketHandler<GameClient> {
        @Override
        public void handle(final GameClient game) {

        }
    }
}
