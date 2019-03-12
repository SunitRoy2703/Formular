package io.github.formular_team.formular.car;

import io.github.formular_team.formular.math.Mth;
import io.github.formular_team.formular.math.Vector2;

public class Car {
    private static final float GRAVITY = 9.8F; // m/s^2

    private static final float DRAG = 5.0F; // factor for air resistance (drag)

    private static final float RESISTANCE = 30.0F; // factor for rolling resistance

    public final CarDefinition definition;

    // position of car center in world coordinates
    public final Vector2 position = new Vector2();

    // velocity vector of car in world coordinates
    private final Vector2 linearVelocity = new Vector2();

    // angle of car body orientation (in rads)
    public float rotation;

    private float angularVelocity;

    public float steerangle;

    public float throttle;

    public float brake;

    public float tireGrip;

    private static float fTireGrip;

    private static float rTireGrip;

    public Vector2 velocity;

    public Car(final CarDefinition type) {
        this.definition = type;
    }

    public void step(final float dt) {
        final float sn = Mth.sin(this.rotation);
        final float cs = Mth.cos(this.rotation);
        // SAE convention: x is to the front of the car, y is to the right, z is down
        // transform velocity in world reference frame to velocity in car reference frame
        velocity = new Vector2(
            cs * this.linearVelocity.getY() + sn * this.linearVelocity.getX(),
            -sn * this.linearVelocity.getY() + cs * this.linearVelocity.getX()
        );

        // Lateral force on wheels
        //
        // Resulting velocity of the wheels as result of the yaw rate of the car body
        // v = yawrate * r where r is distance of wheel to CG (approx. half wheel base)
        // yawrate (ang.velocity) must be in rad/s
        //
        final float yawspeed = this.definition.wheelbase * 0.5F * this.angularVelocity;

        final float rot_angle = Mth.atan2(yawspeed, velocity.getX());
        // Calculate the side slip angle of the car (a.k.a. beta)
        final float sideslip = Mth.atan2(velocity.getY(), velocity.getX());

        // Calculate slip angles for front and rear wheels (a.k.a. alpha)
        final float slipanglefront = sideslip + rot_angle - this.steerangle;
        final float slipanglerear = sideslip - rot_angle;

        // weight per axle = half car mass times 1G (=9.8m/s^2)
        final float weight = this.definition.mass * GRAVITY * 0.5F;

        fTireGrip = tireGrip;
        rTireGrip  = tireGrip;

        // lateral force on front wheels = (Ca * slip angle) capped to friction circle * load
        final Vector2 flatf = new Vector2(0.0F, Mth.clamp(definition.getCaF() * slipanglefront, -fTireGrip, fTireGrip) * weight);

        // lateral force on rear wheels
        final Vector2 flatr = new Vector2(0.0F, Mth.clamp(definition.getCaR() * slipanglerear, -rTireGrip, rTireGrip) * weight);

        // longitudinal force on rear wheels - very simple traction model
        final Vector2 ftraction = new Vector2(100 * (this.throttle - this.brake * Math.signum(velocity.getX())), 0.0F);

        //
        // Forces and torque on body
        //

        // drag and rolling resistance
        final Vector2 resistance = new Vector2(
            -(RESISTANCE * velocity.getX() + DRAG * velocity.getX() * Math.abs(velocity.getX())),
            -(RESISTANCE * velocity.getY() + DRAG * velocity.getY() * Math.abs(velocity.getY()))
        );

        // sum forces
        final Vector2 force = new Vector2(
            ftraction.getX() + Mth.sin(this.steerangle) * flatf.getX() + flatr.getX() + resistance.getX(),
            ftraction.getY() + Mth.cos(this.steerangle) * flatf.getY() + flatr.getY() + resistance.getY()
        );

        final float torque = this.definition.b * flatf.getY() - this.definition.c * flatr.getY();

        final Vector2 linearAcceleration = new Vector2(
            force.getX() / this.definition.mass,
            force.getY() / this.definition.mass
        );
        final Vector2 linearAccelerationWC = new Vector2(
            cs * linearAcceleration.getY() + sn * linearAcceleration.getX(),
            -sn * linearAcceleration.getY() + cs * linearAcceleration.getX()
        );
        this.linearVelocity.add(linearAccelerationWC.multiply(dt));
        final float angularAcceleration = torque / this.definition.inertia;
        this.angularVelocity += dt * angularAcceleration;

        if (this.linearVelocity.length() < 0.5F && this.throttle < 1e-6F) {
            this.linearVelocity.set(0.0F, 0.0F);
            this.angularVelocity = 0.0F;
        }

        this.position.add(this.linearVelocity.clone().multiply(dt));
        this.rotation += dt * this.angularVelocity;
    }

    public Vector2 getLinearVelocity(){
        return linearVelocity;
    }
}
