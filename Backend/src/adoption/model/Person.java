package adoption.model;

public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String mobileNumber;
    private final int age;
    private final String address;

    protected Person(String firstName, String lastName, String email, String mobileNumber, int age, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.age = age;
        this.address = address;
    }

    public String getFirstName()    { return firstName; }
    public String getLastName()     { return lastName; }
    public String getEmail()        { return email; }
    public String getMobileNumber() { return mobileNumber; }
    public int getAge() { return age; }
    public String getAddress()      { return address; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return getFullName() + " (" + getRole() + ")";
    }
}