import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Shaders");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Paint paint = new Paint();
        paint.startTread();
        paint.setFocusable(true);
        frame.addWindowListener(new ClosingWindowsListener(paint.thread));

        frame.add(paint);
        frame.setVisible(true);
        frame.setResizable(true);

        paint.setRunning(true);
        paint.run();
    }

}