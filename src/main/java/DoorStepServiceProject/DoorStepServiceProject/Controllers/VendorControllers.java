/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoorStepServiceProject.DoorStepServiceProject.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author jasme
 */
@Controller
public class VendorControllers {
    @GetMapping("/VendorSignup")
    public String v(){
        return "VendorSignup";
    }
    
    @GetMapping("/VendorLogin")
    public String v4(){
        return "VendorLogin";
    }
    
    @GetMapping("/VendorHome")
    public String v5(){
        return "VendorHome";
    }
    
    @GetMapping("/VendorManagePhotos")
    public String v6(){
        return "VendorManagePhotos";
    }
    
    @GetMapping("/EditVendorDetails")
    public String v10(){
        return "EditVendorDetails";
    }
}

