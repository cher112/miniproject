import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) throws IOException{
//         String UrlString = "cher112.top";
//         int Prob_num = 2;
//         ProcessBuilder pb = new ProcessBuilder("cmd","/c","ping "+"/n "+Prob_num+" "+UrlString);
//         Process process = pb.start();
//         Scanner scanner = new Scanner(process.getInputStream());
//         String regex = "(?<=\\=)\\d+";
//         Pattern p = Pattern.compile(regex);
//         ArrayList<String> a1 = new ArrayList<>();
//         while(scanner.hasNextLine()) {
//             int i = 0;
//         String output = scanner.nextLine();
//         System.out.println(output);
//         if(i<Prob_num && i>1){
//             Matcher m = p.matcher(output);
//             a1.add(m.group(0));
//         }
//     }
//     /*regex text*/
//     for (int i=0;i<a1.size();i++)
//    {
// 	System.out.println(a1.get(i).toString());
//    }
//     System.err.println(a1.toString());
    String text = "from 185.199.111.153 : code=32 time=189ms TTL=48";
    String regex = "(?<=\\=)\\d+";
    Pattern p = Pattern.compile(regex);
    ArrayList<String> a1 = new ArrayList<>();
    Matcher m = p.matcher(text);
    while (m.find()) { 
        a1.add(m.group(0));
    }
    
System.out.println("去除重复值前");
for (int i=0;i<a1.size();i++)
   {
	System.out.println(a1.get(i).toString());
   }
System.err.println(a1.toString());
        
    }
}
