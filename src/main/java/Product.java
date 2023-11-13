import java.io.Serializable;
import java.util.Collection;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String ID;
    private double cost;

    public Product(String text, String text1, String text2, double v) {
    }

    public String formatForRandomAccess() {
        return String.format("%-35s%-75s%-6s%-8.2f", name, description, ID, cost);
    }

    public Collection<Object> getName() {
        return null;
    }
}
