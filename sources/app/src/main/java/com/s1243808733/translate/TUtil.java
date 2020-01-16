package com.s1243808733.translate;

public class TUtil {

    public static String wordSegmentation(String content) {
        StringBuffer sb=new StringBuffer();
		
		char[] charArray=content.toCharArray();
        int length=charArray.length;

        boolean build=false;

        for (int i=0;i < length;i++) {
            char c=charArray[i];

            if (c == '(') {
                build = true;
            } else if (build && c == ')') {
                build = false;
            }

            if (i > 0 && Character.isLowerCase(charArray[i - 1])
                && Character.isLetter(c)
                && Character.isUpperCase(c)) { 
                sb.append(' ');
            }

            if (!Character.isLetter(c) && c != ' ') {
                if (i > 0) {
                    char last=charArray[i - 1];
                    if (last != '.' && c != '.' && last != ' ') {
                        if (!Character.isDigit(last)) {
                            if (last != ',') {
                                sb.append(' ');
                            }
                        } else if (!Character.isLetter(c) && !Character.isDigit(c)) {
                            sb.append(' ');
                        }
                    }

                    if (c == '.') {
                        if (i + 1 < length) {
                            if (!Character.isDigit(charArray[i + 1])) {
                                sb.append(' ');
                            }
                        } else {
                            sb.append(' ');
                        }
                    }

                }
                sb.append(c);
                if (build && c == ',' && i > 0 && i + 1 < length
                    && charArray[i - 1] != ' '
                    && charArray[i + 1] != ' ') {
                    sb.append(' ');
                } else if (i + 1 < length && Character.isLetter(charArray[i + 1])) {
                    sb.append(' ');
                }
            } else {
                sb.append(c);
                if (build && c == ' ' && i > 0 && i + 1 < length) {
                    char last=charArray[i - 1];
                    char next=charArray[i + 1];
                    if ((last == '>' || last == ']') || (Character.isLetter(last) && Character.isLetter(next))) {
                        sb.append(':');
                        sb.append(' ');
                    }
                }
            }

        }
        return sb.toString();

    }

}
