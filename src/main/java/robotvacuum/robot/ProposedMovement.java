package robotvacuum.robot;

public class ProposedMovement {
    private final Movement mov;

    public ProposedMovement(Movement mov) {
        this.mov = mov;
    }

    public Movement getMov() {
        return mov;
    }
}
