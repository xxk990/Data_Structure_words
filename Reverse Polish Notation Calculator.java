
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    //convert the string to a queue in infix form
    private static Queue<String> infix(String s){
        Queue<String> infix = new LinkedList<String>();

        if(!s.equals("quit")) {

            String regex = "(?<=[-+*/%^()])|(?=[-+*/%^()])";
            String[] splitArray = s.split(regex);

            if (!s.equals("quit")) {
                for (int i = 0; i < splitArray.length; i++) {

                    infix.add(splitArray[i]);
                }
            }

        }
        return infix;
    }

    //convert the infix form to postfix form
    private static Queue<String> postfix(Queue<String> infix){
        Queue<String> postfix = new LinkedList<String>();
        Queue<String> postfix1 = new LinkedList<String>();
        Stack<String> myStack = new Stack<String>();

        while (!infix.isEmpty()) {
            String t = infix.element();
            if (isNumeric(t)) {
                postfix.add(" " + t);
                postfix1.add(t);
            } else if (myStack.isEmpty())
                myStack.push(t);
            else if (infix.element().equals("("))
                myStack.push(t);
            else if (infix.element().equals(")")) {
                while (!myStack.peek().equals("(")) {
                    String l = myStack.peek();
                    postfix.add(l);
                    postfix1.add(l);
                    myStack.pop();
                }
                myStack.pop();
            } else {
                while (!myStack.isEmpty() && !myStack.peek().equals("(") && (precedenceLevel(t.charAt(0))) <= precedenceLevel(myStack.peek().toString().charAt(0))) {
                    postfix.add(myStack.peek());
                    postfix1.add(myStack.peek());
                    myStack.pop();
                }
                myStack.push(t);
            }
            infix.remove();
        }
        while (!myStack.isEmpty()) {
            postfix.add(myStack.peek());
            postfix1.add(myStack.peek());
            myStack.pop();

        }
        while (!postfix.isEmpty()) {

            System.out.print(postfix.remove());

        }
        return postfix1;

    }

    //check the precedence level of math operators
    public static int precedenceLevel(char op) {
        switch (op) {
            case '+':
            case '-':
                return 0;
            case '*':
            case '/':
            case '%':
                return 1;
            case '^':
                return 2;
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }

    //check the string is number or not
    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    //calculate the result
    public static void calculate(Queue<String> postfix){
        float result = 0;
        float first = 0;
        float second = 0;
        boolean s =true;
        Stack<Float> Stack = new Stack<Float>();


        while (!postfix.isEmpty()) {
            if (isNumeric(postfix.element())) {
                Stack.push(Float.valueOf(postfix.remove()).floatValue());
            } else if (postfix.element().equals("+")) {
                //first = Integer.parseInt(Stack.pop().toString());
                first = Stack.pop();
                if(!Stack.isEmpty()) {
                    second = Stack.pop();
                    result = first + second;
                }
                else {
                    System.out.println("");
                    System.out.println("Please enter a valid math form!");
                    s = false;
                    break;
                }
                Stack.push(result);
                postfix.remove();
            } else if (postfix.element().equals("*")) {
                first = Stack.pop();
                if(!Stack.isEmpty()) {
                    second = Stack.pop();
                    result = first * second;
                }
                else {
                    System.out.println("");
                    System.out.println("Please enter a valid math form!");
                    s = false;
                    break;
                }
                Stack.push(result);
                postfix.remove();
            } else if (postfix.element().equals("/")) {
                first = Stack.pop();
                if(!Stack.isEmpty()) {
                    second = Stack.pop();
                    result = second / first;
                }
                else {
                    System.out.println("");
                    System.out.println("Please enter a valid math form!");
                    s = false;
                    break;
                }
                Stack.push(result);
                postfix.remove();
            } else if (postfix.element().equals("-")) {
                first = Stack.pop();
                if(!Stack.isEmpty()) {
                    second = Stack.pop();
                    result = second - first;
                }
                else {
                    System.out.println("");
                    System.out.println("Please enter a valid math form!");
                    s = false;
                    break;
                }
                Stack.push(result);
                postfix.remove();
            } else if (postfix.element().equals("^")) {
                first = Stack.pop();
                if(!Stack.isEmpty()) {
                    second = Stack.pop();
                    result = (int) Math.pow(second, first);
                }
                else {
                    System.out.println("");
                    System.out.println("Please enter a valid math form!");
                    s = false;
                    break;
                }
                Stack.push(result);
                postfix.remove();
            } else if (postfix.element().equals("%")) {
                first = Stack.pop();
                if(!Stack.isEmpty()) {
                    second = Stack.pop();
                    result = second % first;
                }
                else {
                    System.out.println("");
                    System.out.println("Please enter a valid math form!");
                    s = false;
                    break;
                }
                Stack.push(result);
                postfix.remove();
            }


        }
        if(s) {
            System.out.println("");
            System.out.println(" result= " + result);
        }
    }


    public static void main(String[] args) {
        String question1;
        Scanner question = new Scanner(System.in);
        //let the user to enter the math form is the enter the 'quit' to quit
        do {
            System.out.println("Enter a math question:(enter 'quit' to quit)(no decimal and negative number) ");
            question1 = question.nextLine();

            if(question1.matches("[-+*^%/()0-9]+")) {
                if(!(question1.charAt(0)== '-' )) {
                    if (!question1.equals("quit"))

                        calculate(postfix(infix(question1)));

                    System.out.println();
                }
                else
                    System.out.println("no negative number please.");
            }
            else if(!question1.equals("quit")) {
                System.out.println("Please enter a valid math form!");
            }
        }
        while (!question1.equals("quit")) ;

    }
}
