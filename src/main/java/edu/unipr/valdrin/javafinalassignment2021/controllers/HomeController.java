package edu.unipr.valdrin.javafinalassignment2021.controllers;

import edu.unipr.valdrin.javafinalassignment2021.models.COVID19LocationsStatistics;
import edu.unipr.valdrin.javafinalassignment2021.services.COVID19DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller     //The @Controller annotation is a specialization of the generic stereotype @Component annotation, which allows a class to be recognized as a Spring-managed component. The @Controller annotation extends the use-case of @Component and marks the annotated class as a business or presentation layer.
public class HomeController {
    @Autowired      //In the spring boot, the @Autowired annotation is used in setter methods to inject the value of the class properties. When the bean is loaded in the ApplicationContext, the setter method is automatically called by the spring boot and the value is assigned
    COVID19DataService COVID19DataService;
    @GetMapping("/")    //@GetMapping annotation maps HTTP GET requests onto specific handler methods. It is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod. GET)
    public String home(Model model) {  //The model represents a Java object carrying data. The view visualizes the data that the model contains. The controller manages the data flow into model object and updates the view whenever data changes; it keeps view and model separate.
        List<COVID19LocationsStatistics> allStatistics = COVID19DataService.getAllStatistics(); //Controller getting access to allStatistics
        int totalReportedCases = allStatistics.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum(); //Taking the list of objects allStatistics converting into a stream and mapping into a integer and than each one of integer maps to the integer value.
        int totalNewCases = allStatistics.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats",allStatistics);      //Add the supplied attribute to this Map using a generated name
        model.addAttribute("totalReportedCases",totalReportedCases);
        model.addAttribute("totalNewCases",totalNewCases);

        return "home"; //return a template value
    }
}
