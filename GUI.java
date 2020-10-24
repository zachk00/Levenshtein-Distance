import com.sun.org.apache.xpath.internal.objects.XObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class GUI implements ActionListener {

    private static JFrame frame;
    private static JPanel panel;
    private static JLabel from;
    private static JTextField fromInput;
    private static JLabel to;
    private static JTextField toInput;
    private static JButton enter;
    private static JLabel result;
    private static JButton showPath;
    private static JTextArea steps;
    private static boolean textLoaded;



    private final static String newline = "\n";




    private static Tool tool = new Tool();
    public static void main(String[] args) {

        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Levenshtein Distance");
        frame.add(panel);

        panel.setLayout(null);
        panel.setBackground(Color.ORANGE);

        from = new JLabel("Original");
        from.setBounds(10, 20, 80, 25);
        panel.add(from);

        fromInput = new JTextField(20);
        fromInput.setBounds(100, 20, 165, 25);
        panel.add(fromInput);

        to = new JLabel("Final");
        to.setBounds(10, 50, 80, 25);
        panel.add(to);

        toInput = new JTextField(20);
        toInput.setBounds(100, 50, 165, 25);
        panel.add(toInput);

        enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enter.addActionListener(new GUI());
        panel.add(enter);

        result = new JLabel("");
        result.setBounds(10, 110, 200, 25);
        panel.add(result);

        showPath = new JButton("Show Steps");
        showPath.setBounds(10, 140, 120, 25);
        showPath.addActionListener(new GUI());
        panel.add(showPath);


        steps = new JTextArea();
        steps.setBounds(140, 140,250,200);
        steps.setEditable(false);
        steps.setBackground(Color.LIGHT_GRAY);
        panel.add(steps);

        frame.setVisible(true);

    }


  @Override
    public void actionPerformed(ActionEvent e) {
        textLoaded = !fromInput.getText().isEmpty() && !toInput.getText().isEmpty();
        if(e.getSource() == enter){
            String originalString = fromInput.getText();
            String finalString = toInput.getText();
            String answer = "";
            tool.editDistance(originalString, finalString);
            answer = String.valueOf(tool.getResult());
            result.setText("The minimum edit distance is: " + answer);

        }
        else if(e.getSource() == showPath && textLoaded){
            LinkedList<String> editSteps = tool.getOrderOfOperations();
            steps.setText("");
            for(String step : editSteps){
                if(!step.equalsIgnoreCase("Nothing")){
                    steps.append(step + newline);
                }
            }

        }

    }





}
