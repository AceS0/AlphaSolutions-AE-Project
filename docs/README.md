
![image](https://github.com/user-attachments/assets/6b28ae59-cf0c-430f-a8d3-18a09a990ef5)

# Alpha Solutions Project  
_A collaboration between Abdulcelil Sekerci and Enes Zeki Filikci_

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)

---

## Goal of the Project  
This application is developed to help Alpha Solutions efficiently manage ongoing projects. It offers a streamlined solution for project organization and task delegation, making it equally suitable for small businesses and larger enterprises in need of a simplified project management tool.

---

## About the Program  

There are **three user roles**, each with distinct responsibilities and access levels:

### Admin  
Admins have full control and access. They can:
-  Create, delete, and manage users  
-  Assign roles (Admin or Project Manager or Employee)  
-  Create projects and assign Project Managers  
-  Assign users to projects  
-  Access everything Project Managers and Employees can  

### Project Manager  
Project Managers can:
- manage projects
- Create and manage subprojects  
- Create, edit, and delete tasks  
- Assign/unassign users to/from projects and tasks 
- View all project details  

### Employee  
Employees can:
-  Mark tasks as completed  
-  Log hours worked on tasks  
-  View task assignments  

---

## Tech Stack  
-  **Java SDK 21.0.4** (Project runs on Java 17)  
-  **IntelliJ IDEA 2024.2.2**  
-  **Spring Boot 3.4.5**  
-  **Spring Web 3.4.5**  
-  **MySQL 8.0**  
-  **JUnit 5.9.2 (Jupiter)**  
-  **Mockito 5.3.1**  
-  **HTML**,  **CSS**,  **Thymeleaf**  

---

### Installation

1. Klon repo fra https://github.com/AceS0/AlphaSolutions-AE-Project.git
2. Opret en MySQL database kaldet 'kanban_db', kør MySQL scripts fra src/main/resources/sql scripts/
3. Setup dine environment variables så de passer til dine database credentials, eller indtast dem direkte i
   src/main/resources/application-dev.properties og ændre active profile til dev i application.properties
<pre>   spring.datasource.url={Url} -> Ex. jdbc:mysql://localhost:3306/kanban_db {eller deployed database}
   spring.datasource.username={Username} -> Ex. root
   spring.datasource.password={Password} -> Ex. password
</pre>
4. Byg og kør projektet.
5. Webapplikationen vil nu kunne køres lokalt på http://localhost:8080/

---

## Credits  
This project was developed as part of a semester collaboration between:

- **Abdulcelil Sekerci** –> [https://github.com/AceS0](https://github.com/AceS0)  
- **Enes Zeki Filikci** –> [https://github.com/Enfi0001](https://github.com/Enfi0001)  

Special thanks to **Alpha Solutions** for providing a real-world use case and continuous feedback during development.  
