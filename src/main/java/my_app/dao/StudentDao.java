package my_app.dao;

import java.util.ArrayList;
import java.util.HashMap;

import my_app.model.StudentDTO;
import my_app.util.QueryExecutor;
public class StudentDao implements Generic<StudentDTO> {
    private static QueryExecutor queryExecutor = new QueryExecutor();    

    private ArrayList<StudentDTO> toArrayDTO(ArrayList<HashMap<String,Object>> data){
        ArrayList<StudentDTO> students = new ArrayList<>();
        for (HashMap<String, Object> row : data) {
            StudentDTO student = new StudentDTO();
            student.setId((int) row.get("id"));
            student.setName((String) row.get("name"));
            student.setSurname((String) (row.get("surname")!=null ? row.get("surname") : row.get("sur_name")));
            student.setCreatetime((String) (    row.get("createtime")!=null ? row.get("createtime").toString() : row.get("create_time").toString()));
            student.setBirthdate((String) (row.get("birthdate")!=null ? row.get("birthdate").toString() : row.get("birth_date").toString()));
            student.setHometown((String) row.get("hometown"));
            students.add(student);
        }
        return students;
    }

    @Override
    public ArrayList<StudentDTO> findbyName(String search) {
        String query = "SELECT * FROM students WHERE name LIKE ? OR surname LIKE ?";
        
        ArrayList<HashMap<String, Object>> results = queryExecutor.ExecuteQuery(query, "%" + search + "%", "%" + search + "%");
        
        return toArrayDTO(results);
    }

    @Override
    public ArrayList<StudentDTO> findbyID(int id) {
        String query = "SELECT * FROM students WHERE id = ?";
        
        ArrayList<HashMap<String, Object>> results = queryExecutor.ExecuteQuery(query, id);
        
        return toArrayDTO(results);
    }

    @Override
    public ArrayList<StudentDTO> findAll() {
        String query = "SELECT * FROM students";
        ArrayList<HashMap<String, Object>> results = queryExecutor.ExecuteQuery(query);
        return toArrayDTO(results);
    }
    @Override
    public int insert(StudentDTO obj) {
        String query = "INSERT INTO students (name, surname, create_time, birth_date, hometown)VALUES (?, ?, ?, ?, ?)";
        int a = queryExecutor.ExecuteUpdate(query,obj.getName(),obj.getSurname(),obj.getCreatetime(),obj.getBirthdate(),obj.getHometown());
        return a;
    }
    @Override
    public int update(StudentDTO obj) {
        String query = "UPDATE students SET name = ?, surname = ?, create_time = ?, birth_date = ?, hometown = ? WHERE id = ?";
        int a = queryExecutor.ExecuteUpdate(query,obj.getName(),obj.getSurname(),obj.getCreatetime(),obj.getBirthdate(),obj.getHometown(),obj.getId());
        return a;
    }
    @Override
    public int delete(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        int a = queryExecutor.ExecuteUpdate(query,id);
        return a;
    }

}
