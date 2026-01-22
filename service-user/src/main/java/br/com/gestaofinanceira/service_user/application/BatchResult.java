package br.com.gestaofinanceira.service_user.application;

public class BatchResult {
    private final int success;
    private final int failed;

    public BatchResult(int success, int failed) {
        this.success = success;
        this.failed = failed;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailed() {
        return failed;
    }
}
