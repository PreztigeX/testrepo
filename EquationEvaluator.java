/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laberintos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;
        
/**
 *
 * @author RPF
 */
public class EquationEvaluator {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("¡Solo se permite un archivo!");
        } else {
            File f = new File(args[0]);
            try {
                BufferedReader b = new BufferedReader(new FileReader(f));
                String readLine = "";
                String text = b.readLine();
                String eval = Evaluar(text);
                System.out.println(eval);
                System.out.println("Resultado: " + getResult(eval));
            } catch (FileNotFoundException e) {
                System.out.println("Error: " + e);
            }
        }
    }
    
    public static String Evaluar(String linea) {
        System.out.println("Linea: " + linea);
        StringTokenizer st = new StringTokenizer(linea, "()+-*/", true);
        Stack<String> pilaOperadores = new Stack<String>();
        StringBuilder postEval = new StringBuilder();
        String token = null;
        
        while (st.hasMoreTokens()) {
            String simbolo = st.nextToken().trim();
            
            if (simbolo.isEmpty()) {
                continue;
            }
            
            if (simbolo.equals("(")) {
                pilaOperadores.push(simbolo);
                continue;
            }
            
            if (simbolo.equals(")")) {
                do {
                    token = pilaOperadores.pop();
                    if (!token.equals("(")) {
                        postEval.append(token);
                        postEval.append(" ");
                    }
                } while (!token.equals("("));
                continue;
            }
            
            if (esOperador(simbolo.trim())) {
                while (!pilaOperadores.empty()) {
                    token = pilaOperadores.peek();
                    if (precedencia(token) < precedencia(simbolo)) {
                        postEval.append(pilaOperadores.pop());
                        postEval.append(" ");
                    } else {
                        break;
                    }
                }
                pilaOperadores.push(simbolo);
                continue;
            }
            postEval.append(simbolo);
            postEval.append(" ");
        }
        
        while (!pilaOperadores.isEmpty()) {
            postEval.append(pilaOperadores.pop());
            postEval.append(" ");
        }
        
        return postEval.toString();
    }
    
    public static double getResult(String eval) {
        double result = 0.0;
        
        Stack<Double> pila = new Stack<Double>();
        
        StringTokenizer st = new StringTokenizer(eval, "()+-*/", true);
        
        while (st.hasMoreTokens()) {
            String simbolo = st.nextToken().trim();
            
            if (simbolo.isEmpty()) {
                continue;
            }
            
            if (simbolo.equals("*")
                    ||simbolo.equals("/")
                    ||simbolo.equals("+")
                    ||simbolo.equals("-")) {
                double o2 = pila.pop();
                double o1 = pila.pop();
                
                pila.push(calcular(o1, o2, simbolo));
            } else {
                Double value = Double.parseDouble(simbolo);
                pila.push(value);
            }
        }
        if (!pila.empty()) {
            result = pila.pop();
        }
        return result;
    }
    
    public static double calcular(double o1, double o2, String operacion) {
        double resultado = 0.0;
        
        switch (operacion) {
            case "*":
                resultado = o1 * o2;
                break;
            case "/":
                resultado = o1 / o2;
                break;
            case "+":
                resultado = o1 + o2;
                break;
            case "-":
                resultado = o1 - o2;
                break;
        }
        return resultado;
    }
    
    public static Boolean esOperador(String token) {
        return "*/+-".contains(token);
    }
    
    public static int precedencia(String token) {
        int precedencia = 100;
        
        if (token.equals("+") || token.equals("-")) {
            precedencia = 2;
        }
        if (token.equals("*") || token.equals("/")) {
            precedencia = 1;
        }
        return precedencia;
    }
}
