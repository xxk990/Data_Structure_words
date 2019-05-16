import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {

    //count the total number of worlds
    public static int count(String s){
        int count = 0;
        int k = 0;
        String[] spart =s.split("\\s+");

        for(int i = 0; i < spart.length; i++){
            if (spart[i].equals("a")||spart[i].equals("and")||spart[i].equals("an")||spart[i].equals("but")||spart[i].equals("or")||
                    spart[i].equals("the")||spart[i].equals("s")||spart[i].equals("t"))
                k++;
            else
                count++;
        }
        return count;
    }

    public static void main(String[] args) {
        int total = 0;
        int indexp = 0;
        String regex = "[^a-zA-Z ]";
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        Trie a = new Trie();
        File file = new File("company.dat");

        //normalize company names and insert in tries
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                String name = sc.nextLine();
                name = name.replaceAll(regex, "");
                total++;
                String[] partname = name.split("    ");
                String[] realpartname = partname[0].split("\\s+");
                a.insert(realpartname[0]);
                if(partname.length>1) {
                    String[] partname2 = partname[1].split("\\s+");
                    for (int i = 0; i < partname2.length; i++)
                        a.insert(partname2[i]);
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found.");
        }

        //create a list of primary name
        String[] Pname = new String[total+1];
        try{
            Scanner sc2 = new Scanner(file);
            while (sc2.hasNextLine()){
                String name = sc2.nextLine();
                name = name.replaceAll(regex, "");
                Pname[indexp] = name;
                indexp++;
            }
            sc2.close();
        }catch (FileNotFoundException e){
            System.out.println();
        }

        //user input the text
        String s = "";
        String text = "";
        Scanner input1 = new Scanner(System.in);
        do {
            System.out.println("Enter your text here, and enter (.) to finish the text:");
            text = input1.nextLine();
            s = s + text;
        }while (!text.contains("(.)"));

        //normalize the text
        String try2 = s.replaceAll("[^a-zA-Z ]", " ");
        s = s.replaceAll("[^a-zA-Z ]", " ");
        System.out.println(s);
        int count = 0;
        String company = "";
        String[] part = s.split("[^a-zA-Z]");
        String[] praName = new String[1];

        //Search from tries and store the results in map
        for (int i = 0; i < part.length; i++){
            if(a.search(part[i])){
                for(int in = 0; in < Pname.length; in++){
                    if(!(Pname[in]== null)) {
                        if (Pname[in].contains(part[i]))
                            praName = Pname[in].split("    ");
                            company = praName[0];
                    }
                }
                if(result.containsKey(company)){
                    count = result.get(company);
                    result.put(company, count+1);
                }
                else if(!result.containsKey(part[i]))
                    result.put(company, 1);
            }
        }

        //print the result
        int totalc =0;
        float totalp = 0;
        DecimalFormat newFormat = new DecimalFormat("#.####");
        double precent;
        System.out.println("Company name" + "        " + "Count" + "         " + "Relevance" );
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            precent =(double)value/count(try2) * 100;
            double newPrecent = Double.valueOf(newFormat.format(precent));
            System.out.println(key + "           " + value + "             " + newPrecent + "%");
            totalp = totalp + ((float)value/count(s));
            totalc = totalc + value;
        }
        System.out.println("Total" + "               " + totalc + "             " + Double.valueOf(newFormat.format(totalp * 100)) + "%");
        System.out.println("Total words" + "         " + count(try2)+ "(without 'a, an, the...')");
    }
}
