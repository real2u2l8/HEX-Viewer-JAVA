import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BinaryViewerFrame extends JFrame{
   //���� ��δ� ��� Ŭ������ �����ؾ� �ϱ⶧���� ��������� ����
   JTextField path;
   //���� ������ �������� ������ֱ� ���� ��������� ����
   JTextArea hex_area;
   
   //������
   public BinaryViewerFrame() {
      setTitle("Binary Viewer");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      Container contentPane = getContentPane();
      //������ �ƽ�Ű���� ��Ÿ�� �ؽ�Ʈ ����� ����
      hex_area = new JTextArea(37, 150);
      //���� ���� �� ���� ��ΰ� �Էµ� �ؽ�Ʈ �ʵ� ����
      path = new JTextField("���õ� ������ �����ϴ�.", 89);
      //�ؽ�Ʈ �ʵ� ���� �Ұ�
      path.setEditable(false);
      path.setHorizontalAlignment(JTextField.CENTER);
      //�ؽ�Ʈ �ʵ� �۲� ����
      path.setFont(new Font("����ü", Font.BOLD, 15));
      hex_area.setFont(new Font("����ü", Font.PLAIN, 12));
      //���� Ž���⸦ ȣ���� ��ư ����
      JButton open = new JButton("���� ����");
      //open ��ư�� ���� �׼�
      open.addActionListener(new OpenActionListener());
      //���� ��ư ����
      JButton modify = new JButton("����");
      modify.addActionListener(new ModifyActionListener());
      //contentPane.setBackground(Color.BLACK);
      contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
      //�����̳ʿ� open ��ư �߰�
      contentPane.add(open);
      //�����̳ʿ� ��� ��Ÿ�� �ؽ�Ʈ �ʵ� �߰�
      contentPane.add(path);
      contentPane.add(new JScrollPane(hex_area));
      contentPane.add(modify);
      
      setSize(980, 700);
      setResizable(false);
      setVisible(true);
   }
   
   void changeFilePath(String filePath) {
      path.setText(filePath);
      readFile(filePath);
   }
   
   void readFile(String filePath) {
      byte data[] = new byte[16];
      
      try {
         FileInputStream fin = null;
         fin = new FileInputStream(filePath);
         int i = 0;
         int c;
         int number = 0;
         
         while((c = fin.read()) != -1) {   
            if(i == 0) {
               hex_area.append(String.format("  0x%08x", number));
               hex_area.append("            ");
               number = number + 16;
            }
            
            data[i++] = (byte)c;
            
            if((i != 0) && (i % 16 == 0)) {
               for(int j = 0; j < data.length; j++) {
                  hex_area.append(String.format("%02X  ", data[j]));
               }
               
               hex_area.append("            ");
               
               for(int j = 0; j < data.length; j++) {
                  hex_area.append(String.format("%c.", data[j]));
               }
               
               hex_area.append("\n");
               
               for(int j = 0; j < data.length; j++) {
                  data[j] = 0;
               }
               
               i = 0;
            }
         }
         
         for(i = 0; i < data.length; i++) {
            hex_area.append(String.format("%02X  ", data[i]));
         }
         
         hex_area.append("            ");
         
         for(int j = 0; j < data.length; j++) {
            hex_area.append(String.format("%c.", data[j]));
         }
         
         fin.close();
      }
      catch(IOException e) {
         System.out.println(filePath + "���� ���� ���߽��ϴ�. ��θ��� Ȯ���ϼ���.");
      }
   }
   
      public static void main(String[] args) {
      BinaryViewerFrame binaryViewer = new BinaryViewerFrame();
   }
   
   class OpenActionListener implements ActionListener {
      public String filePath = "";
      
      public void actionPerformed(ActionEvent e) {
         hex_area.setText("");
         
         JFileChooser chooser = new JFileChooser();
         chooser.setDialogTitle("���� �ҷ�����");
         FileNameExtensionFilter filter = new FileNameExtensionFilter("text file", "txt");
         chooser.setFileFilter(filter);
         
         int ret  = chooser.showOpenDialog(null);
         
         if(ret != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "��θ� �������� �ʾҽ��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
            return;
         }
         else {
            this.filePath = chooser.getSelectedFile().getPath();
            changeFilePath(filePath);
         }
      }
   }
   
   class ModifyActionListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         String full_data = "";
         full_data = hex_area.getText();
         full_data = full_data + '\0';
         hex_area.setText("");
         int n = 0;
         
         while(n < full_data.length()) {
            String split_data = "";
            System.out.println(full_data);
            
            for(int i = 0; i < 133; i++) {
               split_data = split_data + String.format("%c", full_data.charAt(n));
               n = n + 1;
            }
            
            String split_arr[] = split_data.split("            ");
            
            split_arr[1] = "";
            for (int i = 0; i < 32; i+=2) {
               split_arr[1] = split_arr[1] + String.format("%02X  ", (int)split_arr[2].charAt(i+2));
            }
            
            split_arr[2] = split_arr[2].trim();
            
            String out_data = "";
            for(int i = 0; i < 3; i++) {
               out_data = out_data + split_arr[i];
               if(i != 2) out_data = out_data + "            ";
            }
            
            hex_area.append(out_data + "\n");
         }
         
         System.out.println(full_data.length());
      }
   }
}


