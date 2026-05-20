package com.blasphemous2.trainer;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;

/**
 * Core memory manipulation trainer for Blasphemous 2.
 * Uses JNA to interact with Windows process memory.
 */
public class MemoryTrainer {

    private static final int PROCESS_VM_READ = 0x0010;
    private static final int PROCESS_VM_WRITE = 0x0020;
    private static final int PROCESS_VM_OPERATION = 0x0008;

    private HANDLE processHandle;
    private final int pid;
    private final Kernel32 kernel32;

    /**
     * Creates a trainer attached to a given process ID.
     * @param pid The process ID of the Blasphemous 2 game.
     */
    public MemoryTrainer(int pid) {
        this.pid = pid;
        this.kernel32 = Kernel32.INSTANCE;
        openProcess();
    }

    private void openProcess() {
        int access = PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION;
        processHandle = kernel32.OpenProcess(access, false, pid);
        if (processHandle == null || !processHandle.isValid()) {
            throw new RuntimeException("Failed to open process with PID: " + pid +
                    ". Error: " + Native.getLastError());
        }
    }

    /**
     * Reads an integer value from a memory address.
     * @param address The memory address to read from.
     * @return The integer value at that address.
     */
    public int readInt(long address) {
        byte[] buffer = new byte[4];
        IntByReference bytesRead = new IntByReference();
        boolean success = kernel32.ReadProcessMemory(processHandle,
                new Pointer(address), buffer, 4, bytesRead);
        if (!success || bytesRead.getValue() != 4) {
            throw new RuntimeException("Failed to read memory at 0x" +
                    Long.toHexString(address));
        }
        return (buffer[0] & 0xFF) | ((buffer[1] & 0xFF) << 8) |
               ((buffer[2] & 0xFF) << 16) | ((buffer[3] & 0xFF) << 24);
    }

    /**
     * Writes an integer value to a memory address.
     * @param address The memory address to write to.
     * @param value The integer value to write.
     */
    public void writeInt(long address, int value) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (value & 0xFF);
        buffer[1] = (byte) ((value >> 8) & 0xFF);
        buffer[2] = (byte) ((value >> 16) & 0xFF);
        buffer[3] = (byte) ((value >> 24) & 0xFF);
        IntByReference bytesWritten = new IntByReference();
        boolean success = kernel32.WriteProcessMemory(processHandle,
                new Pointer(address), buffer, 4, bytesWritten);
        if (!success || bytesWritten.getValue() != 4) {
            throw new RuntimeException("Failed to write memory at 0x" +
                    Long.toHexString(address));
        }
    }

    /**
     * Closes the process handle to release resources.
     */
    public void close() {
        if (processHandle != null && processHandle.isValid()) {
            kernel32.CloseHandle(processHandle);
            processHandle = null;
        }
    }

    /**
     * Gets the current process ID.
     * @return The PID of the target process.
     */
    public int getPid() {
        return pid;
    }
}
