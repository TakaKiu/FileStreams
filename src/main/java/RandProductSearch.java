import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RandProductSearch extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;

    public RandProductSearch() {

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Partial Product Name:"));
        panel.add(searchField);
        panel.add(searchButton);

        JPanel resultPanel = new JPanel();
        resultPanel.add(new JLabel("Search Results:"));
        resultPanel.add(new JScrollPane(resultArea));


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(panel);
        this.add(resultPanel);
        this.pack();
        this.setVisible(true);
    }

    private void searchProducts() {
        String partialName = searchField.getText().trim();
        System.out.println("Searching for partial name: " + partialName);

        List<Product> matchingProducts = searchInRandomAccessFile(partialName);

        displaySearchResults(matchingProducts);
    }

    private List<Product> searchInRandomAccessFile(String partialName) {
        List<Product> matchingProducts = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile("products.dat", "r")) {
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                try {
                    String record = randomAccessFile.readUTF();
                    System.out.println("Read record: " + record);
                    Product product = parseProductRecord(record);

                    // Debugging print statements
                    if (product != null && product.getName() != null) {
                        System.out.println("Product name: " + product.getName());
                        System.out.println("Comparing: " + product.getName() + " with " + partialName);
                    } else {
                        System.out.println("Product is null or has a null name. Skipping comparison.");
                        continue;
                    }

                    if (product.getName().contains(partialName)) {
                        matchingProducts.add(product);
                    }
                } catch (EOFException e) {

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return matchingProducts;
    }

    private Product parseProductRecord(String record) {


        if (record.length() < 116) {

            System.out.println("Invalid record length: " + record.length());
            return null;
        }

        try {
            String name = record.substring(0, 35).trim();
            String description = record.substring(35, 110).trim();


            String ID = record.substring(110, 116).trim();

            double cost = Double.parseDouble(record.substring(116).trim());

            return new Product(name, description, ID, cost);
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Error parsing record: " + record);
            return null;
        }
    }

    private void displaySearchResults(List<Product> matchingProducts) {
        resultArea.setText("");

        if (matchingProducts.isEmpty()) {
            resultArea.append("No matching products found.");
        } else {
            for (Product product : matchingProducts) {
                resultArea.append(product.getName() + "\n");
                System.out.println("Found matching product: " + product.getName());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandProductSearch();
            }
        });
    }
}
