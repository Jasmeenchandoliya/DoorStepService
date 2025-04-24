/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoorStepServiceProject.DoorStepServiceProject.Controllers;

import DoorStepServiceProject.DoorStepServiceProject.vmm.RDBMS_TO_JSON;
import DoorStepServiceProject.DoorStepServiceProject.vmm.dbloader;
import java.awt.Window;
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
public class AdminRestControllers {

    @PostMapping("/Adminlogin")
    public String go1(@RequestParam String email, @RequestParam String password) {
        try {
            ResultSet rs = dbloader.executeQuery("select * from admin where email='" + email + "'and pass='" + password + "' ");
            if (rs.next()) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @PostMapping("/AdminManageCities")
    public String go4(@RequestParam String cityname, @RequestParam String citydescription, @RequestParam MultipartFile cityphoto) {
        try {

            ResultSet rs = dbloader.executeQuery("select * from cities where cityname='"+cityname+"'  ");
            if (rs.next()) {
                return "exists";
            } else {

                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + cityphoto.getOriginalFilename();
                rs.moveToInsertRow();
                rs.updateString("cityname", cityname);
                rs.updateString("citydesc", citydescription);
                rs.updateString("cityphoto", orgName);
                rs.insertRow();
                byte b1[];

                FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
                b1 = cityphoto.getBytes();
                fos.write(b1);
                return "success";

            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @GetMapping("/alreadyAddedCities")
    public String go5() {
        String ans1=new RDBMS_TO_JSON().generateJSON("select * from cities");
        return ans1;
    }
    
    @GetMapping("/deleteCity")
    public String go6(@RequestParam int id) {
        try {
            
            ResultSet rs = dbloader.executeQuery("select * from cities where cityid='"+id+"'  ");
            if(rs.next()){
                rs.deleteRow();
                return "success";
            }else{
                return "not found";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    @PostMapping("/AdminManageServices")
    public String go8(@RequestParam String sname, @RequestParam String sdesc, @RequestParam MultipartFile sphoto) {
        try {

            ResultSet rs = dbloader.executeQuery("select * from services where sname='"+sname+"'  ");
            if (rs.next()) {
                return "exists";
            } else {

                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + sphoto.getOriginalFilename();
                rs.moveToInsertRow();
                rs.updateString("sname", sname);
                rs.updateString("sdesc", sdesc);
                rs.updateString("sphoto", orgName);
                rs.insertRow();
                byte b1[];

                FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
                b1 = sphoto.getBytes();
                fos.write(b1);
                return "success";

            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    @GetMapping("/alreadyAddedServices")
    public String go9() {
        String ans1=new RDBMS_TO_JSON().generateJSON("select * from services");
        return ans1;
    }
    
    @GetMapping("/deleteService")
    public String go10(@RequestParam int id) {
        try {
            
            ResultSet rs = dbloader.executeQuery("select * from services where sid='"+id+"'  ");
            if(rs.next()){
                rs.deleteRow();
                return "service deleted successfully";
            }else{
                return "service doesn't exist";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    @GetMapping("/serviceprovider")
    public String go11() {
        String ans1=new RDBMS_TO_JSON().generateJSON("select * from vendor");
        return ans1;
    }
    
    @GetMapping("/Accept")
    public String go12(@RequestParam int id) {
        try {
            
            ResultSet rs = dbloader.executeQuery("select * from vendor where vid='"+id+"'  ");
            if(rs.next()){
                
                return "vendor accepted";
            }else{
                return "vendor blocked";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    @GetMapping("/Block")
    public String go13(@RequestParam int id) {
        try {
            
            ResultSet rs = dbloader.executeQuery("select * from vendor where vid='"+id+"'  ");
            if(rs.next()){
                
                return "vendor blocked";
            }else{
                return "vendor accepted";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }
}
