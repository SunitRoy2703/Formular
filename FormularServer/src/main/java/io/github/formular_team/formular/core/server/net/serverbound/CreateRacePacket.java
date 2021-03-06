package io.github.formular_team.formular.core.server.net.serverbound;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.function.Function;

import io.github.formular_team.formular.core.course.Course;
import io.github.formular_team.formular.core.Driver;
import io.github.formular_team.formular.core.game.GameModel;
import io.github.formular_team.formular.core.kart.KartModel;
import io.github.formular_team.formular.core.SimpleDriver;
import io.github.formular_team.formular.core.race.Race;
import io.github.formular_team.formular.core.race.RaceConfiguration;
import io.github.formular_team.formular.core.server.net.ByteBuffers;
import io.github.formular_team.formular.core.server.net.Connection;
import io.github.formular_team.formular.core.server.net.Context;
import io.github.formular_team.formular.core.server.net.KartContext;
import io.github.formular_team.formular.core.server.net.Packet;
import io.github.formular_team.formular.core.server.net.PacketHandler;
import io.github.formular_team.formular.core.server.net.UserContext;

public class CreateRacePacket implements Packet {
    public static final Function<ByteBuffer, CreateRacePacket> CREATOR = CreateRacePacket::new;

    private final RaceConfiguration configuration;

    private final Course course;

    public CreateRacePacket(final RaceConfiguration configuration, final Course course) {
        this.configuration = Objects.requireNonNull(configuration);
        this.course = Objects.requireNonNull(course);
    }

    public CreateRacePacket(final ByteBuffer buf) {
        this.configuration = ByteBuffers.getRaceConfiguration(buf);
        this.course = ByteBuffers.getCourse(buf);
    }

    @Override
    public Function<ByteBuffer, ? extends Packet> creator() {
        return CREATOR;
    }

    @Override
    public void write(final ByteBuffer buf) {
        ByteBuffers.putRaceConfiguration(buf, this.configuration);
        ByteBuffers.putCourse(buf, this.course);
    }

    public static class Handler implements PacketHandler<UserContext, Context, CreateRacePacket> {
        @Override
        public Context apply(final UserContext context, final CreateRacePacket packet) {
            final GameModel game = context.getServer().getGame();
            final Race race = game.createRace(context.getUser(), packet.configuration, packet.course);
            final KartModel kart = race.createKart(context.getUser());
            kart.setColor(context.getUser().getColor());
            game.addKart(kart);
            final Driver userDriver = SimpleDriver.create(context.getUser(), kart);
            game.addDriver(userDriver);
            final Connection conn = context.getRemote();
            // TODO: racer specific listeners
            race.addListener(new DriverRaceListener(conn, userDriver));
            race.add(userDriver);
            return new KartContext(context, kart);
        }
    }
}
