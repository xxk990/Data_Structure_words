
import java.io.*;
import  java.io.PrintWriter;
import java.util.*;

//Huffman code
//Group: Ke Xu
//		 FangZhou Liu
//		 Tej Modi


//This program will read a infile.dat file and convert it to Huffman code. It will display the frequency and Huffman code on
//the screen and writ the Frequency (%) table and Huffman code in outfile.dat

public class Huffman {
    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static Map<String, Integer> sortedMap;
    static String cha[];     //array of key (unsorte)
    static int chav[];      //array of value (unsort)
    static String cha1[];   //array of key (sorted)
    static int len;         //length of text
    static int total = 0;   //total bits of huffman code
    static String text = "";


    public static void main(String[] args) throws IOException {
        String output = "";
        int i =0;
        try {
            FileInputStream fis = new FileInputStream("infile.dat");
            while (fis.available() > 0) {
                char answer = (char) fis.read();
                if (Character.isLetter(answer) || Character.isDigit(answer)) {
                    output = output + answer;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(output);

        String input = output.toLowerCase();
        if (handleNewText(output))
            i = 1;
        }


    private static boolean handleNewText(String scanner) throws IOException {
        text = scanner;

        nodes.clear();
        codes.clear();
        calculateCharIntervals(nodes, text);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");
        printCodes();
        write();
        return false;
    }

    //write the table in outfile.dat
    private static void write() throws IOException {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream("outfile.dat"));
        }catch (IOException e) {
            System.out.println("\"Error opening the file stuff.txt\".");
            System.exit(0);
        }

        //count the total number of char.
        float valuep  = text.length();
        outputStream.println("Symbol      Frequency");
        for (int l = 0; l < cha.length; l++){

            outputStream.println(cha[l] + ":          " + chav[l]/valuep*100 + "%");
        }

        outputStream.println("Symbol          Huffman Codes");
        for(int k = 0; k < cha.length; k++){

            for(int m = 0; m < cha1.length; m++) {
                if(cha1[m].charAt(0) == cha[k].charAt(0))
                    outputStream.println(cha1[m]);

            }
        }
        outputStream.println("Total: " + total);
        //outputStream.open();
        outputStream.close();


    }

    //creat a tree
    private static void buildTree(PriorityQueue<Node> vector) {
        while (vector.size() > 1) {
            vector.add(new Node(vector.poll(), vector.poll()));
        }
    }

    //print the huffman code table
    private static void printCodes() {

        System.out.println("Symbol          Huffman Codes");
        cha1= new String[codes.size()];
        int n =0;

        for (Character name: codes.keySet()){
            String key = name.toString();
            cha1[n] = key +" :              " + codes.get(name).toString();
            total = codes.get(name).toString().length() +total;
            n++;
        }
       for(int k = 0; k < cha.length; k++){

        for(int m = 0; m < cha1.length; m++) {
           if(cha1[m].charAt(0) == cha[k].charAt(0))
                System.out.println(cha1[m]);

           }
        }
        System.out.println("Total: " + total);

        }

    //find frenquency of each char and put the frequency in PriorityQueue
    public static void calculateCharIntervals(PriorityQueue<Node> vector, String s) {
        text = s;
        len = s.length();
        String ch1;
        char[] char_array = text.toCharArray();
        System.out.println("Symbol      Frequency");
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < text.length(); i++) {
            ch1 = Character.toString(char_array[i]);
            if(map.containsKey(ch1)){
                map.put(ch1, map.get(ch1) + 1);
            }else {
                map.put(ch1, 1);
            }

        }


        sortedMap = sortByValue(map);
        int b =0;
        int valueq = text.length();
        map.entrySet().stream()
                .forEach(k -> vector.add(new Node(k.getValue()/(text.length() * 1.0), k.getKey())));
        cha = new String[sortedMap.size()];
        chav = new int[sortedMap.size()];
        for (String name: sortedMap.keySet()){
            String key = name.toString();
            cha[b] = key;
            chav[b] = sortedMap.get(name).intValue();
            b++;
        }

        for (int l = 0; l < cha.length; l++){
            System.out.println(cha[l] + ":          " + ((float)chav[l]/(float)valueq)*100 + "%");
        }
    }

    //generate Huffman code
    private static void generateCodes(Node node, String s) {
        if (node != null) {
            if (node.right != null)
                generateCodes(node.right, s + "1");

            if (node.left != null)
                generateCodes(node.left, s + "0");

            if (node.left == null && node.right == null)
                codes.put(node.character.charAt(0), s);
        }
    }

    //sort hashmap by value
    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}

class Node {
    Node left, right;
    double value;
    String character;

    public Node(double value, String character) {
        this.value = value;
        this.character = character;
        left = null;
        right = null;
    }

    public Node(Node left, Node right) {
        this.value = left.value + right.value;
        character = left.character + right.character;
        if (left.value < right.value) {
            this.right = right;
            this.left = left;
        } else {
            this.right = left;
            this.left = right;
        }
    }
}