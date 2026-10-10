package lsgwr.exam.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    @GetMapping({
            "/",
            "/login",
            "/register",
            "/dashboard",
            "/hall",
            "/questions",
            "/papers",
            "/users",
            "/subjects",
            "/grades",
            "/records",
            "/records/{id}",
            "/exam/{id}"
    })
    public String index() {
        return "forward:/index.html";
    }
}
