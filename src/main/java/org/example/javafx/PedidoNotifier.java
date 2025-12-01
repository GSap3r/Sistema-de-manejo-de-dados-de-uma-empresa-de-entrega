package org.example.javafx;

import java.util.ArrayList;
import java.util.List;

public class PedidoNotifier {
    private static final List<Runnable> listeners = new ArrayList<>();

    public static void addListener(Runnable r) {
        synchronized (listeners) {
            listeners.add(r);
        }
    }

    public static void removeListener(Runnable r) {
        synchronized (listeners) {
            listeners.remove(r);
        }
    }

    public static void notifyListeners() {
        List<Runnable> copy;
        synchronized (listeners) {
            copy = new ArrayList<>(listeners);
        }
        for (Runnable r : copy) {
            try { r.run(); } catch (Exception ignored) {}
        }
    }
}
