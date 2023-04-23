/**
 * ##################### Swimming Workout Klasse #########################
 * Diese Klasse erweitert die abstrakte Klasse "Workout"
 * und definiert eine spezifische Art von Workout namens "SwimmingWorkout",
 * die eine Schwimmaktivität mit einer bestimmten Art von Schwimmen ("type")
 * enthält.
 */

public class SwimmingWorkout extends Workout {

    private SwimmingType type;

    public SwimmingWorkout(Long id, int duration, int distance, Long personId, SwimmingType type) {
        super(id, duration, distance, personId);
        this.type = type;
    }

    public SwimmingType getType() {
        return type;
    }

    public void setType(SwimmingType type) {
        this.type = type;
    }
}
