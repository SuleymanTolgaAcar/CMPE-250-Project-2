import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File initialFile = new File("small_cases/inputs/initial4.txt");
        Scanner initialScanner = new Scanner(initialFile);
        File operationsFile = new File("small_cases/inputs/input4.txt");
        Scanner operationsScanner = new Scanner(operationsFile);
        FileWriter writer = new FileWriter("small_cases/outputs/output.txt");
        HMap<String, Branch> branches = new HMap<>();

        while (initialScanner.hasNextLine()) {
            String[] line = initialScanner.nextLine().split(", ");
            String city = line[0]; String district = line[1]; String name = line[2]; String role = line[3];

            Branch branch;
            if(!branches.containsKey(city + "-" + district)){
                branch = new Branch(city, district);
                branches.put(city + "-" + district, branch);
            }
            else{
                branch = branches.get(city + "-" + district);
            }

            Employee employee = new Employee(city, district, name, role);
            branch.put(name, employee, writer);
        }
        
        while(operationsScanner.hasNextLine()){
            String[] line = operationsScanner.nextLine().split(": ");

            if(line.length <= 1){
                for(Branch branch: branches.valueList()){
                    branch.monthlyBonus = 0;
                }
                continue;
            }

            String operation = line[0];
            String[] params = line[1].split(", ");
            String city = params[0]; String district = params[1];
            Branch branch = branches.get(city + "-" + district);
            Employee employee;
            
            switch(operation){
                case "ADD":
                    employee = new Employee(city, district, params[2], params[3]);
                    branch.put(params[2], employee, writer);
                    break;

                case "LEAVE":
                    employee = branch.get(params[2]);
                    if(employee == null){
                        writer.write("There is no such employee.\n");
                        break;
                    }
                    branch.remove(employee, writer);
                    break;

                case "PERFORMANCE_UPDATE":
                    employee = branch.get(params[2]);
                    if(employee == null){
                        writer.write("There is no such employee.\n");
                        break;
                    }
                    branch.setEmployeeScore(employee, Integer.parseInt(params[3]));
                    branch.promotion(employee, writer);
                    break;

                case "PRINT_MONTHLY_BONUSES":
                    writer.write("Total bonuses for the " + branch.district + " branch this month are: " + branch.monthlyBonus + "\n");
                    break;

                case "PRINT_OVERALL_BONUSES":
                    writer.write("Total bonuses for the " + branch.district + " branch are: " + branch.totalBonus + "\n");
                    break;

                case "PRINT_MANAGER":
                    writer.write("Manager of the " + branch.district + " branch is " + branch.manager.name + ".\n");
                    break;
            }
        }

        initialScanner.close();
        operationsScanner.close();
        writer.close();
    }
}
