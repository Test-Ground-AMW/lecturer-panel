package lk.ijse.dep11.lecpanel.to.request;

import lk.ijse.dep11.lecpanel.validation.LecturerImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerReqTO implements Serializable {
    @NotBlank(message = "Name can't be empty")
    @Pattern(regexp = "^[A-Za-z ]+$",message = "Invalid name")
    private String name;

    @NotBlank(message = "Designation can't be empty")
    @Length(min = 2, message = "Invalid designation")
    private String designation;

    @NotBlank(message = "qualifications can't be empty")
    @Length(min = 2, message = "Invalid qualification")
    private String qualifications;

    @NotBlank(message = "type can't be empty")
    @Pattern(regexp = "^(full-time|visiting)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid type")
    private String type;

    @LecturerImage(message = "File size exceeded 5kb")
    private MultipartFile picture;

    @Pattern(regexp = "^http[s]?://.+$", message = "Invalid linkedin url")
    private String linkedin;
}
