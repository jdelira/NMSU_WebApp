package server.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import server.models.Module;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer> {
    Module findOneById(Integer id);
    Module findOneByName(String name);
    Module findOneByOwner(String owner);
    Module findOneByLocation(String location);
    
    @Modifying
    @Transactional
    @Query("update Module m set m.name = :name, m.owner = :owner, m.location = :location, m.loaddate = :loaddate")
    int updateModule(
            @Param("name") String name, 
            @Param("owner") String owner,
            @Param("location") String location,
            @Param("loaddate") String loaddate);

    @Modifying
    @Transactional
    @Query("update Module m set m.location = ?2 where m.name = ?1")
    int updateLocation(String name, String location);
    
    @Modifying
    @Transactional
    @Query("update Module m set m.name = ?2 where m.id = ?1")
    int updateName(Integer id, String name);
    
    /*@Modifying
    @Transactional
    @Query("insert into module_user (id, iduser) values (id, iduser)")
    int addModule(
            @Param("id") Integer id, 
            @Param("iduser") Integer iduser);*/

}