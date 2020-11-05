


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class GUI implements ActionListener, ItemListener {

    private static JFrame frame;
    private static JPanel panel;
    private static JLabel from;
    private static JTextField fromInput;
    private static JTextField toInput;
    private static JButton enter;
    private static JLabel result;
    private static JButton showPath;
    private static JTextArea steps;
    private static JCheckBox nothing;
    private static boolean showNothing = false;
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

        JLabel to = new JLabel("Final");
        to.setBounds(10, 50, 80, 25);
        panel.add(to);

        toInput = new JTextField(20);
        toInput.setBounds(100, 50, 165, 25);
        panel.add(toInput);

        enter = new JButton("Enter");
        enter.setBounds(10, 80, 120, 25);
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


        nothing = new JCheckBox("Show 'Nothing' Steps");

        nothing.setBounds(300, 20, 165, 20);
        nothing.addItemListener(new GUI());
        panel.add(nothing);

        frame.setVisible(true);

    }


  @Override
    public void actionPerformed(ActionEvent e) {
      boolean textLoaded = !fromInput.getText().isEmpty() || !toInput.getText().isEmpty();
        if(e.getSource() == enter){
            steps.setText("");
            String originalString = fromInput.getText();
            String finalString = toInput.getText();
            String answer = "";
            tool.editDistance(originalString, finalString);
            answer = String.valueOf(tool.getResult());
            result.setText("The minimum edit distance is: " + answer);

        }
        else if(e.getSource() == showPath && textLoaded){

            steps.setText("");

            for(int i = tool.getOrderOfOperations().size() - 1; i >= 0; i--){

                if(!tool.getOrderOfOperations().get(i).equalsIgnoreCase("nothing")&& !showNothing){
                   steps.append(tool.getOrderOfOperations().get(i) + newline);
                }
                else if (showNothing){
                    steps.append(tool.getOrderOfOperations().get(i) + newline);
                }
            }

        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == 1){
            showNothing = true;
        }
        else{
            showNothing = false;
        }
    }
}

