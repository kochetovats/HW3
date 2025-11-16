package edu.innotech;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.IMarkerFactory;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class RestTests {





    /*
    get /student/{id}
    1. get /student/{id} возвращает JSON студента с указанным ID и заполненным именем, если такой есть в базе, код 200.
     * */
     @Test @SneakyThrows
    //Проверяем, что для возвращается существующий студент
    public void TestGetStudent200(){
        int id=101;
        String name="PeterTest";
        List<Integer> lst = List.of(3,3,3);
         RestAssured.given()
                    .baseUri("http://localhost:8090/student/"+id)
                    .contentType(ContentType.JSON)
                    //.body(mapper.writeValueAsString(st1))
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", Matchers.equalTo(id))
                    .body("name", Matchers.equalTo(name))
                    .body("marks",Matchers.equalTo(lst));
    }

   @Test @SneakyThrows
    //Проверяем, что для несуществующего студента вернется 404
   //2. get /student/{id} возвращает код 404, если студента с данным ID в базе нет.
    public void TestGetStudent404(){
        int id=0;
        //Student st1 = new Student("Vasia");
        RestAssured.given()
                        .baseUri("http://localhost:8090/student/"+id)
                        .contentType(ContentType.JSON)
                .when()
                     .get()
                .then()
                     .statusCode(404);
    }

    @Test @SneakyThrows
    //Проверка успешного создания нового студента
    //post /student добавляет студента в базу, если студента с таким ID ранее не было, при этом имя заполнено, код 201
    public void TestPostStudent(){
        Student st1 = new Student();
        List<Integer> lst = List.of(5,5,5);
        st1.setId(200);
        st1.setMarks(lst);
        st1.setName("KateTest");
        System.out.println(st1.toString());
        RestAssured.given()
                    .baseUri("http://localhost:8090/student")
                    .contentType(ContentType.JSON)
                    .body(st1.toString())
                .when()
                    .post()
                .then()
                    .statusCode(201);
    }

   @Test @SneakyThrows
    //Проверка обновления имени существующего студента
    // 4/ post /student обновляет студента в базе, если студент с таким ID ранее был, при этом имя заполнено, код 201.
    public void TestPostExsitedStudentNewName(){
        Student st1 = new Student();
        List<Integer> lst = List.of(5,5,5,5);
        st1.setId(100);
        st1.setMarks(lst);
        st1.setName("IvanTest2");
        System.out.println(st1.toString());
        //обновляем имя
        RestAssured.given()
                .baseUri("http://localhost:8090/student")
                .contentType(ContentType.JSON)
                .body(st1.toString())
                .when()
                .post()
                .then()
                .statusCode(201);
       //проверяем, что имя обновилось
       RestAssured.given()
                    .baseUri("http://localhost:8090/student/"+st1.getId())
                    .contentType(ContentType.JSON)
                  .when()
                     .get()
                  .then()
                     .statusCode(200)
                     .body("name", Matchers.equalTo(st1.getName()));
    }

    @Test @SneakyThrows
    //Проверка обновления оценок существующего студента
    //4. post /student обновляет студента в базе, если студент с таким ID ранее был, при этом имя заполнено, код 201.
    public void TestPostExsitedStudentNewMarks(){
        Student st1 = new Student();
        List<Integer> lst = List.of(4,4,4,4);
        st1.setId(102);
        st1.setMarks(lst);
        st1.setName("AlexanderTest");
        System.out.println(st1.toString());
        RestAssured.given()
                .baseUri("http://localhost:8090/student")
                .contentType(ContentType.JSON)
                .body(st1.toString())
                .when()
                .post()
                .then()
                .statusCode(201);

        RestAssured.given()
                .baseUri("http://localhost:8090/student/"+st1.getId())
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("marks", Matchers.equalTo(st1.getMarks()));
    }

    @Test @SneakyThrows
    //Проверка обновления оценок существующего студента
    //5. post /student добавляет студента в базу, если ID null, то возвращается назначенный ID, код 201.
    public void TestPostStudentNullId(){
        String reqBody="{\n" +
                "    \"name\":\"Daniel2\",\n" +
                "    \"marks\":[2,3,4,5]\n" +
                "}\n";
        RestAssured.given()
                         .baseUri("http://localhost:8090/student")
                         .contentType(ContentType.JSON)
                          .body(reqBody)
                .when()
                         .post()
                .then()
                          .statusCode(201)
                           .body(notNullValue());
    }

    @Test @SneakyThrows
    //Проверка, что попытка создания студента без имени вернет 400 ошибку
    //6 post /student возвращает код 400, если имя не заполнено.
    public void TestPostStudent400(){
        String reqBody="{\n" +
                "    \"id\":9,\n" +
                "    \"marks\":[2,3,4,5]\n" +
                "}";
        RestAssured.given()
                .baseUri("http://localhost:8090/student")
                .contentType(ContentType.JSON)
                .body(reqBody)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test @SneakyThrows
    //Проверка успешного удаления студента
    //7. delete /student/{id} удаляет студента с указанным ID из базы, код 200.
    public void TestDeleteStudent200(){
        int id = 103;
        RestAssured.given()
                     .baseUri("http://localhost:8090/student/"+id)
                     .contentType(ContentType.JSON)
                .when()
                    .delete()
                .then()
                     .statusCode(200);
    }

    @Test @SneakyThrows
    //Проверка НЕуспешного удаления студента
    //8. delete /student/{id} возвращает код 404, если студента с таким ID в базе нет.
    public void TestDeleteStudent404(){
        int id = 0;
        RestAssured.given()
                .baseUri("http://localhost:8090/student/"+id)
                .contentType(ContentType.JSON)
                .when()
                .delete()
                .then()
                .statusCode(404);
    }

    @Test @SneakyThrows
    //Проверка получения пустого тела ответа,  если стулентов нет
    //9.     //get /topStudent код 200 и пустое тело, если студентов в базе нет.
    public void TestTopStudent200Empty(){
        int id = 0;
        RestAssured.given()
                .baseUri("http://localhost:8090/topStudent/")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(0));
    }

    @Test @SneakyThrows
    //Проверка получения пустого тела ответа,  если стулентов нет
    //10.get /topStudent код 200 и пустое тело, если ни у кого из студентов в базе нет оценок.
    public void TestTopStudent200(){
        int id = 0;
        RestAssured.given()
                .baseUri("http://localhost:8090/topStudent/")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(0));
    }


    @Test @SneakyThrows
    //Проверка получения пустого тела ответа,  если стулентов нет
    //11. //get /topStudent код 200 и один студент, если у него максимальная средняя оценка, либо же среди всех студентов с максимальной средней у него их больше всего.

    public void TestTopStudent200max(){
        RestAssured.given()
                .baseUri("http://localhost:8090/topStudent/")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("", not(empty()))
                .body("[0].name", notNullValue());
    }


    @Test @SneakyThrows
    //Проверка получения пустого тела ответа,  если стулентов нет
    //12. get /topStudent код 200 и несколько студентов, если у них всех эта оценка максимальная и при этом они равны по количеству оценок.
    public void TestTopStudent200max2(){
        int id = 0;
        RestAssured.given()
                .baseUri("http://localhost:8090/topStudent/")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("", not(empty()))
                .body("", hasSize(greaterThan(1)));
    }

    public void deleteTestStudent(int id) {
         Response resp = RestAssured.given()
                         .baseUri("http://localhost:8090/student/" + id)
                         .contentType(ContentType.JSON)
                     .when()
                        .delete();
         //  .then().statusCode(200);
        String message =  resp.statusCode()==200
                ? "Student with "+ id + " was deleted"
                : "An error occured when delleting student with id " + id;
        System.out.println(message);
    }

    public void createTestStudent(Student st) {
        System.out.println( "Start to creating student: \n" +st.toString());
        Response resp = RestAssured.given()
                    .baseUri("http://localhost:8090/student")
                    .contentType(ContentType.JSON)
                    .body(st.toString())
                .when()
                     .post();
                //     .then()
               // .statusCode(201);
        String message =  resp.statusCode()==200
                ? "Student with "+ st.getId() + " was created"
                : "An error occured while creating student with id " + st.getId();
        System.out.println(message);
    }

    @BeforeEach
    public void createTestData()
    {
        System.out.println("****Start tests. Creating test data.****");
        List <String> names =  List.of("IvanTest","PeterTest","AlexanderTest","VladimirTest","PavelTest",
                                "SergeyTest","MaximTest","NikolayTest","IgorTest","VadimTest");
        int initialId = 100;
        for (int i = 0; i < names.size(); i++) {
            Student st = new Student();
            st.setId(initialId + i);
            st.setName(names.get(i));
            List<Integer> marks = List.of(3,3,3);
            if (i%2 == 0) marks = List.of(2, 3, 4);
            if (i%3  == 0) marks = List.of(3, 4, 5);
            if (i%5  == 0) marks = List.of(5, 5, 5);
            st.setMarks(marks);
            createTestStudent(st);
        }
    }

    @AfterEach
    public void deteleTestData(){
        System.out.println("****Start tests. Clearing test data.****");
        int initialId = 100;
        for (int i = 0; i < 10; i++){
            deleteTestStudent(initialId+i);
        }
    }

}
