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
public class UserControllers {

    @GetMapping("/UserSignup")
    public String u() {
        return "UserSignup";
    }
    
    @GetMapping("/UserLogin")
    public String u1(){
        return "UserLogin";
    }

}
