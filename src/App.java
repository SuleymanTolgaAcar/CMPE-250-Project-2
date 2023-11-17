import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File initialFile = new File("small_cases/inputs/initial1.txt");
        Scanner initialScanner = new Scanner(initialFile);
        File operationsFile = new File("small_cases/inputs/input1.txt");
        Scanner operationsScanner = new Scanner(operationsFile);
        FileWriter writer = new FileWriter("small_cases/outputs/output.txt");
        HMap<String, HMap<String,HMap<String, Employee>>> cities = new HMap<>();
        while (initialScanner.hasNextLine()) {
            String[] line = initialScanner.nextLine().split(", ");
            String city = line[0]; String district = line[1]; String name = line[2]; String role = line[3];
            HMap<String, HMap<String, Employee>> districts;
            if(!cities.containsKey(city)){
                districts = new HMap<>();
                cities.put(city, districts);
            }
            else{
                districts = cities.get(city);
            }
            HMap<String, Employee> employees;
            if(!cities.get(city).containsKey(district)){
                employees = new HMap<>();
                cities.get(city).put(district, employees);
            }
            else{
                employees = cities.get(city).get(district);
            }
            Employee employee = new Employee(city, district, name, role);
            employees.put(name, employee);
        }
        System.out.println(cities.get("Diyarbakir").get("Dicle").get("Canela Temel"));
    }
}
