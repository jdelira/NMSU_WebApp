package server.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "module")
public class Module {

	@GeneratedValue
	@Id
	private Integer id;

	@NotNull
	private String name;

	@NotNull
	private String owner;

	@NotNull
	private String location;

	@NotNull
	private String loaddate;

    /*@OneToMany
    private Set<User> users;
 
    @JoinTable(
            name = "module_user",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "iduser")
    )
    */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLoaddate() {
		return loaddate;
	}

	public void setLoaddate(String loaddate) {
		this.loaddate = loaddate;
	}

	/**
	 * @return the users
	 *//*
	public Set<User> getUsers() {
		return users;
	}

	*//**
	 * @param users the users to set
	 *//*
	public void setUsers(Set<User> users) {
		this.users = users;
	}*/
}
