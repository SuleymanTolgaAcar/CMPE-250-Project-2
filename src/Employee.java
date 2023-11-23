/**
 * Employee class for the Branch class.
 * Has data fields for name, city, district, role and promotion point.
 */
public class Employee {
    public String name;
    public String city;
    public String district;
    public String role;
    public int promotionPoint;

    public Employee(String city, String district, String name, String role) {
        this.name = name;
        this.city = city;
        this.district = district;
        this.role = role;
    }
}
