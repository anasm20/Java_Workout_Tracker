/**
 * ##################### Abstrakte Workout Klasse #########################
 * Dies ist eine abstrakte Java-Klasse namens "Workout".
 * Die Klasse enthält vier private Instanzvariablen: "id" (vom Typ "Long"),
 * "duration" und "distance" (beide vom Typ "int") und "personId" (vom Typ
 * "Long").
 */

public abstract class Workout {

    private Long id;
    private int duration;
    private int distance;
    private Long personId;

    public Workout(Long id, int duration, int distance, Long personId) {
        this.id = id;
        this.duration = duration;
        this.distance = distance;
        this.personId = personId;
    }

    public Long getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
