Framework: Spring Framework (5.1.0) with JSP

Requirements to run:
1. Tomcat 9 (Deploy the generated .war file in /target/ on Tomcat)
2. Java 8+
3. MySQL 8 (create a db named test_db before running)

Building: mvn clean package (better to have maven 3.6 installed)

Entry-page to access on deployment: http://localhost:8080/spring-web-0.0.1/upload-form to upload an xml file
After upload, try accessing http://localhost:8080/spring-web-0.0.1/generate-xml to download the table xml file
