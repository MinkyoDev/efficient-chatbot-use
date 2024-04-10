import DTO.UserDTO;
import controllers.HomeController;
import controllers.MenuController;

public class Main {

	public static void main(String[] args) {
//		HomeController.homeConroller();
//		ChatController.chatController();
		
		UserDTO user = new UserDTO();
		user.setId("22");
		user.setPassword("2222");
		MenuController.menuController(user);
	}
	
}
