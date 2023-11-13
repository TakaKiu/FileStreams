import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame {
    private JTextField nameTextField;
    private JTextField descriptionTextField;
    private JTextField idTextField;
    private JTextField costTextField;
    private JTextField recordCountTextField;
    private JButton addButton;

    private int recordCount;

    public RandProductMaker() {

        nameTextField = new JTextField(20);
        descriptionTextField = new JTextField(40);
        idTextField = new JTextField(10);
        costTextField = new JTextField(10);
        recordCountTextField = new JTextField(10);
        addButton = new JButton("Add");

        recordCount = 0;
        recordCountTextField.setText(String.valueOf(recordCount));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });


        JPanel mainPanel = new JPanel(new GridLayout(6, 2));
        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(nameTextField);
        mainPanel.add(new JLabel("Description:"));
        mainPanel.add(descriptionTextField);
        mainPanel.add(new JLabel("ID:"));
        mainPanel.add(idTextField);
        mainPanel.add(new JLabel("Cost:"));
        mainPanel.add(costTextField);
        mainPanel.add(new JLabel("Record Count:"));
        mainPanel.add(recordCountTextField);
        mainPanel.add(addButton);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addButtonActionPerformed(ActionEvent evt) {
        String name = nameTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        String id = idTextField.getText().trim();
        double cost;

        try {
            cost = Double.parseDouble(costTextField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid cost input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.isEmpty() || description.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String formattedName = String.format("%-35s", name);
        String formattedDescription = String.format("%-75s", description);
        String formattedId = String.format("%-10s", id);

        String productRecord = formattedName + formattedDescription + formattedId + String.format("%-10.2f", cost);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile("products.dat", "rw")) {
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.writeUTF(productRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }

        recordCount++;
        recordCountTextField.setText(String.valueOf(recordCount));

        nameTextField.setText("");
        descriptionTextField.setText("");
        idTextField.setText("");
        costTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandProductMaker();
            }
        });
    }
}
