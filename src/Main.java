import Integration.IntegrationMain;
import Iterations.IterationsMain;
import QR.QRMain;
import Rotate.RotateMain;
import TDMA.TDMAMain;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n=== МЕТОД ПРОГОНКИ ===");
        TDMAMain.main(args);

        System.out.println("\n=== МЕТОД ИТЕРАЦИЙ + ЗЕЙДЕЛЯ ===");
        IterationsMain.main(args);

        System.out.println("\n=== МЕТОД ВРАЩЕНИЯ ===");
        RotateMain.main(args);

        System.out.println("\n=== QR ===");
        QRMain.main(args);

        System.out.println("\n=== ЧИСЛЕННОЕ ИНТЕГРИРОВАНИЕ ===");
        IntegrationMain.main(args);
    }
}