package server.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import server.Decompress;
import server.models.Module;
import server.models.User;
import server.repositories.ModuleRepository;
import server.repositories.UserRepository;
import server.services.ModuleService;
import server.services.UserService;

@Controller
public class BusinessController {

	private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${app.user.modules.folder}")
    private String modulesFolder;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleRepository moduleRepository;

    /*@RequestMapping(value = "/modules/admin/PracticeMode/q1", method = RequestMethod.POST)
    public String modulesTrain() {
        

        return "admin/PracticeMode/q1" ;
    }*/
    
    @RequestMapping("/business/modules/{id}")
    public String modules(@PathVariable("id") Integer id, User user) {
        User u;
        User loggedInUser = userService.getLoggedInUser();
        
        if(loggedInUser != null && loggedInUser.getRole().equals("")){
        	return "redirect:/";
        }
        
        if(id == 0) {
            id = loggedInUser.getId();
        }
        if(loggedInUser.getId() != id && (!loggedInUser.isAdmin() || !loggedInUser.isUser())) {
            return "user/premission-denied";
        } else if (loggedInUser.isAdmin()) {
            u = userRepository.findOne(id);
        } else {
            u = loggedInUser;
        }
        
        user.setId(u.getId());
        user.setUserName(u.getUserName());
        user.setAddress(u.getAddress());
        user.setCompanyName(u.getCompanyName());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setRole(u.getRole());
        
        return "business/modules";
    }
    
    @RequestMapping(value = "/business/modules", method = RequestMethod.POST)
    public String modulesPost(@Valid User user, BindingResult result) {
    	
        if (result.hasFieldErrors("email")) {
            return "business/modules";
        }
        
        if(userService.getLoggedInUser().isAdmin() || userService.getLoggedInUser().isUser()) {
            userService.updateUser(user);
        } else {
            userService.updateUser(userService.getLoggedInUser().getUserName(), user);
        }

        if (userService.getLoggedInUser().getId().equals(user.getId())) {
            // put updated user to session
            userService.getLoggedInUser(true);
        }

        return "redirect:/business/modules/" + user.getId() + "?updated";
    }
    
    @RequestMapping("/business/business/{id}")
    public String business(@PathVariable("id") Integer id, User user, ModelMap map) {
    	Iterable<Module> module = this.moduleRepository.findAll();
        map.addAttribute("module", module);
    	
        User u;
        User loggedInUser = userService.getLoggedInUser();
        
        if(id == 0) {
            id = loggedInUser.getId();
        }
        if(loggedInUser.getId() != id && (!loggedInUser.isAdmin() || !loggedInUser.isUser())) {
            return "user/premission-denied";
        } else if (loggedInUser.isAdmin()) {
            u = userRepository.findOne(id);
        } else {
            u = loggedInUser;
        }
        
        user.setId(u.getId());
        user.setUserName(u.getUserName());
        user.setAddress(u.getAddress());
        user.setCompanyName(u.getCompanyName());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setRole(u.getRole());
        
        return "business/business";
    }
    
    @RequestMapping(value = "/business/business", method = RequestMethod.POST)
    public String businessPost(@Valid User user, BindingResult result, ModelMap map) {
    	Iterable<Module> module = this.moduleRepository.findAll();
        map.addAttribute("module", module);
        
    	User loggedInUser = userService.getLoggedInUser();
        
    	if (result.hasFieldErrors("email")) {
            return "business/business";
        }
        
        if(loggedInUser.isAdmin() || loggedInUser.isUser()) {
            userService.updateUser(user);
        } else {
            userService.updateUser(loggedInUser.getUserName(), user);
        }

        if (loggedInUser.getId().equals(user.getId())) {
            // put updated user to session
            userService.getLoggedInUser(true);
        }

        return "redirect:/business/business/" + user.getId() + "?updated";
    }
    
    @RequestMapping("/business/create/{id}")
    public String create(@PathVariable("id") Integer id, User user) {
        User u;
        User loggedInUser = userService.getLoggedInUser();
        if(id == 0) {
            id = loggedInUser.getId();
        }
        if(loggedInUser.getId() != id && (!loggedInUser.isAdmin() || !loggedInUser.isUser())) {
            return "user/premission-denied";
        } else if (loggedInUser.isAdmin()) {
            u = userRepository.findOne(id);
        } else {
            u = loggedInUser;
        }
        
        user.setId(u.getId());
        user.setUserName(u.getUserName());
        user.setAddress(u.getAddress());
        user.setCompanyName(u.getCompanyName());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setRole(u.getRole());
        
        return "business/create";
    }
    
    @RequestMapping(value = "/business/create", method = RequestMethod.POST)
    public String createPost(@Valid User user, BindingResult result) {
        if (result.hasFieldErrors("email")) {
            return "business/modules";
        }
        
        if(userService.getLoggedInUser().isAdmin() || userService.getLoggedInUser().isUser()) {
            userService.updateUser(user);
        } else {
            userService.updateUser(userService.getLoggedInUser().getUserName(), user);
        }

        if (userService.getLoggedInUser().getId().equals(user.getId())) {
            // put updated user to session
            userService.getLoggedInUser(true);
        }

        return "redirect:/business/create/" + user.getId() + "?updated";
    }

    @RequestMapping(value = "/business/upload", method = RequestMethod.POST)
    public String handleFolderUpload(@RequestParam("zip_file") MultipartFile file) {
		User user = userService.getLoggedInUser();

		Module module = new Module();
		module.setName(file.getOriginalFilename().substring(0,file.getOriginalFilename().length() - 4));
		module.setOwner(user.getUserName());
		module.setLocation("");
		//module.setUsers(new HashSet<User>());
		module.setOwner(user.getUserName());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		module.setLoaddate(df.format(Calendar.getInstance().getTime()));

		/*Set<Module> modules = user.getModules();
		if (modules == null)
			modules = new HashSet<Module>();
		
		modules.add(module);
		user.setModules(modules);*/
		
		moduleService.saveModule(module);
		//userService.updateUser(user);
		
		module = moduleRepository.findOneByName(module.getName());

		String workingDir = modulesFolder + File.separator;
		
		moduleRepository.updateLocation(module.getName(), File.separator + workingDir + module.getName() + "_" + module.getId() + File.separator + "q1.htm");
		moduleRepository.updateName(module.getId(), module.getName() + "_" + module.getId());
		
		File theDir = new File(workingDir);

		if (!theDir.exists()) {
			try{
				theDir.mkdir();
			} 
			catch(SecurityException e){
				e.printStackTrace();
			}
		}

		String zip_path_str = workingDir + file.getOriginalFilename();
		log.info(zip_path_str);

		byte[] bytes = null;

		try {
			bytes = file.getBytes();
			FileOutputStream fos = new FileOutputStream(zip_path_str);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   

		Decompress d = new Decompress(zip_path_str, workingDir); 
		d.unzip(); 
		
		new File(zip_path_str).delete();
		
		File dir = new File(modulesFolder + File.separator + module.getName() + File.separator);
        File newName = new File(modulesFolder + File.separator + module.getName() + "_" + module.getId() + File.separator);
        if ( dir.isDirectory() ) {
                dir.renameTo(newName);
        } else {
                dir.mkdir();
                dir.renameTo(newName);
        }
		
		return "redirect:/business/business/" + user.getId();
	}

    @RequestMapping("/module/delete")
    public String delete(Integer id) {
    	Module module = moduleRepository.findOneById(id);
    	
    	File moduleDir = new File(modulesFolder + File.separator + module.getName() + "_" + module.getId() + File.separator);
    	File filePath = null;
    	try {
			log.info("\nDeleted module "+moduleDir.getCanonicalPath().toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			filePath = new File(moduleDir.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		deleteDirectory(filePath);
    	
        moduleService.delete(id);
        
        User user = userService.getLoggedInUser();
        
        return "redirect:/business/business/" + user.getId() + "?updated";
    }
    
    /**
     * Force deletion of directory
     * @param path
     * @return
     */
    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    
}
