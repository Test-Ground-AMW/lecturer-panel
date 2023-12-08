package lk.ijse.dep11.lecpanel.api;

import lk.ijse.dep11.lecpanel.to.request.LecturerReqTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public void createNewLecturer(@ModelAttribute @Valid LecturerReqTO lecturer){

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{lecturer-id}",consumes = "multipart/form-data")
    public void updateLecturerDetails(){
        System.out.println("updateLecturer()");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(){
        System.out.println("deleteLecturer()");
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(produces = "application/json")
    public void getAllLecturers(){
        System.out.println("getAllLecturers()");
    }
}
