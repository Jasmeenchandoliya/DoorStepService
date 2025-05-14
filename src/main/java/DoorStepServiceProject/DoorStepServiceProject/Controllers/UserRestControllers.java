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
import java.util.StringTokenizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
    public String us1(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            ResultSet rs = dbloader.executeQuery("select * from user where uemail='" + email + "'and upass='" + password + "' ");
            if (rs.next()) {
                session.setAttribute("uemail", email);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @GetMapping("/getCity")
    public String us2() {
        String ans1 = new RDBMS_TO_JSON().generateJSON("select * from cities");
        System.out.println(ans1);
        return ans1;
    }

    @PostMapping("/Service")
    public String us3(@RequestParam String cityid) {
        int cid = Integer.parseInt(cityid);
        System.out.println(cid);
        String query = "SELECT distinct services.* FROM services JOIN vendor ON vendor.vservice = services.sid WHERE vendor.vcity = '" + cid + "' and vendor.vstatus ='Accepted'";
        String ans = new RDBMS_TO_JSON().generateJSON(query);
        System.out.println(ans);
        return ans;
    }

    @PostMapping("/Vendor")
    public String us4(@RequestParam String servid, @RequestParam String cityid) {
        int id1 = Integer.parseInt(servid);
        int id2 = Integer.parseInt(cityid);
        String query = "SELECT distinct vendor.* FROM vendor JOIN services ON services.sid=vendor.vservice  WHERE vendor.vservice = '" + id1 + "' and vendor.vcity = '"+ id2 +"' ";
        String ans = new RDBMS_TO_JSON().generateJSON(query);
        return ans;
    }
    
    @PostMapping("/Singlevendor")
    public String us5(@RequestParam String vendorid) {
        int id1 = Integer.parseInt(vendorid);
        String query = "SELECT vendor.*,services.* FROM vendor JOIN services ON services.sid=vendor.vservice  WHERE vendor.vid = '"+ id1 +"'";
        String ans = new RDBMS_TO_JSON().generateJSON(query);
        return ans;
    }
    
    @PostMapping("/Otherservice")
    public String us6(@RequestParam String vendorid) {
        int id2 = Integer.parseInt(vendorid);
        String query = "SELECT vendor.*,vendorphoto.* FROM vendor JOIN vendorphoto on vendor.vid=vendorphoto.vid where vendor.vid = '"+ id2 +"' ";
        String ans = new RDBMS_TO_JSON().generateJSON(query);
        return ans;
    }
    
    @GetMapping("/view_slots")
    String view_slots(@RequestParam String email, @RequestParam String date) {

        System.out.println(date);
        System.out.println(email);
        try {
            ResultSet rs = dbloader.executeQuery("select * from vendor where vemail='" + email + "'");

            String start;
            String end;
            String slot;
            if (rs.next()) {
                start = rs.getString("vstart");
                end = rs.getString("vend");
                slot = rs.getString("vprice");

            } else {
                String err = "failed";
                return err;
            }
            int Start = Integer.parseInt(start);
            int End = Integer.parseInt(end);
            int Slot = Integer.parseInt(slot);
            JSONObject ans = new JSONObject();

            //Define JSONArray
            JSONArray arr = new JSONArray();
            for (int i = Start; i <= End; i++) {
                JSONObject row = new JSONObject();
                row.put("start_slot", Start);
                row.put("end_slot", ++Start);
                row.put("slot_amount", slot);

                ResultSet rs2 = dbloader.executeQuery("select * from booking_detail where start_slot ='" + i + "' and bid in (select booking_id from booking where date=\'" + date + "\' and vendor_email =\'" + email + "\' ) ");
                if (rs2.next()) {
                    row.put("status", "Booked");
                } else {
                    row.put("status", "Available");
                }

                arr.add(row);
            }
            ans.put("ans", arr);
            System.out.println(ans.toString());
            return (ans.toJSONString());

        } catch (Exception e) {
            return e.toString();
        }

    }
    
    @GetMapping("/pay")
    public String payment(@RequestParam String date,
            @RequestParam String vendor_email,
            @RequestParam String amount,
            @RequestParam String slots,
            HttpSession session,
            @RequestParam String type,
            @RequestParam String status) {
        String ans = "";
        String user_email = (String) session.getAttribute("uemail");

        try {
            // 1. Insert into booking table
            ResultSet rs = dbloader.executeQuery("SELECT * FROM booking");
            rs.moveToInsertRow();
            rs.updateString("date", date);
            rs.updateString("vendor_email", vendor_email);
            rs.updateString("user_email", user_email);
            rs.updateString("total_price", amount);
            rs.updateString("payment_type", type);
            rs.updateString("status", status);
            rs.insertRow();

            // 2. Get inserted booking_id
            int booking_id = 0;
            ResultSet rs2 = dbloader.executeQuery("SELECT MAX(booking_id) AS maxid FROM booking");
            if (rs2.next()) {
                booking_id = rs2.getInt("maxid");
            }

            // 3. Insert slots into booking_detail table
            StringTokenizer st = new StringTokenizer(slots, ",");
            while (st.hasMoreTokens()) {
                int start_slot = Integer.parseInt(st.nextToken());
                int end_slot = start_slot + 1;

                ResultSet rs3 = dbloader.executeQuery("SELECT * FROM booking_detail");
                rs3.moveToInsertRow();
                rs3.updateInt("bid", booking_id); // fk to booking_id
                rs3.updateString("start_slot", String.valueOf(start_slot));
                rs3.updateString("end_slot", String.valueOf(end_slot));
                rs3.insertRow();
            }

            ans = "success";
            return ans;
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    
}
