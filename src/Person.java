/**
 * Represent a Person
 */
public class Person {

    private Long id;

    private String firstName;
    private String lastName;
    private int age;

    // ##################### Konstruktor #########################
    // Dies ist ein Konstruktor für die Java-Klasse "Person".
    // Ein Konstruktor ist eine spezielle Methode, die automatisch aufgerufen wird,
    // wenn ein Objekt einer Klasse erstellt wird.
    // Der Konstruktor initialisiert die Eigenschaften (Variablen) des Objekts mit
    // den angegebenen Werten.
    public Person(Long id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // ##################### Getter & Setter #########################
    // Dieser Java-Code definiert acht Methoden, vier sogenannte "Getter" und vier
    // sogenannte "Setter", für die Java-Klasse "Person".
    // Diese Methoden ermöglichen es, auf die Eigenschaften (Variablen) eines
    // Objekts der Klasse "Person" zuzugreifen und sie zu ändern.
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
