import org.apache.xmlrpc.WebServer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Inet4Address;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Klasa z metodą main do odpalenia serwera
 */
public class RunMathRPCServer {


    /**
     * Klasa serwera z funkcjonalnością
     */
    public static class MathRPCServer {


        /**
         * Metoda zaokrąglająca liczbę
         * @param number liczba do zaokrąglenia
         * @param precision precyzja z jaką zaokrąglamy
         * @return zaokrąglona liczba
         */
        public double round(double number, int precision) {
            double p = 1;
            for (int i = 0; i < precision; i++) {
                p *= 10;
            }
            return (double) Math.round(number * p) / p;
        }

        /**
         * Metoda obliczająca srednia z logarytmów
         * @param typeOflog typ logarytmu
         * @param range liczba liczonych logarytmów
         * @param timeout czas oczekiwania wykonania metody
         * @return srednia logarytmów
         */
        public double log(String typeOflog, int range, int timeout) {
            try {
                Thread.sleep(timeout * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double avg = 0;
            System.out.println(typeOflog);
            switch (typeOflog) {
                case "lg2":
                    for (int i = 1; i <= range; i++) {
                        avg += Math.log(i) / Math.log(2);
                    }
                    break;
                case "lg10":
                    for (int i = 1; i <= range; i++) {
                        avg += Math.log10(i);
                    }
                    break;
                case "ln":
                    for (int i = 1; i <= range; i++) {
                        avg += Math.log(i);
                    }
                    break;
                default:
                    return -500;
            }
            avg /= (double) range;
            return avg;
        }

        /**
         * Metoda obliczająca liczbe pi metodą monte carlo
         * @param numThrows liczba prób
         * @return obliczona liczba pi
         */
        public String pi(int numThrows) {
            int inCircle= 0;
            for(int i= 0;i < numThrows;i++){
                //a square with a side of length 2 centered at 0 has
                //x and y range of -1 to 1
                double randX= (Math.random() * 2) - 1;//range -1 to 1
                double randY= (Math.random() * 2) - 1;//range -1 to 1
                //distance from (0,0) = sqrt((x-0)^2+(y-0)^2)
                double dist= Math.sqrt(randX * randX + randY * randY);
                //^ or in Java 1.5+: double dist= Math.hypot(randX, randY);
                if(dist < 1){//circle with diameter of 2 has radius of 1
                    inCircle++;
                }
            }
            double result = 4.0 * inCircle / numThrows;
            DecimalFormat df2 = new DecimalFormat(".####");
            return df2.format(result);
        }

        /**
         * Metoda obliczająca potęge
         * @param a podstawa potęgi
         * @param b wykładnik potęgi
         * @return obliczona potęga
         */
        public double pow(double a, double b) {
            return Math.pow(a, b);
        }

        /**
         * Metoda obliczająca nty wyraz ciagu fibonaciego
         * @param n element ciagu, który chcemy obliczyć
         * @return obliczony element
         */
        public int fib(int n) {
            if (n == 0) return 0;
            else if (n == 1) return 1;
            else return fib(n - 1) + fib(n - 2);
        }

        /**
         * Metoda pokazująca dostępne metody z serwera
         * @return tekst z dostepnymi metodami
         */
        public String show() {
            StringBuilder show = new StringBuilder("Mozesz wywolac nastepujace metody:");
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method1 : methods) {
                show.append("\nMetoda: ").append(method1.getName());
                Parameter[] methodParameters = method1.getParameters();
                if (methodParameters.length != 0) {
                    show.append(" Parametry: ");
                    for (Parameter parameter : methodParameters) {
                        show.append(parameter.getName() + " typu: " + parameter.getType().getName() + " ");
                    }
                }
            }
            return show.toString();
        }
    }

    /**
     *  Metoda uruchamiająca serwer
     */
    public static void main(String[] args) {
//        double lg2 = MathRPCServer.log("ln", 10, 2);
//        System.out.println(lg2);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj adres (np. localhost lub 192.168.43.178) serwera oraz port\nAdres: ");
        String address = scanner.nextLine();
        System.out.print("Port: ");
        int serverPort = scanner.nextInt();
        try {
            System.out.println("Startuje serwer XML-RPC...");
            WebServer server = new WebServer(serverPort, Inet4Address.getByName(address));
            server.addHandler("mathServer", new MathRPCServer());
            server.start();
            System.out.println("Serwer wystartował pomyślnie.");
            System.out.println("Nasłuchuje na porcie: " + serverPort);
            System.out.println("Aby zatrzymać serwer nacisnij crl+c");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Serwer XML-RPC: " + e);
        }
    }
}
