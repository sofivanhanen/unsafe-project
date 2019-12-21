package sec.project.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    private static final String GUEST_NAME_KEY = "guest-name";
    private static final String LOGGED_IN_KEY = "logged";

    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private HttpSession httpSession;

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
        for (Signup signup : signupRepository.findAll()) {
            if (signup.getName().equals(name)) {
                httpSession.setAttribute(GUEST_NAME_KEY, name);
                httpSession.setAttribute(LOGGED_IN_KEY, true);
                return "redirect:/welcome";
            }
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String loadWelcome(Model model) {
        try {
            String name = (String) httpSession.getAttribute(GUEST_NAME_KEY);
            for (Signup signup : signupRepository.findAll()) {
                if (signup.getName().equals(name)) {
                    model.addAttribute("name", name);
                    model.addAttribute("address", signup.getAddress());
                    return "welcome";
                }
            }
        } catch (Exception e) {
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "/loginAdmin", method = RequestMethod.POST)
    public String submitLogin(@RequestParam String name, @RequestParam String password) {
        if (name.equals("admin") && password.equals("aluminum")) {
            httpSession.setAttribute(LOGGED_IN_KEY, true);
            return "redirect:/admin";
        } else {
            return "redirect:/login"; // TODO show revealing error 
        }
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdmin(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        return "admin";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.DELETE)
    public String deleteAdmin() {
        try {
            if ((boolean)httpSession.getAttribute(LOGGED_IN_KEY)) {
                signupRepository.deleteAll();
                return "redirect:/admin";
            }
        }
        catch (Exception e) {
        }
        return "redirect:/login";
    }
}
