package server.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import server.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findOneByUsername(String name);
    User findOneByEmail(String email);
    User findOneByUsernameOrEmail(String username, String email);
    User findOneByToken(String token);
    
    @Modifying
    @Transactional
    @Query("update User u set u.email = :email, u.firstname = :firstname, u.lastname = :lastname, "
            + "u.address = :address, u.companyname = :companyname, u.role = :role "
            + "where u.username = :username")
    int updateUser(
            @Param("username") String username, 
            @Param("email") String email,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            @Param("address") String address,
            @Param("companyname") String companyname,
            @Param("role") String role);
    
    @Modifying
    @Transactional
    @Query("update User u set u.lastlogin = CURRENT_TIMESTAMP where u.username = ?1")
    int updateLastLogin(String username);
    
    @Modifying
    @Transactional
    @Query("update User u set u.profilepicture = ?2 where u.username = ?1")
    int updateProfilePicture(String username, String profilepicture);
    
    /*@Modifying
    @Transactional
    @Query("insert into user_module (iduser, id) values (iduser, id)")
    int addModule( 
            @Param("iduser") Integer iduser,
            @Param("id") Integer id );*/

}