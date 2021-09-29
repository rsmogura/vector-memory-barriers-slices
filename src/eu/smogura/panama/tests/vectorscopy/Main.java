package eu.smogura.panama.tests.vectorscopy;

import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.SequenceLayout;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {
    private static final VectorSpecies<Byte> SPECIES_BYTE = VectorSpecies.ofLargestShape(byte.class);
    private static final VectorSpecies<Integer> SPECIES_INTEGER = VectorSpecies.ofLargestShape(int.class);

    private static volatile int size = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        ByteBuffer heapIn = ByteBuffer.allocate(size);
        ByteBuffer heapOu = ByteBuffer.allocate(size);

        ByteBuffer directIn = ByteBuffer.allocateDirect(size);
        ByteBuffer directOu = ByteBuffer.allocateDirect(size);

//        Stream<Path> walk = Files.walk(Path.of("/home/rado"));
//        walk.forEach(p -> {});
        for (int i=0; i < 30_000; i++) {
//            copyMemoryBytes(heapIn, heapOu, heapOu, heapIn.array());
//            copyMemoryBytes(directIn, directOu, directOu, null);
            test2(heapIn, heapOu, heapOu, heapOu.array());
            test2(directIn, directOu, directOu, heapOu.array());
            test3(heapIn, heapOu, heapOu, heapOu.array());
            test3(directIn, directOu, directOu, heapOu.array());

//            copyMemoryBytes3(heapIn, heapOu, heapOu, heapOu.array());
//            copyMemoryBytes3(directIn, directOu, directOu, heapOu.array());
//
//            copyMemoryBytes4(heapIn, heapOu, heapOu, heapOu.array());
//            copyMemoryBytes4(directIn, directOu, directOu, heapOu.array());
//            copyMemoryBytes2(heapIn.array(), heapOu.array());
        }
    }

    public static final VectorSpecies<Byte> SPECIES = VectorSpecies.ofLargestShape(byte.class);

//    public static void main(String[] args) {
//        for (int i = 0; i < 30000; i++) {
//            if (test() != 1) {
//                throw new AssertionError();
//            }
//        }
//    }

    public static int test() {
        byte arr[] = new byte[256];
        final var bb = ByteBuffer.wrap(arr);
        final var ones = ByteVector.broadcast(SPECIES, 1);
        var res = ByteVector.zero(SPECIES);

        int result = 0;
        result += arr[2];
        res.add(ones).intoByteBuffer(bb, 0, ByteOrder.nativeOrder());
        result += arr[2];

        return result;
    }

    public static void copyMemoryBytes(ByteBuffer in, ByteBuffer out, ByteBuffer out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.limit()); i += SPECIES_BYTE.vectorByteSize()) {
            ByteVector.fromByteBuffer(SPECIES_BYTE, in, i, ByteOrder.nativeOrder())
                    .intoByteBuffer(out, i, ByteOrder.nativeOrder());
//            if (arr != null) {
//                k += i + arr[i];
//                arr[i] = 1;
//            }
            ByteVector.fromByteBuffer(SPECIES_BYTE, in, i, ByteOrder.nativeOrder())
                    .intoByteBuffer(out, i, ByteOrder.nativeOrder());
        }
    }

    static int k;

    public static void copyMemoryBytes2(byte[] in, byte[] out) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.length); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromByteArray(SPECIES_BYTE, in, i, ByteOrder.nativeOrder());
            k++;
            var v2 = ByteVector.fromByteArray(SPECIES_BYTE, in, i, ByteOrder.nativeOrder());

            ByteVector.fromByteArray(SPECIES_BYTE, in, i, ByteOrder.nativeOrder())
                    .intoByteArray(out, i, ByteOrder.nativeOrder());
        }
    }

    public static void copyMemoryBytes3(ByteBuffer in, ByteBuffer out, ByteBuffer out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.limit()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromByteBuffer(SPECIES_BYTE, in, 0, ByteOrder.nativeOrder());
            arr[i] = 0;
//            k += arr[i];
            v1.intoByteBuffer(out, 0, ByteOrder.nativeOrder());
//            k += arr[i];
        }
    }

    public static void copyMemoryBytes4(ByteBuffer in, ByteBuffer out, ByteBuffer out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.limit()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromByteBuffer(SPECIES_BYTE, in, 0, ByteOrder.nativeOrder());
            var v2 = IntVector.fromByteBuffer(SPECIES_INTEGER, in, 0, ByteOrder.nativeOrder());

//            k += arr[i];
            v1.intoByteBuffer(out, 0, ByteOrder.nativeOrder());
            v2.intoByteBuffer(out, 0, ByteOrder.nativeOrder());
//            k += arr[i];
        }
    }
    public static int test2(ByteBuffer in, ByteBuffer out, ByteBuffer out2, byte[] arr) {
//        for (int i = 0; i < SPECIES_BYTE.loopBound(in.limit()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromByteBuffer(SPECIES_BYTE, in, 0, ByteOrder.nativeOrder());
            arr[0] = (byte) 0;
            v1.intoByteBuffer(out, 0, ByteOrder.nativeOrder());
//        }

        return 0;
    }

    public static int test3(ByteBuffer in, ByteBuffer out, ByteBuffer out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.limit()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromByteBuffer(SPECIES_BYTE, in, i, ByteOrder.nativeOrder());
            arr[i] = (byte) 0;
            v1.intoByteBuffer(out, i, ByteOrder.nativeOrder());
        }

        return 0;
    }
}