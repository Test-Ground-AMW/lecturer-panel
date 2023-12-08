package lk.ijse.dep11.lecpanel.api;

import com.google.protobuf.ValueOrBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @PostMapping
    public void createNewLecturer(){
        System.out.println("createNewLecturer()");
    }

    @PatchMapping("/{lecturer-id}")
    public void updateLecturerDetails(){
        System.out.println("updateLecturer()");
    }

    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(){
        System.out.println("deleteLecturer()");
    }

    @GetMapping
    public void getAllLecturers(){
        System.out.println("getAllLecturers()");
    }
}
