/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoorStepServiceProject.DoorStepServiceProject.Controllers;

import DoorStepServiceProject.DoorStepServiceProject.vmm.RDBMS_TO_JSON;
import DoorStepServiceProject.DoorStepServiceProject.vmm.dbloader;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jasme
 */
@RestController
public class VendorRestControllers {

    @PostMapping("/Vendorsignup")
    public String v1(@RequestParam String vname,
            @RequestParam String vemail,
            @RequestParam String vpass,
            @RequestParam String vcity,
            @RequestParam String vservice,
            @RequestParam String vsservice,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String price,
            @RequestParam String contact,
            @RequestParam MultipartFile photo,
            @RequestParam String desc) {
        System.out.println("controller called");
        try {

            ResultSet rs = dbloader.executeQuery("select * from vendor where vemail='" + vemail + "'");
            if (rs.next()) {
                return "exists";
            } else {

                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + photo.getOriginalFilename();
                rs.moveToInsertRow();

                rs.updateString("vname", vname);
                rs.updateString("vemail", vemail);
                rs.updateString("vpass", vpass);
                rs.updateInt("vcity", Integer.parseInt(vcity));
                rs.updateInt("vservice", Integer.parseInt(vservice));
                rs.updateString("vsservice", vsservice);
                rs.updateString("vstart-time", start);
                rs.updateString("vend-time", end);
                rs.updateString("vprice", price);
                rs.updateString("vcontact", contact);
                rs.updateString("vdesc", desc);
                rs.updateString("vstatus", "pending");
                rs.updateString("vphoto", orgName);

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

    @GetMapping("/getCities")
    public String v2() {
        String ans1 = new RDBMS_TO_JSON().generateJSON("select * from cities");
        return ans1;
    }

    @GetMapping("/getServices")
    public String v3() {
        String ans1 = new RDBMS_TO_JSON().generateJSON("select * from services");
        return ans1;
    }

    @PostMapping("/vendorlogin")
    public String v4(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            ResultSet rs = dbloader.executeQuery("select * from vendor where vemail='" + email + "'and vpass='" + password + "' ");
            if (rs.next()) {
                int id = rs.getInt("vid");
                session.setAttribute("vid", id);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @PostMapping("/vendorManagePhotos")
    public String v7(@RequestParam String vdesc, @RequestParam MultipartFile vphoto, HttpSession session) {
        try {
            Integer id = (Integer) session.getAttribute("vid");

            ResultSet rs = dbloader.executeQuery("select * from vendorphoto where vid='"+id+"'");

            String projectPath = System.getProperty("user.dir");
            String internal_path = "/src/main/resources/static";
            String folderName = "/myUploads";
            String orgName = "/" + vphoto.getOriginalFilename();
            rs.moveToInsertRow();

            rs.updateString("desc", vdesc);
            rs.updateString("photo", orgName);
            rs.updateInt("vid", id);
            rs.insertRow();
            byte b1[];

            FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
            b1 = vphoto.getBytes();
            fos.write(b1);
            return "success";

        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @GetMapping("/alreadyAddedPhotos")
    public String v8() {
        String ans1 = new RDBMS_TO_JSON().generateJSON("select * from vendorphoto");
        return ans1;
    }

    @GetMapping("/deletePhoto")
    public String v9(@RequestParam int id, HttpSession session) {
        try {

            ResultSet rs = dbloader.executeQuery("select * from vendorphoto where pid='" + id + "'  ");
            if (rs.next()) {

                session.getAttribute("vid");
                rs.deleteRow();
                return "photo deleted successfully";
            } else {
                return "photo doesn't exist";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @PostMapping("/editvdetails")
    public String v11(HttpSession session, @RequestParam String vname,
            @RequestParam String vcity,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String price,
            @RequestParam String contact,
            @RequestParam String desc) {
        System.out.println("controller called");
        try {
            Integer vid = (Integer) session.getAttribute("vid");
            ResultSet rs = dbloader.executeQuery("select * from vendor where vid='" + vid + "'");
            if (rs.next()) {
                rs.moveToCurrentRow();
                rs.updateString("vname", vname);
                rs.updateInt("vcity", Integer.parseInt(vcity));
                rs.updateString("vstart", start);
                rs.updateString("vend", end);
                rs.updateString("vprice", price);
                rs.updateString("vcontact", contact);
                rs.updateString("vdesc", desc);
                rs.updateRow();
                return "success";
            } else {
                return "vendor not found";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

@GetMapping("/getdata")
public String getdata(HttpSession session) {
    Integer id = (Integer) session.getAttribute("vid");
    String query = "SELECT v.*, s.sname " +
                   "FROM vendor v " +
                   "JOIN services s ON v.vservice = s.sid " +
                   "WHERE v.vid = '" + id + "'";
    String ans = new RDBMS_TO_JSON().generateJSON(query);
    return ans;
}


}
