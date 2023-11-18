import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;

public class Branch {
    public String city;
    public String district;
    public int courierCount;
    public int cashierCount;
    public int cookCount;
    public int monthlyBonus;
    public int totalBonus;
    public Employee manager;
    public Queue<Employee> cookQueue;
    public Employee cashierToPromote;
    private HMap<String, Employee> employees;

    public Branch(String city, String district) {
        this.city = city;
        this.district = district;
        this.employees = new HMap<>();
        this.cookQueue = new LinkedList<>();
    }

    public void put(String name, Employee employee, FileWriter writer) throws Exception {
        employees.put(name, employee);
        switch (employee.role){
            case "COURIER":
                courierCount++;
                break;
            case "CASHIER":
                if(cashierToPromote != null){
                    writer.write(cashierToPromote.name + " is promoted from Cashier to Cook.\n");
                    cashierToPromote.role = "COOK";
                    cashierToPromote.promotionPoint -= 3;
                    cashierCount--;
                    cookCount++;
                    cashierToPromote = null;
                }
                cashierCount++;
                break;
            case "COOK":
                cookCount++;
                break;
            case "MANAGER":
                manager = employee;
                break;
        }
    }

    public void remove(Employee employee, FileWriter writer) throws Exception {
        employee.bonus += 200;
        monthlyBonus += 200;
        totalBonus += 200;

        switch (employee.role){
            case "COURIER":
                if(courierCount == 1) return;
                courierCount--;
                break;

            case "CASHIER":
                if(cashierCount == 1) return;
                cashierCount--;
                break;

            case "COOK":
                if(cookCount == 1) return;
                employee.role = "LEFT";
                cookCount--;
                break;

            case "MANAGER":
                if(cookCount > 1 && cookQueue.size() > 0){
                    Employee cookToPromote = cookQueue.poll();
                    while((cookToPromote.promotionPoint < 10 || !cookToPromote.role.equals("COOK")) && cookQueue.size() > 0){
                        cookToPromote = cookQueue.poll();
                    }
                    if(cookToPromote.promotionPoint < 10 || !cookToPromote.role.equals("COOK")) return;
                    cookToPromote.role = "MANAGER";
                    cookToPromote.promotionPoint -= 10;
                    manager = cookToPromote;
                    cookCount--;
                }
                else return;
                break;
        }
            
        employee.bonus -= 200;
        monthlyBonus -= 200;
        totalBonus -= 200;
        
        writer.write(employee.name + " is leaving from branch: " + district + ".\n");
        if(employee.role.equals("MANAGER") && !manager.name.equals(employee.name)){
            writer.write(manager.name + " is promoted from Cook to Manager.\n");
        }
        employees.remove(employee.name);
    }

    public void setEmployeeScore(Employee employee, int score){
        employee.score = score;
        employee.promotionPoint += score / 200;
        if(score > 0){
            employee.bonus += score % 200;
            monthlyBonus += score % 200;
            totalBonus += score % 200;
        }
    }

    public Employee get(String name){
        return employees.get(name);
    }

    public void promotion(Employee employee, FileWriter writer) throws Exception {
        switch(employee.role){
            case "CASHIER":
                if(employee.promotionPoint >= 3 && cashierCount == 1){
                    cashierToPromote = employee;
                }
                else if(employee.promotionPoint >= 3 && cashierCount > 1){
                    writer.write(employee.name + " is promoted from Cashier to Cook.\n");
                    employee.role = "COOK";
                    employee.promotionPoint -= 3;
                    cashierCount--;
                    cookCount++;
                }
                else if(employee.promotionPoint <= -5 && cashierCount > 1){
                    writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                    employees.remove(employee.name);
                    cashierCount--;
                }
                break;

            case "COOK":
                if(employee.promotionPoint >= 10){
                    cookQueue.add(employee);
                }
                else if(employee.promotionPoint <= -5 && cookCount > 1){
                    writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                    employee.role = "LEFT";
                    employees.remove(employee.name);
                    cookCount--;
                }
                break;

            case "MANAGER":
                if(employee.promotionPoint <= -5 && !cookQueue.isEmpty() && cookCount > 1){
                    writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                    Employee cookToPromote = cookQueue.poll();
                    while(cookToPromote.promotionPoint < 10 || !cookToPromote.role.equals("COOK")){
                        cookToPromote = cookQueue.poll();
                    }
                    if(cookToPromote.promotionPoint < 10 || !cookToPromote.role.equals("COOK")) return;
                    writer.write(cookToPromote.name + " is promoted from Cook to Manager.\n");
                    cookToPromote.role = "MANAGER";
                    cookToPromote.promotionPoint -= 10;
                    manager = cookToPromote;
                    cookCount--;
                    employees.remove(employee.name);
                }
                break;
                
            case "COURIER":
                if(employee.promotionPoint <= -5 && courierCount > 1){
                    writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                    employees.remove(employee.name);
                    courierCount--;
                }
                break;
        }
    }

}