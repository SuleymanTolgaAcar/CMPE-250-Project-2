public class Employee {
    public String name;
    public String city;
    public String district;
    public String role;
    public int score;
    public int bonus;
    public int promotionPoint;

    public Employee(String city, String district, String name, String role) {
        this.name = name;
        this.city = city;
        this.district = district;
        this.role = role;
    }

    public String toString(){
        return this.name + " " + this.city + " " + this.district + " " + this.role;
    }
}
