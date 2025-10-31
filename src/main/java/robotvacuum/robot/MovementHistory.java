package robotvacuum.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovementHistory {

    private final List<Movement> movements = new ArrayList<>();

    public void addMovement(Movement m) {
        movements.add(m);
    }

    public List<Movement> getMovements() {
        return Collections.unmodifiableList(movements);
    }
}
