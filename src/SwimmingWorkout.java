/**
 * Represent an SwimmingWorkout
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
