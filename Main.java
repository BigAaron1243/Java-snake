import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.LinkedList;
import java.util.TimerTask;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {

        SnakeDaemon jp = new SnakeDaemon();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                jp.updateAll();
                if (jp.dead) {
                    this.cancel();
                }
            }
        }, 100, 100);
    }
}

 class SnakeDaemon extends JPanel {
    
    LinkedList<Segment> drawList;
    Segment head = new Segment(3, 3, 10);
    Segment dir = new Segment(1, 0, 0);
    Segment fruit = new Segment(-1, -1, 0);
    JFrame frame = new JFrame();
    boolean dead = false;

    SnakeDaemon() {
        drawList = new LinkedList<Segment>();
        drawList.add(head);
        head = new Segment(head.x + 1, head.y, head.age);
        this.setPreferredSize(new Dimension(500,500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(new KeyDaemon(this));
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < drawList.size(); i++) {
            if (drawList.get(i).age > 0) {
                g2.fillRect((drawList.get(i).x * 20) + 1, (drawList.get(i).y * 20) + 1, 18, 18);
            }
            g2.drawString("head: " + head.x + ", " + head.y + " | dir: " + dir.x + ", " + dir.y, 10, 10);
            g2.drawString("next: " + drawList.getLast().x + ", " + drawList.getLast().y, 10, 22);
        }
        if (dead) {
            g2.setColor(Color.RED);
            g2.drawString("You Died.", 224, 246);
        }
        g2.fillRect((head.x * 20) + 1, (head.y * 20) + 1, 18, 18);
        g2.setColor(new Color(255, 160, 119));
        if (fruit.age == 1) {
            g2.fillRect((fruit.x * 20) + 1, (fruit.y * 20) + 1, 18, 18);
        }
    }

    public void updateAll() {
        frame.repaint();
        if (fruit.age == 0) {
            boolean fruitbreaker = true;
            while (fruitbreaker) {
                fruit = new Segment((int)(Math.random() * 25), (int)(Math.random() * 25), 1);
                fruitbreaker = false;
                for (int i = 0; i < drawList.size(); i++) {
                    if (fruit.x == drawList.get(i).x && fruit.y == drawList.get(i).y) {
                        fruitbreaker = true;
                    }
                }
            }
        }
        Segment newhead = head;
        head = new Segment(head.x + dir.x, head.y + dir.y, head.age);
        drawList.add(newhead);
        for (int i = 0; i < drawList.size(); i++) {
            drawList.get(i).age -= 1;
            if (drawList.get(i).age <= 0) {
                drawList.remove(i);
            }
            if (head.x == drawList.get(i).x && head.y == drawList.get(i).y) {
                dead = true;
            }
        }
        if (head.x > 24 || head.y > 24 || head.x < 0 || head.y < 0) {
            dead = true;
        }
        if (head.x == fruit.x && head.y == fruit.y) {
            fruit.age = 0;
            head.age += 10;
        }
    }

    public void dirMan(char c) {
        if (c == 'w' && head.y != drawList.getLast().y + 1) {
            dir.x = 0;
            dir.y = -1;
        } else if (c == 'a' && head.x != drawList.getLast().x + 1) {
            dir.x = -1;
            dir.y = 0;
        } else if (c == 's' && head.y != drawList.getLast().y - 1) {
            dir.x = 0;
            dir.y = 1;
        } else if (c == 'd' && head.x != drawList.getLast().x - 1) {
            dir.x = 1;
            dir.y = 0;
        }
    }
}

 class Segment {
    int x, y, age;

    Segment(int inputx, int inputy, int inputage) {
        x = inputx;
        y = inputy;
        age = inputage;
    }
}

 class KeyDaemon implements KeyListener {
    SnakeDaemon jpD;
    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        jpD.dirMan(Character.toLowerCase(e.getKeyChar()));
    }
    public void keyReleased(KeyEvent e) {

    }
    public KeyDaemon(SnakeDaemon jpDREF) {
        jpD = jpDREF;
    }
}

