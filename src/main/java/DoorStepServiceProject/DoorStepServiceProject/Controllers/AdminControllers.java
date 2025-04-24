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
public class AdminControllers {
    @GetMapping("/AdminLogin")
    public String go(){
        return "AdminLogin";
    }
    
    @GetMapping("/AdminHome")
    public String go2(){
        return "/AdminHome";
    }
    
    @GetMapping("/AdminManageCities")
    public String go3(){
        return "AdminManageCities";
    }
    
    @GetMapping("/AdminManageServices")
    public String go7(){
        return "AdminManageServices";
    }
    
    @GetMapping("/AdminManageServiceProvider")
    public String go10(){
        return "AdminManageServiceProvider";
    }
}
