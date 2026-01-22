package com.devsuperior.dscommerce.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "tb_user")//@Table é opcional → Serve apenas para personalizar o nome da tabela ou definir configurações específicas (como constraints, índices, esquema).
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//Não preciso colocar @Column para os demais atributos, a não ser que queira renomeá-los no banco de dados
	private Long id;
	private String name;
	
	//Coluna que não aceita repetição, é única. Achave primária já é por padrão única
	@Column(unique = true)
	private String email; //e-mail vai ser o username para fazer o login na aplicação
	private String Phone;
	private LocalDate birthDate;
	private String password;
	
	@OneToMany(mappedBy = "client")//mapear com a outra tabela. Importante: colocar exatemente o mesmo o nome do 
	//atributo da classe Order (private User client;)
	private List<Order> orders = new ArrayList<>();	
	
	@ManyToMany
    @JoinTable(name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
	
	public User() {		
	}
	public User(Long id, String name, String email, String phone, LocalDate birthDate, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.Phone = phone;
		this.birthDate = birthDate;
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Order> getOrders() {
		return orders;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	//adicionar role à lista
    public void addRole(Role role) {
    	roles.add(role);
    }
    
    //Verificar se um usuário possui determinado role
    public boolean hasRole(String roleName) {
    	for (Role role : roles) {
    		if(role.getAuthority().equals(roleName)) {
    			return true;
    		}
    	}
    	return false;    	
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	
	 //Métodos abaixo que deverão ser implementados por causa da interface UserDetails
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() { //Role implementa GrantedAuthority
			return roles;
		}

		@Override
		public String getUsername() { //defini que meu UserName vai ser o e-mail
			return email;
		}

		//esse método não vou trabalhar, assim deixo uma implementação padrão
		@Override
		public boolean isAccountNonExpired() {		
			return true;
		}

		//esse método não vou trabalhar, assim deixo uma implementação padrão
		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		//esse método não vou trabalhar, assim deixo uma implementação padrão
		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		//esse método não vou trabalhar, assim deixo uma implementação padrão
		@Override
		public boolean isEnabled() {
			return true;
		}
}
