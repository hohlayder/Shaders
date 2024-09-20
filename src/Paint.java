import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

import static java.lang.Math.*;
import static java.lang.Math.min;


public class Paint extends JPanel implements Runnable, MouseListener, KeyListener {

    public static int width, height, diagonal;
    long start_time, prev_time, new_time, fps_time;
    int step = 1, frames;
    boolean first_launch = true;


    Color[] colors;
    String s;
    float fps = 30;

    double[] palette(double t) {
        double[] a = new double[]{0.5, 0.5, 0.5};
        double[] b = new double[]{0.5, 0.5, 0.5};
        double[] c = new double[]{0.69, 0.69, 0.69};
        double[] d = new double[]{-0.453, -0.273, -0.363};
        double[] res = new double[3];
        for (int i = 0; i < 3; i++) {
            res[i] = a[i] + b[i] * cos(2 * Math.PI * (c[i] * t + d[i]));
        }
        return res;
    }

    double[] getColor(double[] xy) {
        double x = xy[0];
        double y = xy[1];
        double x0 = x;
        double y0 = y;
        double d, d0, time;
        double[] color;
        double[] res_color = new double[]{0d, 0d, 0d};
        for(int i = 0; i < 3; i++) {
            x *= 1.5;
            y *= 1.5;
            x = x - floor(x) - 0.5;
            y = y - floor(y) - 0.5;
            d = hypot(x, y);
            d0 = hypot(x0, y0);
            d *= sin(d0/2);
            time = new_time / 1000d;
            color = palette(d0 + i + time);
            d = sin(d * 8. - time) / 8.;
            d = abs(d);

            d = 0.02 / d;
            d = pow(d, 1.5);
            for (int j = 0; j < 3; j++) {
                res_color[j] += color[j] * d / (i+1);
            }
        }
        return new double[]{res_color[0], res_color[1], res_color[2], 1.0};
    }

    Color formColor(double[] color) {
        return new Color(formComp(color[0]), formComp(color[1]), formComp(color[2]), formComp(color[3]));
    }

    int formComp(double value) {
        return (int) max(0, min(255, value * 255));
    }

    double[] getXY(double x, double y) {
        double value = min(width, height);
        y = height - y;
        x = x - width / 2d;
        y = y - height / 2d;
        return new double[]{2 * x / value, 2 * y / value};
    }
    public Thread thread;

    public static boolean running = false;

    private int delay = 10;

    public Paint() {
        setDoubleBuffered(true);
        addMouseListener(this);
        this.addKeyListener(this);
    }
    public void startTread() {
        thread = new Thread(this);
        thread.start();
    }

    public void paint(Graphics g) {
        if (first_launch) {
            fps_time = start_time = prev_time = new_time = System.currentTimeMillis();
            first_launch = false;
        }
        super.paint(g);
        new_time = System.currentTimeMillis();
        BufferedImage image = new BufferedImage(width / step, height / step, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width / step; x++) {
            for (int y = 0; y < height / step; y++) {
                image.setRGB(x, y, formColor(getColor(getXY(x * step, y * step))).getRGB());
                //colors[x + y * width] = Color.argb(255, 255, 255, (int)( 255 * sin(new_time - start_time)));
            }
        }
        g.drawImage(image, 0, 0, width, height, null);
        prev_time = new_time;
        new_time = System.currentTimeMillis();
        frames++;
        if (new_time - fps_time > 1000) {
            fps = frames * 1000f / (new_time - fps_time);
            frames = 0;
            fps_time = new_time;
            if (fps < 10) {
                step++;
            }
            if (fps > 60 && step > 1) {
                step--;
            }
        }
        s = String.valueOf(fps);
        s = s.substring(0, min(s.indexOf('.') + 3, s.length()));
        g.setFont(new Font(null, Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString(s + " FPS " + step + " STEPS", 10, 50);
    }

    public void updateView() {
        width = getWidth();
        height = getHeight();
        diagonal = (int) Math.hypot(height, width);
    }

    @Override
    public void run() {
        while (running) {
            updateView();
            this.repaint();
            try {
                thread.sleep(delay);
            } catch (InterruptedException e) {
                running = false;
                throw new RuntimeException(e);
            }

        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}