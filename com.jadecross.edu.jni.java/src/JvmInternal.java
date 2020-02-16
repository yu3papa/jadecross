
public class JvmInternal {
    static int cv = 0;
    final static int fcv = 100;

    public static void main(String[] args) {
        int a, b, c;
        a = Integer.parseInt(args[0]);
        b = Integer.parseInt(args[1]);
        c = addTwoArgs(a, b);
    }

    static int addTwoArgs(int x, int y) {
        cv = fcv;
        return (x + y);
    }
}
