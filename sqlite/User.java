package sqlite;

public class User {
    int id;
    String name;
    String skills;
    Double salary;
    int age;

    public User(int id, String name, int age, String skills, Double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.skills = skills;
        this.salary = salary;
    }

    public User(String name, int age, String skills, Double salary) {
        this.name = name;
        this.age = age;
        this.skills = skills;
        this.salary = salary;
    }

    public User(String name) {
        this.name = name;
        this.age = 0;
        this.skills = "";
        this.salary = 0.0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skills='" + skills + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }

    public boolean isValid() {
        return !(this.name == null && this.id == 0);
    }

    public String getName() {
        return name;
    }

    public String getSkills() {
        return skills;
    }

    public Double getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean equals(User user) {
        return this.name.equals(user.name) && this.age == user.age && this.skills.equals(user.skills) && this.salary.equals(user.salary);
    }

    public void update(User user) {
        this.name = user.name;
        this.age = user.age;
        this.skills = user.skills;
        this.salary = user.salary;
    }
}
