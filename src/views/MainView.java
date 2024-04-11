package views;

import java.util.Collections;
import java.util.List;

import DTO.ChatDTO;
import DTO.LogDTO;
import DTO.ModelDTO;

public class MainView {

	public static void printMenus(String title, String[] contents, char borderChar) {
		final int boxWidth = 50;

        System.out.println();
		printBorder(borderChar, boxWidth);
		printTitle(title, boxWidth);
		printBorder(borderChar, boxWidth);
		if (contents != null) {
			for (int i = 0; i < contents.length; i++) {
				System.out.printf("%d.%s\n", i+1, contents[i]);
			}
		}
	}

	public static void printBorder(char symbol, int width) {
		for (int i = 0; i < width; i++) {
			System.out.print(symbol);
		}
		System.out.println();
	}

	public static void printTitle(String title, int width) {
		int padding = (width - title.length()) / 2;
		for (int i = 1; i < padding; i++) {
			System.out.print(" ");
		}
		System.out.print(title);
		for (int i = 0; i < padding - 1; i++) {
			System.out.print(" ");
		}
		System.out.println("");
	}
	
	public static void printChatDTOs(List<ChatDTO> dtos) {
	    String header = String.format("| %-7s | %-20s | %-20s | %-14s | %-14s | %-14s |",
	            "Chat ID", "Name", "Model", "Stream Enabled", "Memory Enabled", "Cache Enabled");
	    String line = "+---------+----------------------+----------------------+----------------+----------------+----------------+";
	    
	    System.out.println(line);
	    System.out.println(header);
	    System.out.println(line);
	    
	    for (ChatDTO dto : dtos) {
	        String row = String.format("| %-7d | %-20s | %-20s | %-14b | %-14b | %-14b |",
	                dto.getChat_id(), 
	                dto.getName() == null ? "null" : dto.getName(),
	                dto.getModel() == null ? "null" : dto.getModel(),
	                dto.isStream_enabled(), 
	                dto.isMemory_enabled(), 
	                dto.isCeche_enabled());
	        System.out.println(row);
	    }
	    System.out.println(line);
	}
	
	public static void printChatDTOs(ChatDTO dto) {
        printChatDTOs(Collections.singletonList(dto));
    }
	
	public static void printLogDTOs(List<LogDTO> dtos) {
	    String header = String.format("| %-7s | %-20s | %-40s | %-15s | %-18s |",
	            "Log ID", "Request", "Response", "Prompt Tokens", "Completion Tokens");
	    String line = "+---------+----------------------+------------------------------------------+-----------------+--------------------+";

	    System.out.println(line);
	    System.out.println(header);
	    System.out.println(line);

	    for (LogDTO dto : dtos) {
	        String row = String.format("| %-7d | %-20s | %-40s | %-15d | %-18d |",
	                dto.getLog_id(),
	                trimAndAppendEllipsis(dto.getRequest(), 20),
	                trimAndAppendEllipsis(dto.getResponse(), 40),
	                dto.getPrompt_tokens(),
	                dto.getCompletion_tokens());
	        System.out.println(row);
	    }
	    System.out.println(line);
	}

	private static String trimAndAppendEllipsis(String text, int maxLength) {
	    if (text.length() > maxLength) {
	        return text.substring(0, maxLength - 1) + "…";
	    }
	    return text;
	}
	
	public static void printModelDTOs(List<ModelDTO> dtos) {
        String header = String.format("| %-3s | %-25s | %-10s | %-15s | %-15s |",
                "No", "Name", "Company", "Price/PToken", "Price/CToken");
        String line = "+-----+---------------------------+------------+-----------------+-----------------+";

        System.out.println(line);
        System.out.println(header);
        System.out.println(line);

        int no = 1;
        for (ModelDTO dto : dtos) {
            String row = String.format("| %-3d | %-25s | %-10s | %-15s | %-15s |",
                    no++, dto.getName(), dto.getCompany(),
                    formatPrice(dto.getPrice_per_ptoken()), formatPrice(dto.getPrice_per_ctoken()));
            System.out.println(row);
        }
        System.out.println(line);
    }

    private static String formatPrice(double price) {
        return String.format("%.4f/1k", price * 1000);
    }
}
