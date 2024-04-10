package DTO;

public class UserDTO {

	private String id;
	private String password;
	private boolean is_active = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", password=" + password + ", is_active=" + is_active + "]";
	}

}
