package server.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "user")
public class User {
    @GeneratedValue
    @Id
    @Column(name="iduser")
    private Integer id;
    
    @NotNull
    @Size(min = 3, max = 100, message = "Username must from 3 to 100 characters.")
    @Column(name="username")
    private String username;
    
    @NotNull
    @Size(min = 3, max = 100, message = "Password must from 3 to 100 characters.")
    private String password;
    
    @Transient
    private String confirmpassword;
    
    @Email(message = "Email address is not valid.")
    @NotNull
    private String email;
    
    private String token;
    
    private String role = "";

    @Column(name="fname")
    private String firstname;

    @Column(name="lname")
    private String lastname;
    
    private String address;

    @Column(name="companyname")
    private String companyname;

    @Column(name="lastlogin")
    private String lastlogin;
    
    @Column(name="profilepicture")
    private String profilepicture;

    /*@OneToMany
    private Set<Module> modules;
 
    @JoinTable(
            name = "user_module",
            joinColumns = @JoinColumn(name = "iduser"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    */
	public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmpassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmpassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyname;
    }

    public void setCompanyName(String companyName) {
        this.companyname = companyName;
    }

    public String getLastLogin() {
        return lastlogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastlogin = lastLogin;
    }
    
    public String getProfilePicture() {
        return profilepicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilepicture = profilePicture;
    }

    public Boolean isAdmin() {
        return this.role.equals("ROLE_ADMIN");
    }

    public Boolean isUser() {
        return this.role.equals("ROLE_USER");
    }
    
    public Boolean isMatchingPasswords() {
        return this.password.equals(this.confirmpassword);
    }

	/**
	 * @return the modules
	 *//*
	public Set<Module> getModules() {
		return modules;
	}

	*//**
	 * @param modules the modules to set
	 *//*
	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}*/
}