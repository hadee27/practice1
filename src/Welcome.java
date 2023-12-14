import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame implements ActionListener {
    JLabel label;
    ImageIcon icon,image;
    JButton button;
    Welcome(){
        button = new JButton();
        button.setBounds(350,600,100,50);
        button.setText("Start");
        button.setBackground(Color.black);
        button.setForeground(Color.white);
        button.setFocusable(false);
        button.setVisible(true);
        button.addActionListener(this);



        label = new JLabel();//create a label
        label.setText("WELCOME TO PROJECT MANAGEMENT APP");//set text of label
        icon = new ImageIcon("R.png");//icon for the label
        label.setHorizontalTextPosition(JLabel.CENTER);//set text Left/center/right of the imageicon
        label.setVerticalTextPosition(JLabel.TOP);//set text top,center/bottom of the
        label.setIcon(icon);
        label.setFont(new Font("MV Boli",Font.PLAIN,20));//set font of text
        label.setBackground(Color.gray);//set background color
        label.setOpaque(true);//display background color
        label.setVerticalAlignment(JLabel.CENTER);//align the icon and text within the label
        label.setHorizontalAlignment(JLabel.CENTER);




        Border border = BorderFactory.createLineBorder(Color.cyan,3);
        label.setBorder(border);
//        label.setBounds(250,250,300,300);
        label.add(button);

        image = new ImageIcon("icon1.jpg");//create an imageicon
        this.setIconImage(image.getImage());//change icon of the frame
        this.setTitle("Project Manager");//title of the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//exit out of the frame
        this.setSize(800,800);//set the size of the frame
        this.setResizable(false);
//        this.setLayout(null);
        this.add(label);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(123,50,250));//change color of the background
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            new ProjectManagementApp();
            Welcome.this.dispose();
        }
    }

    public static void main(String[] args) {
        new ProjectManager();
    }

}
