package my_app.model;

public class StudentDTO {
    private int id;
    private String name;
    private String surname; 
    private String createtime;
    private String birthdate;   
    private String hometown;
    public StudentDTO() {
    }
    public StudentDTO(int id, String name, String surname, String createtime, String birthdate,
            String hometown) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.createtime = createtime;
        this.birthdate = birthdate;
        this.hometown = hometown;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getCreatetime() {
        return createtime;
    }
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public String getHometown() {
        return hometown;
    }
    public void setHometown(String hometown) {
        this.hometown = hometown;
    }
    

}
