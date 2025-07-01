package toolchain.vm.vmlistener;

public interface VMListener {
    void onCycleCompleted();
    void onProgramFinished();
    void onProgramDataInitialized();
    void onOutputProduced(String message);
}
