package instructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

/** Runs controls on the JavaFX application thread without showing a window. */
final class FxTestSupport {
    private static boolean started;

    private FxTestSupport() {
    }

    static synchronized void startToolkit() throws InterruptedException {
        if (!started) {
            CountDownLatch ready = new CountDownLatch(1);
            Platform.startup(() -> {
                Platform.setImplicitExit(false);
                ready.countDown();
            });
            if (!ready.await(20, TimeUnit.SECONDS)) {
                throw new AssertionError("JavaFX did not start");
            }
            started = true;
        }
    }

    static void run(CheckedRunnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(() -> {
            action.run();
            return null;
        });
        Platform.runLater(task);
        try {
            task.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException exception) {
            if (exception.getCause() instanceof Error error) {
                throw error;
            }
            if (exception.getCause() instanceof Exception cause) {
                throw cause;
            }
            throw exception;
        }
    }

    @FunctionalInterface
    interface CheckedRunnable {
        void run() throws Exception;
    }
}
