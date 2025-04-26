/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoorStepServiceProject.DoorStepServiceProject.Controllers;

import DoorStepServiceProject.DoorStepServiceProject.vmm.dbloader;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jasme
 */
@RestController
public class UserRestControllers {

    @PostMapping("/Usersignup")
    public String us(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String pass,
            @RequestParam String address,
            @RequestParam String contact,
            @RequestParam MultipartFile photo) {
        System.out.println("controller called");
        try {

            ResultSet rs = dbloader.executeQuery("select * from user where uemail='" + email + "'");
            if (rs.next()) {
                return "exists";
            } else {

                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + photo.getOriginalFilename();
                rs.moveToInsertRow();

                rs.updateString("uname", name);
                rs.updateString("uemail", email);
                rs.updateString("upass", pass);
                rs.updateString("ucontact", contact);
                rs.updateString("uaddress", address);
                rs.updateString("uphoto", orgName);

                rs.insertRow();
                byte b1[];

                FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
                b1 = photo.getBytes();
                fos.write(b1);
                return "success";

            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @PostMapping("/userlogin")
    public String us1(@RequestParam String email, @RequestParam String password) {
        try {
            ResultSet rs = dbloader.executeQuery("select * from user where uemail='" + email + "'and upass='" + password + "' ");
            if (rs.next()) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }
}
