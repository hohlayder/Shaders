import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClosingWindowsListener extends WindowAdapter {
    private Thread thread;

    public ClosingWindowsListener(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        thread.interrupt();
        Paint.running = false;
    }
}