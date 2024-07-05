public class MethodCapitalizeWord {

    public static String CapitalizeWords(String str) {
        String[] words = str.toLowerCase().split(" ");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedString.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return capitalizedString.toString().trim();
    }
}
