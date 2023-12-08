package lk.ijse.dep11.lecpanel.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturers/full-time")
@CrossOrigin
public class FullTimeLecturerHttpController {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/ranks", consumes = "application/json")
    public void arrangeFullTimeLecturerOrder(){
        System.out.println("arrangeFullTimeLecturerOrder()");
    }

    @GetMapping(produces = "application/json")
    public void getAllFullTimeLecturers(){
        System.out.println("getAllFullTimeLecturers()");
    }
}
