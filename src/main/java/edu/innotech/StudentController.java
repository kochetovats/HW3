//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.innotech;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    Integer id = 1;
    List<edu.innotech.Student> lst = new ArrayList();

    @GetMapping({"/student"})
    public ResponseEntity<edu.innotech.Student> getStudentByName(String name) {
        edu.innotech.Student st = (edu.innotech.Student)this.lst.stream().filter((x) -> x.getName().equals(name)).findFirst().orElse((edu.innotech.Student) null);
        return st == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body((edu.innotech.Student) null) : ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(st);
    }

    @GetMapping({"/student/{id}"})
    public ResponseEntity<edu.innotech.Student> getStudentByID(@PathVariable Integer id) {
        edu.innotech.Student st = (edu.innotech.Student)this.lst.stream().filter((x) -> x.getId().equals(id)).findFirst().orElse((edu.innotech.Student) null);
        return st != null ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(st) : ResponseEntity.status(HttpStatus.NOT_FOUND).body((edu.innotech.Student) null);
    }

    @PostMapping({"/student"})
    public ResponseEntity createStudent(RequestEntity<Student> req) {
        edu.innotech.Student st = (edu.innotech.Student)req.getBody();
        System.out.print("Studentfrom request:"+st.toString());
        if (st.getMarks() == null) {
            st.setMarks(new ArrayList());
        }

        if (st.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Object)null);
        } else if (st.getId() == null) {
            Integer var4 = this.id;
            this.id = this.id + 1;
            st.setId(var4);
            this.lst.add(st);
            return ResponseEntity.status(HttpStatus.CREATED).body(st.getId());
        } else {
            edu.innotech.Student stFind = (edu.innotech.Student)this.lst.stream().filter((x) -> x.getId().equals(st.getId())).findFirst().orElse((edu.innotech.Student) null);
            if (stFind == null) {
                this.lst.add(st);
                if (st.getId() > this.id) {
                    this.id = st.getId();
                }
            } else {
                stFind.setMarks(st.getMarks());
                stFind.setName(st.getName());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body((Object)null);
        }
    }

    @DeleteMapping({"/student/{id}"})
    public ResponseEntity<edu.innotech.Student> deleteStudent(@PathVariable Integer id) {
        edu.innotech.Student st = (edu.innotech.Student)this.lst.stream().filter((x) -> x.getId().equals(id)).findFirst().orElse((edu.innotech.Student) null);
        if (st != null) {
            this.lst.remove(st);
            return ResponseEntity.status(HttpStatus.OK).body((edu.innotech.Student) null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((edu.innotech.Student) null);
        }
    }

    @GetMapping({"/topStudent"})
    public ResponseEntity<List<edu.innotech.Student>> getTopStudent() {
        double max = this.lst.stream().mapToDouble((x) -> x.average()).max().orElse((double)-1.0F);
        if (max == (double)-1.0F) {
            return ResponseEntity.status(HttpStatus.OK).body((List<edu.innotech.Student>) null);
        } else {
            List<edu.innotech.Student> res = (List)this.lst.stream().filter((x) -> x.average() == max).collect(Collectors.toList());
            int maxLen = res.stream().mapToInt((x) -> x.getMarks().size()).max().orElse(-1);
            res = (List)res.stream().filter((x) -> x.getMarks().size() == maxLen).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(res);
        }
    }
}
