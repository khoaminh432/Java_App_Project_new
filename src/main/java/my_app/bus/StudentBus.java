package my_app.bus;
import java.util.ArrayList;

import my_app.dao.StudentDao;
import my_app.model.StudentDTO;
public class StudentBus {
    private StudentDao studentDao = new StudentDao();
    public static ArrayList<StudentDTO> studentsCache;

    public void viewAll(){
        this.studentsCache = studentDao.findAll();
    }

    public int add(StudentDTO student){
        validate(student);
        studentsCache.add(student);
        return studentDao.insert(student);
    }
    public int update(StudentDTO student){
        validate(student);
        studentsCache.set(studentsCache.indexOf(student), student);
        return studentDao.update(student);
    }
    public int delete(int id){

        studentsCache.removeIf(s -> s.getId() == id);
        return studentDao.delete(id);
    }
    public int searchfromArray(String search){
        this.studentsCache.removeIf(s -> !s.getName().toLowerCase().contains(search.toLowerCase()) && !s.getSurname().toLowerCase().contains(search.toLowerCase()));
        return this.studentsCache.size();
    }
    public int searchfromQuery(String search){
        this.studentsCache = studentDao.findbyName(search);
        return this.studentsCache.size();
    }
    private void validate(StudentDTO student){
        if(student.getName() == null || student.getName().isEmpty()){
            throw new IllegalArgumentException("Name is required");
        }
        if(student.getSurname() == null || student.getSurname().isEmpty()){
            throw new IllegalArgumentException("Surname is required");
        }
        if(student.getBirthdate() == null || student.getBirthdate().isEmpty()){
            throw new IllegalArgumentException("Birthdate is required");
        }
        if(student.getHometown() == null || student.getHometown().isEmpty()){
            throw new IllegalArgumentException("Hometown is required");
        }
        if(student.getCreatetime() == null || student.getCreatetime().isEmpty()){
            throw new IllegalArgumentException("Createtime is required");
        }
    }
    
}
