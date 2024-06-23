import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lab1 extends JFrame {
    /* GUI elements */
    private JSlider     slider1;
    private JPanel      panel;
    private JLabel      prior1Label;
    private JSpinner    spinner1;
    private JLabel      priority2Label;
    private JSpinner    spinner2;
    private JPanel      panelA;
    private JPanel      panelB;
    private JSeparator  tasksSepar;
    private JButton     startThreadsButton;
    private JButton     buttonStart1;
    private JButton     buttonStart2;
    private JButton     buttonStop1;
    private JButton     buttonStop2;

    /* Threads elements */

    private Thread  puller1;
    private Thread  puller2;
    private int     semaphore;
    private Thread setThread(int num){
        if(num == 1) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    while (slider1.getValue() != 10 && semaphore == 1) {
                        try {
                            if (slider1.getValue() < 10) {
                                slider1.setValue(slider1.getValue() + 1);
                            } else if (slider1.getValue() > 10) {
                                slider1.setValue(slider1.getValue() - 1);
                            }
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            System.out.println("puller1 interrupted");
                            break;
                        }
                    }
                    enableControlButtons();
                    semaphore = 0;
                }
            });
        } else if (num == 2) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    while (slider1.getValue() != 90 && semaphore == 1) {
                        try {
                            if (slider1.getValue() < 90) {
                                slider1.setValue(slider1.getValue() + 1);
                            } else if (slider1.getValue() > 90) {
                                slider1.setValue(slider1.getValue() - 1);
                            }
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            System.out.println("puller2 interrupted");
                            break;
                        }
                    }
                    enableControlButtons();
                    semaphore = 0;
                }
            });
        }
        return null;
    }

    private void enableControlButtons() {
        startThreadsButton.setEnabled(true);
        buttonStart1.setEnabled(true);
        buttonStart2.setEnabled(true);
        buttonStop1.setEnabled(true);
        buttonStop2.setEnabled(true);
    }

    private void disablePanelB(){
        buttonStart1.setEnabled(false);
        buttonStart2.setEnabled(false);
        buttonStop1.setEnabled(false);
        buttonStop2.setEnabled(false);
    }

    private void ThreadStarter(Thread th, int num, int priority){
        th = setThread(num);
        th.setDaemon(true);
        th.setPriority(priority);
        semaphore = 1;
        th.start();
    }

    public Lab1() {
        //GUI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setContentPane(panel);
        SpinnerModel priorities1 = new SpinnerNumberModel(5, 1, 10, 1);
        SpinnerModel priorities2 = new SpinnerNumberModel(5, 1, 10, 1);
        spinner1.setModel(priorities1);
        spinner2.setModel(priorities2);
        semaphore = 0;
        //Threads
        puller1 = setThread(1);
        puller2 = setThread(2);
        startThreadsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disablePanelB();
                ThreadStarter(puller1, 1, (Integer) spinner1.getValue());
                ThreadStarter(puller2, 2, (Integer) spinner2.getValue());
                System.out.println("puller1 started with priority " + puller1.getPriority());
                System.out.println("puller2 started with priority " + puller2.getPriority());
            }
        });
        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                puller1.setPriority((Integer) spinner1.getValue());
                System.out.println("puller1's new priority is " + puller1.getPriority());
            }
        });
        spinner2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                puller2.setPriority((Integer) spinner2.getValue());
                System.out.println("puller2's new priority is " + puller2.getPriority());
            }
        });
        JFrame cur = this;
        buttonStart1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(semaphore == 1)
                {
                    JOptionPane.showMessageDialog(cur, "Зайнято потоком", "Увага", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                startThreadsButton.setEnabled(false);
                ThreadStarter(puller1, 1, Thread.MIN_PRIORITY);
                buttonStop2.setEnabled(false);
            }
        });
        buttonStart2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(semaphore == 1)
                {
                    JOptionPane.showMessageDialog(cur, "Зайнято потоком", "Увага", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                startThreadsButton.setEnabled(false);
                ThreadStarter(puller2, 2, Thread.MAX_PRIORITY);
                buttonStop1.setEnabled(false);
            }
        });
        buttonStop1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                semaphore = 0;
                buttonStop2.setEnabled(true);
            }
        });
        buttonStop2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                semaphore = 0;
                buttonStop1.setEnabled(true);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Lab1();
    }
}
