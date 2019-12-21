package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping(value = "/")
    public String defaultMapping() {
        return "default";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        signupRepository.save(new Signup(name, address));
        return "done";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLogin() {
        return "login";
    }
    
    @RequestMapping(value = "/loginGuest", method = RequestMethod.POST)
    public String submitLogin(@RequestParam String name) {
        // TODO validate login and show guest-specific data
        return "welcome";
    }
    
    @RequestMapping(value = "/loginAdmin", method = RequestMethod.POST)
    public String submitLogin(@RequestParam String name, @RequestParam String password) {
        // TODO validate login and show all who signed up
        return "admin";
    }
}
