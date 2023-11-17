public class Employee {
    public String name;
    public String city;
    public String district;
    public String role;
    private int score;
    public int bonus;
    public int promotionPoint;

    public Employee(String city, String district, String name, String role) {
        this.name = name;
        this.city = city;
        this.district = district;
        this.role = role;
    }

    public void setScore(int score){
        this.score = score;
        this.promotionPoint += score / 200;
        if(score > 0){
            this.bonus = score % 200;
        }
    }

    public int getScore(){
        return this.score;
    }

    public String toString(){
        return this.name + " " + this.city + " " + this.district + " " + this.role;
    }
}
