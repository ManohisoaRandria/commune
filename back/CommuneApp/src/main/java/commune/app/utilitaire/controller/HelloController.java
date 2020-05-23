/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.controller;


import commune.app.utilitaire.models.JSONResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author P11A-MANOHISOA
 */
@RestController
public class HelloController {
    @GetMapping(path ="/")
	public JSONResponse index(){
           
        JSONResponse hello = new JSONResponse("succes", 200,"hello", null);
        return hello;
    }

}

