package com.renthub.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


	    @GetMapping("/hello")
	    public String home() {
	        return "Welcome to RentHub - Find Rooms, Homes and PGs";
}
}