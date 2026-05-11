import Integration.IntegrationMain;
import Iterations.IterationsMain;
import NonlinearEquations.NonlinearEquationsMain;
import NonlinearSystems.NonlinearSystemsMain;
import ODE.ODEMain;
import QR.QRMain;
import Rotate.RotateMain;
import TDMA.TDMAMain;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n=== РЕШЕНИЕ НЕЛИНЕЙНЫХ УРАВНЕНИЙ ===");
        NonlinearEquationsMain.main(args);

        System.out.println("\n=== РЕШЕНИЕ СИСТЕМ НЕЛИНЕЙНЫХ УРАВНЕНИЙ ===");
        NonlinearSystemsMain.main(args);

        System.out.println("\n=== РЕШЕНИЕ ОДУ (ЗАДАЧА КОШИ) ===");
        ODEMain.main(args);
    }
}