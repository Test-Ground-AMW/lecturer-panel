package lk.ijse.dep11.lecpanel.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturers/visiting")
@CrossOrigin
public class VisitingLecturerHttpController {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/ranks", consumes = "application/json")
    public void arrangeVisitingLecturerOrder(){
        System.out.println("arrangeVisitingLecturerOrder()");
    }

    @GetMapping(produces = "application/json")
    public void getAllVisitingLecturers(){
        System.out.println("getAllVisitingLecturers()");
    }
}
