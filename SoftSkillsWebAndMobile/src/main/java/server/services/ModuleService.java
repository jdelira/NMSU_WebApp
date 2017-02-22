package server.services;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.models.Module;
import server.repositories.ModuleRepository;

@Service
public class ModuleService {
	
    @Autowired
    private ModuleRepository repo;
    
    public final String CURRENT_USER_KEY = "CURRENT_USER";
    
    public Module saveModule(Module module) {
        if (this.repo.findOneByName(module.getName()) == null) {
            this.repo.save(module);
            return module;
        }

        return null;
    }
    
    public Boolean delete(Integer id) {
        this.repo.delete(id);
        return true;
    }
    
    public void updateModule(Module module) {
        updateModule(module.getName(), module);
    }
    
    public void updateModule(String name, Module newData) {
        this.repo.updateModule(
                name, 
                newData.getOwner(), 
                newData.getLocation(), 
                newData.getLoaddate());
    }
}