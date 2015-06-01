package com.pinyin;
  
public class PinYinUtil {  
      
//    public static String getPinYin(String inputString) {  
//          
//        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
//        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
//        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);  
//        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);  
//  
//        char[] input = inputString.trim().toCharArray();  
//        StringBuffer output = new StringBuffer("");  
//  
//        try {  
//            for (int i = 0; i < input.length; i++) {  
//                if (Character.toString(input[i]).matches("[\\\\u4E00-\\\\u9FA5]+")) {  
//                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);  
//                    output.append(temp[0]);  
//                    output.append(" ");  
//                } else  
//                    output.append(Character.toString(input[i]));  
//            }  
//        } catch (BadHanyuPinyinOutputFormatCombination e) {  
//            e.printStackTrace();  
//        }  
//        return output.toString();  
//    }  
//      
//    public static void main(String[] args) {  
//        String chs = "�����й���! I'm Chinese!";  
//        System.out.println(chs);  
//        System.out.println(getPinYin(chs));  
//    }  
      
} 