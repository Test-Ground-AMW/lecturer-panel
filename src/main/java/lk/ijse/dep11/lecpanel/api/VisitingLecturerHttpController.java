package lk.ijse.dep11.lecpanel.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturers/visiting")
@CrossOrigin
public class VisitingLecturerHttpController {

    @PatchMapping("/ranks")
    public void arrangeVisitingLecturerOrder(){
        System.out.println("arrangeVisitingLecturerOrder()");
    }

    @GetMapping
    public void getAllVisitingLecturers(){
        System.out.println("getAllVisitingLecturers()");
    }
}
