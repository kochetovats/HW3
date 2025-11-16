//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.innotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBoot {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBoot.class, args);
        System.out.println("REST service returning JSON student");
        System.out.println("get http://localhost:8080/student?name=value");
        System.out.println("get http://localhost:8080/student/{id}");
        System.out.println("delete http://localhost:8080/student/{id}");
        System.out.println("post http://localhost:8080/student");
        System.out.println("get http://localhost:8080/topStudent");
        System.out.println("port can be changed by jvm property -Dserver.port=8090");
    }
}
