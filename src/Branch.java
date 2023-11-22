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
    public Employee onlyCourier;
    public Employee onlyCashier;
    public Employee onlyCook;
    public Queue<Employee> cookQueue;
    private HMap<String, Employee> employees;

    public Branch(String city, String district) {
        this.city = city;
        this.district = district;
        this.employees = new HMap<>();
        this.cookQueue = new LinkedList<>();
        this.courierCount = 0;
        this.cashierCount = 0;
        this.cookCount = 0;
    }
    
    public Employee get(String name){
        return employees.get(name);
    }

    public void setEmployeeScore(Employee employee, int score, FileWriter writer) throws Exception {
        if(employee == null){
            writer.write("There is no such employee.\n");
            return;
        }

        employee.score = score;
        employee.promotionPoint += score / 200;
        if(score > 0){
            employee.bonus += score % 200;
            monthlyBonus += score % 200;
            totalBonus += score % 200;
        }
    }

    public void put(Employee employee, FileWriter writer) throws Exception {
        if(employees.containsKey(employee.name)){
            writer.write("Existing employee cannot be added again.\n");
            return;
        }
        
        switch(employee.role){
            case "COURIER":
                employees.put(employee.name, employee);
                courierCount++;
                if(courierCount == 2 && onlyCourier != null && employees.containsKey(onlyCourier.name) && onlyCourier.role.equals("COURIER")) promotion(onlyCourier, writer);
                break;

            case "CASHIER":
                employees.put(employee.name, employee);
                cashierCount++;
                if(cashierCount == 2 && onlyCashier != null && employees.containsKey(onlyCashier.name) && onlyCashier.role.equals("CASHIER")) {
                    promotion(onlyCashier, writer);
                }
                break;

            case "COOK":
                employees.put(employee.name, employee);
                cookCount++;
                if(cookCount == 2 && onlyCook != null && employees.containsKey(onlyCook.name) && onlyCook.role.equals("COOK")) promotion(onlyCook, writer);
                if(manager != null) promotion(manager, writer);
                break;

            case "MANAGER":
                employees.put(employee.name, employee);
                manager = employee;
                break;
        }
    }

    public void remove(Employee employee, FileWriter writer) throws Exception {
        if(employee == null){
            writer.write("There is no such employee.\n");
            return;
        }
        
        switch(employee.role){
            case "COURIER":
                if(courierCount == 1){
                    if(employee.promotionPoint > -5){
                        employee.bonus += 200;
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
                else{
                    writer.write(employee.name + " is leaving from branch: " + district + ".\n");
                    employees.remove(employee.name);
                    courierCount--;
                }
                break;

            case "CASHIER":
                if(cashierCount == 1){
                    if(employee.promotionPoint > -5){
                        employee.bonus += 200;
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
                else{
                    writer.write(employee.name + " is leaving from branch: " + district + ".\n");
                    employees.remove(employee.name);
                    cashierCount--;
                }
                break;

            case "COOK":
                if(cookCount == 1){
                    if(employee.promotionPoint > -5){
                        employee.bonus += 200;
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
                else{
                    writer.write(employee.name + " is leaving from branch: " + district + ".\n");
                    employees.remove(employee.name);
                    cookQueue.remove(employee);
                    cookCount--;
                }
                break;

            case "MANAGER":
                if(cookCount == 1 || cookQueue.size() == 0){
                    if(employee.promotionPoint > -5){
                        employee.bonus += 200;
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
                else{
                    Employee newManager = cookQueue.poll();
                    newManager.promotionPoint -= 10;
                    newManager.role = "MANAGER";
                    cookCount--;
                    writer.write(employee.name + " is leaving from branch: " + district + ".\n");
                    writer.write(newManager.name + " is promoted from Cook to Manager.\n");
                    if(newManager.name.equals("Dilsozi Seker")){
                        System.out.println(newManager.promotionPoint + " " + newManager.role + " " + cookCount + " " + cookQueue.size());
                    }
                    employees.remove(employee.name);
                    manager = newManager;
                }
                break;
        }
    }

    public void promotion(Employee employee, FileWriter writer) throws Exception {
        if(employee == null) return;

        switch(employee.role){
            case "COURIER":
                if(employee.promotionPoint <= -5){
                    if(courierCount == 1){
                        onlyCourier = employee;
                    }
                    else{
                        writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                        employees.remove(employee.name);
                        courierCount--;
                    }
                }
                break;

            case "CASHIER":
                if(employee.promotionPoint <= -5){
                    if(cashierCount == 1){
                        onlyCashier = employee;
                    }
                    else{
                        writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                        employees.remove(employee.name);
                        cashierCount--;
                    }
                }
                if(employee.promotionPoint >= 3){
                    if(cashierCount == 1){
                        onlyCashier = employee;
                    }
                    else{
                        writer.write(employee.name + " is promoted from Cashier to Cook.\n");
                        employee.promotionPoint -= 3;
                        employee.role = "COOK";
                        cashierCount--;
                        cookCount++;
                        promotion(employee, writer);
                        if(cookCount == 2 && onlyCook != null) promotion(onlyCook, writer);
                    }
                }
                break;

            case "COOK":
                if(employee.promotionPoint < 10){
                    cookQueue.remove(employee);
                }
                if(employee.promotionPoint <= -5){
                    if(cookCount == 1){
                        onlyCook = employee;
                    }
                    else{
                        writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                        employees.remove(employee.name);
                        cookQueue.remove(employee);
                        cookCount--;
                    }
                }
                if(employee.promotionPoint >= 10){
                    if(!cookQueue.contains(employee)){
                        cookQueue.add(employee);
                    }
                    promotion(manager, writer);
                }
                break;

            case "MANAGER":
                if(employee.promotionPoint <= -5){
                    if(cookCount > 1 && cookQueue.size() > 0){
                        Employee newManager = cookQueue.poll();
                        writer.write(employee.name + " is dismissed from branch: " + this.district + ".\n");
                        writer.write(newManager.name + " is promoted from Cook to Manager.\n");
                        newManager.promotionPoint -= 10;
                        newManager.role = "MANAGER";
                        cookCount--;
                        employees.remove(employee.name);
                        manager = newManager;
                    }
                }
                break;
        }
    }
}
