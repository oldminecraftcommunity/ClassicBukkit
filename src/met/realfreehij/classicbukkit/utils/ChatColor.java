package met.realfreehij.classicbukkit.utils;

public class ChatColor {
    public static final String BLACK = "&0";
    public static final String DARK_BLUE = "&1";
    public static final String DARK_GREEN = "&2";
    public static final String DARK_AQUA = "&3";
    public static final String DARK_RED = "&4";
    public static final String DARK_PURPLE = "&5";
    public static final String GOLD = "&6";
    public static final String GRAY = "&7";
    public static final String DARK_GRAY = "&8";
    public static final String BLUE = "&9";
    public static final String GREEN = "&a";
    public static final String AQUA = "&b";
    public static final String RED = "&c";
    public static final String PURPLE = "&d";
    public static final String YELLOW = "&e";
    public static final String WHITE = "&f";
	public static final String ANSI_RESET = "\u001B[0m";
    
    public static String toANSICode(String code) {
    	switch(code) {
    		case ChatColor.BLACK: return "\u001B[30m";
    		case ChatColor.DARK_BLUE: return "\u001B[34m";
    		case ChatColor.DARK_GREEN: return "\u001B[32m";
    		case ChatColor.DARK_AQUA: return "\u001B[36m";
    		case ChatColor.DARK_RED: return "\u001B[91m";
    		case ChatColor.DARK_PURPLE: return "\u001B[35m";
    		case ChatColor.GOLD: return "\u001B[33m";
    		
    		case ChatColor.GRAY: return "\u001B[38;5;8m";
    		case ChatColor.DARK_GRAY: return "\u001B[38;5;0m";
    		case ChatColor.BLUE: return "\u001B[94m";
    		case ChatColor.GREEN: return "\u001B[92m";
    		case ChatColor.AQUA: return "\u001B[96m";
    		case ChatColor.RED: return "\u001B[91m";
    		case ChatColor.PURPLE: return "\u001B[95m";
    		case ChatColor.YELLOW: return "\u001B[93m";
    		default: return "\u001B[37m";
    	}
    }

	public static String stringToANSI(String s) {
		String n = "";
		for(int i = 0; i < s.length()-1; ++i) {
			char c = s.charAt(i);
			if(c == '&') {
				char col = s.charAt(i+1);
				if(col == '0') n += toANSICode(ChatColor.BLACK);
				else if(col == '1') n += toANSICode(ChatColor.DARK_BLUE);
				else if(col == '2') n += toANSICode(ChatColor.DARK_GREEN);
				else if(col == '3') n += toANSICode(ChatColor.DARK_AQUA);
				else if(col == '4') n += toANSICode(ChatColor.DARK_RED);
				else if(col == '5') n += toANSICode(ChatColor.DARK_PURPLE);
				else if(col == '6') n += toANSICode(ChatColor.GOLD);
				else if(col == '7') n += toANSICode(ChatColor.GRAY);
				else if(col == '8') n += toANSICode(ChatColor.DARK_GRAY);
				else if(col == '9') n += toANSICode(ChatColor.BLUE);
				else if(col == 'a') n += toANSICode(ChatColor.GREEN);
				else if(col == 'b') n += toANSICode(ChatColor.AQUA);
				else if(col == 'c') n += toANSICode(ChatColor.RED);
				else if(col == 'd') n += toANSICode(ChatColor.PURPLE);
				else if(col == 'e') n += toANSICode(ChatColor.YELLOW);
				else if(col == 'f') n += toANSICode(ChatColor.WHITE);
				else n += c + col;
				++i;
			}else {
				n += c;
			}
		}
		n += s.charAt(s.length()-1);
		
		return n;
	}
	
}

