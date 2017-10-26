package logging;

public class Log {
    public static void that(String... args) {
        StringBuilder sb = new StringBuilder("===LOG---: ");
        for (String arg : args) {
            sb.append(arg);
        }
        System.out.println(sb.toString());
    }
}
