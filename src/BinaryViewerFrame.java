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
   //파일 경로는 모든 클래스가 참조해야 하기때문에 멤버변수로 선언
   JTextField path;
   //파일 내용을 헥스값으로 출력해주기 위해 멤버변수로 선언
   JTextArea hex_area;
   
   //생성자
   public BinaryViewerFrame() {
      setTitle("Binary Viewer");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      Container contentPane = getContentPane();
      //헥스값과 아스키값을 나타낼 텍스트 에어리어 생성
      hex_area = new JTextArea(37, 150);
      //파일 선택 후 파일 경로가 입력될 텍스트 필드 생성
      path = new JTextField("선택된 파일이 없습니다.", 89);
      //텍스트 필드 수정 불가
      path.setEditable(false);
      path.setHorizontalAlignment(JTextField.CENTER);
      //텍스트 필드 글꼴 설정
      path.setFont(new Font("굴림체", Font.BOLD, 15));
      hex_area.setFont(new Font("굴림체", Font.PLAIN, 12));
      //파일 탐색기를 호출할 버튼 생성
      JButton open = new JButton("파일 열기");
      //open 버튼에 대한 액션
      open.addActionListener(new OpenActionListener());
      //수정 버튼 생성
      JButton modify = new JButton("수정");
      modify.addActionListener(new ModifyActionListener());
      //contentPane.setBackground(Color.BLACK);
      contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
      //컨테이너에 open 버튼 추가
      contentPane.add(open);
      //컨테이너에 경로 나타낼 텍스트 필드 추가
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
         System.out.println(filePath + "에서 읽지 못했습니다. 경로명을 확인하세요.");
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
         chooser.setDialogTitle("파일 불러오기");
         FileNameExtensionFilter filter = new FileNameExtensionFilter("text file", "txt");
         chooser.setFileFilter(filter);
         
         int ret  = chooser.showOpenDialog(null);
         
         if(ret != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "경로를 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
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


