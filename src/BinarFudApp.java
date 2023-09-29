import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class FoodItem {
    private String name;
    private double price;
    private int quantity;

    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}

public class BinarFudApp {
    private static DecimalFormat df = new DecimalFormat("#.###");
    private static List<FoodItem> menu = new ArrayList<>();
    private static List<FoodItem> order = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeMenu();
        boolean exit = false;

        while (!exit) {
            displayMainMenu();

            int choice = getUserChoice(0, 99);
            switch (choice) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    orderFood(choice);
                    break;
                case 99:
                    exit = confirmOrderAndPayment();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Input tidak valid. Silakan coba lagi.");
            }
        }
    }

    private static void initializeMenu() {
        menu.add(new FoodItem("Nasi Goreng", 15.0));
        menu.add(new FoodItem("Mie Goreng", 13.0));
        menu.add(new FoodItem("Nasi + Ayam", 18.0));
        menu.add(new FoodItem("Es Teh Manis", 3.0));
        menu.add(new FoodItem("Es Jeruk", 5.0));
    }

    private static void displayMainMenu() {
        System.out.println("=======================");
        System.out.println("Selamat datang di BinarFud");
        System.out.println("=======================");
        System.out.println("Silahkan pilih makanan :");
        for (int i = 0; i < menu.size(); i++) {
            FoodItem item = menu.get(i);
            System.out.println(i + 1 + ". " + item.getName() + " | " + df.format(item.getPrice()));
        }
        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar aplikasi");
    }

    private static int getUserChoice(int min, int max) {
        int choice = -1;
        while (true) {
            try {
                System.out.print("Pilihan Anda: ");
                choice = scanner.nextInt();
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Input tidak valid. Masukkan angka antara " + min + " dan " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                scanner.next();
            }
        }
    }

    private static void orderFood(int foodIndex) {
        FoodItem selectedFood = menu.get(foodIndex - 1);
        System.out.println("=======================");
        System.out.println("Berapa pesanan anda?");
        System.out.println("=======================");
        System.out.println(selectedFood.getName() + " | " + df.format(selectedFood.getPrice()));
        System.out.print("qty => ");

        int quantity = getUserChoice(0, Integer.MAX_VALUE);
        if (quantity > 0) {
            selectedFood.setQuantity(selectedFood.getQuantity() + quantity);
            System.out.println("Pesanan ditambahkan.");
        } else {
            System.out.println("Pesan minimal 1 pesanan.");
        }
    }

    private static boolean confirmOrderAndPayment() {
        double total = 0;
        System.out.println("=======================");
        System.out.println("Konfirmasi & Pembayaran");
        System.out.println("=======================");

        for (FoodItem item : menu) {
            int quantity = item.getQuantity();
            if (quantity > 0) {
                System.out.println(item.getName() + "\t" + quantity + "\t" + df.format(item.getTotalPrice()));
                total += item.getTotalPrice();
            }
        }

        System.out.println("------------------------------------------ +");
        System.out.println("Total\t\t" + df.format(total));

        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");

        int choice = getUserChoice(0, 2);
        switch (choice) {
            case 1:
                generateReceipt(total);
                clearOrder();
                return false;
            case 2:
                clearOrder();
                return false;
            default:
                return true;
        }
    }

    private static void generateReceipt(double total) {
        try {
            FileWriter writer = new FileWriter("receipt.txt");
            writer.write("=======================\n");
            writer.write("BinarFud\n");
            writer.write("=======================\n");
            writer.write("Terima kasih sudah memesan\n");
            writer.write("di BinarFud\n");
            writer.write("\n");
            writer.write("Dibawah ini adalah pesanan anda\n");
            writer.write("\n");

            for (FoodItem item : menu) {
                int quantity = item.getQuantity();
                if (quantity > 0) {
                    writer.write(item.getName() + "\t" + quantity + "\t" + df.format(item.getTotalPrice()) + "\n");
                }
            }

            writer.write("------------------------------------------ +\n");
            writer.write("Total\t\t" + df.format(total) + "\n");
            writer.write("\n");
            writer.write("Pembayaran : BinarCash\n");
            writer.write("=======================\n");
            writer.write("Simpan struk ini sebagai\n");
            writer.write("bukti pembayaran\n");
            writer.close();
            System.out.println("Struk pembayaran telah disimpan.");
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan struk pembayaran.");
        }
    }

    private static void clearOrder() {
        for (FoodItem item : menu) {
            item.setQuantity(0);
        }
    }
}
