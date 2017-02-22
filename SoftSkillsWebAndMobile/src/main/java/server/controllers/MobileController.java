package server.controllers;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import server.models.Module;
import server.models.User;
import server.repositories.ModuleRepository;
import server.repositories.UserRepository;
import server.services.MailService;
import server.services.ModuleService;
import server.services.UserService;

@Controller
// @RequestMapping("/mobile/*")
public class MobileController {
    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Value("${app.androidFolder}")
    private String applicationFolder;
    
    @Value("${app.user.verification}")
    private Boolean requireActivation;
    
    @Value("${app.user.root}")
    private String userRoot;
    
    @Value("${app.url}")
    private String appURL;
    
    @Value("${spring.mail.username}")
    private String serverManagerEmail;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;
    
    @RequestMapping("/mobile/thymeleaf_to_param")
    public String thymeleafToParam(String location, String mode, ModelMap model) {
    	Module module = moduleRepository.findOneByLocation(location);
        User user = userService.getLoggedInUser();

        model.addAttribute("mode", mode);
        model.addAttribute("appURL", appURL);
    	model.addAttribute("folderLocation", location.substring(0, location.length() - 6));
    	model.addAttribute("testId", module.getId());
    	model.addAttribute("and", "&");
        
        return "mobile/thymeleaf_to_param";
    }
    
    @RequestMapping(value={"/mobile/logresults"}, method=RequestMethod.GET)
    public String getparam(HttpServletRequest request) {
    	User u = userService.getLoggedInUser();
    	
        Map<String, String[]> parameters = request.getParameterMap();
        Integer testId = Integer.parseInt(parameters.get("testId")[0]);
        Integer score = Integer.parseInt(parameters.get("score")[0]);
        
        Module testModule = moduleRepository.findOneById(testId);
        User owner = userRepository.findOneByUsername(testModule.getOwner());
        
        String emailStr = u.getFirstName() + " " + u.getLastName() + " took soft skills test " + 
        				testModule.getName() + " the the resulting score was " + score + "\n";
        
        mailService.sendMail(owner.getEmail(), "Soft Skills Test Result", emailStr);
        /*String str = "";
        for(String key : parameters.keySet()) {
        	str += key + " = ";

            String[] vals = parameters.get(key);
            for(String val : vals)
            	str += val + ", ";
        } */
        log.info("\n"+ u.getUserName() + ": testId=" + testId + ", score=" + score +" email=" + owner.getEmail());
    	return "mobile/start" ;
    }
    
    @RequestMapping("/mobile/start/{id}")
    public String modules(@PathVariable("id") Integer id, User user) {
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
        
        
        return "mobile/start";
    }
    
    @RequestMapping(value = "/mobile/start")
    public String mobilePost(@Valid User user, BindingResult result) {
    	
        if (result.hasFieldErrors("email")) {
            return "mobile/start";
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

        return "redirect:/mobile/start/" + user.getId() + "?updated";
    }

    @RequestMapping("/mobile/practice-list/{id}")
    public String mobilePracticeList(@PathVariable("id") Integer id, User user, ModelMap map) {
    	Iterable<Module> module = this.moduleRepository.findAll();
        map.addAttribute("module", module);
    	
        User u;
        User loggedInUser = userService.getLoggedInUser();
        
        if(id == 0) {
            id = loggedInUser.getId();
        }
        
        u = loggedInUser;
        
        user.setId(u.getId());
        user.setUserName(u.getUserName());
        user.setAddress(u.getAddress());
        user.setCompanyName(u.getCompanyName());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setRole(u.getRole());
        
        return "mobile/practice-list";
    }
    
    @RequestMapping(value = "/mobile/practice-list")
    public String mobilePracticeListPost(@Valid User user, BindingResult result, ModelMap map) {
    	Iterable<Module> module = this.moduleRepository.findAll();
        map.addAttribute("module", module);
        
    	User loggedInUser = userService.getLoggedInUser();
        
    	if (result.hasFieldErrors("email")) {
            return "mobile/practice-list";
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

        return "redirect:/mobile/practice-list/" + user.getId() + "?updated";
    }

    @RequestMapping("/mobile/test-list/{id}")
    public String mobileTestList(@PathVariable("id") Integer id, User user, ModelMap map) {
    	Iterable<Module> module = this.moduleRepository.findAll();
        map.addAttribute("module", module);
    	
        User u;
        User loggedInUser = userService.getLoggedInUser();
        
        if(id == 0) {
            id = loggedInUser.getId();
        }
        
        u = loggedInUser;
        
        user.setId(u.getId());
        user.setUserName(u.getUserName());
        user.setAddress(u.getAddress());
        user.setCompanyName(u.getCompanyName());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setRole(u.getRole());
        
        return "mobile/test-list";
    }
    
    @RequestMapping(value = "/mobile/test-list")
    public String mobileTestListPost(@Valid User user, BindingResult result, ModelMap map) {
    	Iterable<Module> module = this.moduleRepository.findAll();
        map.addAttribute("module", module);
        
    	User loggedInUser = userService.getLoggedInUser();
        
    	if (result.hasFieldErrors("email")) {
            return "mobile/test-list";
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

        return "redirect:/mobile/test-list/" + user.getId() + "?updated";
    }
    
    /*@RequestMapping("/detect-device")
    public String detectDevice(Device device, ModelMap map) {
        String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
        
        map.addAttribute("params", "?testId=exampletest1&testURL=/modules/result.html");
        return "thymeleaf_to_param";
    }*/
    
    @RequestMapping(value = "/mobile/upload", method = RequestMethod.POST)
    public String handleAPKUpload(@RequestParam("apk_file") MultipartFile file) {
    	
		String workingDir = applicationFolder;

		File theDir = new File(workingDir);

		if (!theDir.exists()) {
			try{
				theDir.mkdir();
			} 
			catch(SecurityException e){
				e.printStackTrace();
			}
		}

		String apk_path_str = workingDir + file.getOriginalFilename();
		log.info("\n"+apk_path_str);

		byte[] bytes = null;

		try {
			bytes = file.getBytes();
			FileOutputStream fos = new FileOutputStream(apk_path_str);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
    
}