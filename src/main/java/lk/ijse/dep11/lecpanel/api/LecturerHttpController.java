package lk.ijse.dep11.lecpanel.api;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.lecpanel.to.request.LecturerReqTO;
import lk.ijse.dep11.lecpanel.to.response.LecturerResTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @Autowired
    private DataSource pool;

    @Autowired
    private Bucket bucket;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerResTO createNewLecturer(@ModelAttribute @Valid LecturerReqTO lecturer){
        try(Connection connection = pool.getConnection()) {
            connection.setAutoCommit(false);
            try {
                PreparedStatement stmInsertLecturer = connection.prepareStatement("INSERT INTO lecturer (name, designation, qualifications, linkedin) " +
                        "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                stmInsertLecturer.setString(1,lecturer.getName());
                stmInsertLecturer.setString(2,lecturer.getDesignation());
                stmInsertLecturer.setString(3,lecturer.getQualifications());
                stmInsertLecturer.setString(4,lecturer.getLinkedin());
                stmInsertLecturer.executeUpdate();
                ResultSet generatedKeys = stmInsertLecturer.getGeneratedKeys();
                generatedKeys.next();
                int lecturerId = generatedKeys.getInt(1);
                String picture = lecturerId + "-" + lecturer.getName();

                if (lecturer.getPicture() != null && !lecturer.getPicture().isEmpty()){
                    PreparedStatement stmUpdateLecturer = connection.prepareStatement("UPDATE lecturer SET picture = ? WHERE id = ?");
                    stmUpdateLecturer.setString(1,picture);
                    stmUpdateLecturer.setInt(2,lecturerId);
                    stmUpdateLecturer.executeUpdate();
                }

                final String table = lecturer.getType().equalsIgnoreCase("full-time") ? "full_time_rank" : "visiting_rank";
                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT `rank` FROM " + table + " ORDER BY `rank` DESC LIMIT 1");
                int rank;
                if (!rst.next()) rank = 1;
                else rank = rst.getInt("rank")+1;

                PreparedStatement stmInsertRank = connection.prepareStatement("INSERT INTO " + table + " (lecturer_id, `rank`) VALUES (?,?)");
                stmInsertRank.setInt(1,lecturerId);
                stmInsertRank.setInt(2,rank);
                stmInsertRank.executeUpdate();

                String pictureUrl = null;
                if (lecturer.getPicture() != null && !lecturer.getPicture().isEmpty()){
                    Blob blob = bucket.create(picture, lecturer.getPicture().getInputStream(), lecturer.getPicture().getContentType());
                    pictureUrl = blob.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString();
                }
                connection.commit();
                return new LecturerResTO(lecturerId,lecturer.getName(),lecturer.getDesignation(),lecturer.getQualifications(),lecturer.getType(),pictureUrl,lecturer.getLinkedin());
            } catch (Throwable t) {
                connection.rollback();
                throw t;
            } finally {
                connection.setAutoCommit(true);
            }


        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{lecturer-id}",consumes = "multipart/form-data")
    public void updateLecturerDetails(){
        System.out.println("updateLecturer()");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") int lecturerId){
        try(Connection connection = pool.getConnection()) {
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM lecturer WHERE id = ?");
            stmExist.setInt(1,lecturerId);
            if (!stmExist.executeQuery().next()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            connection.setAutoCommit(false);
            try {
                PreparedStatement stmIdentify = connection.prepareStatement("SELECT l.id, l.name, l.picture, ftr.`rank` AS ftr, vr.`rank` AS vr FROM " +
                        "lecturer l LEFT OUTER JOIN full_time_rank ftr ON l.id = ftr.lecturer_id LEFT OUTER JOIN visiting_rank vr on " +
                        "l.id = vr.lecturer_id WHERE l.id = ?");
                stmIdentify.setInt(1,lecturerId);
                ResultSet rst = stmIdentify.executeQuery();
                rst.next();
                String picture = rst.getString("picture");
                int ftr = rst.getInt("ftr");
                int vr = rst.getInt("vr");
                String tableName = ftr > 0 ? "full_time_rank" : "visiting_rank";
                int rank = ftr > 0 ? ftr : vr;

                Statement stmDelRank = connection.createStatement();
                stmDelRank.executeUpdate("DELETE FROM " + tableName + " WHERE `rank` = " + rank);

                Statement stmShift = connection.createStatement();
                stmShift.executeUpdate("UPDATE "+ tableName + " SET `rank` = `rank` - 1 WHERE `rank` > " + rank);

                PreparedStatement stmDelLec = connection.prepareStatement("DELETE FROM lecturer WHERE id = ?");
                stmDelLec.setInt(1,lecturerId);
                stmDelLec.executeUpdate();

                if (picture != null) bucket.get(picture).delete();

                connection.commit();
            } catch (Throwable t) {
                connection.rollback();
                throw t;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(produces = "application/json")
    public void getAllLecturers(){
        System.out.println("getAllLecturers()");
    }
}
