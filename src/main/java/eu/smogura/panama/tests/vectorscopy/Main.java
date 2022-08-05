package eu.smogura.panama.tests.vectorscopy;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.nio.ByteOrder;

public class Main {
    private static final VectorSpecies<Byte> SPECIES_BYTE = VectorSpecies.ofLargestShape(byte.class);
    private static final VectorSpecies<Integer> SPECIES_INTEGER = VectorSpecies.ofLargestShape(int.class);

    private static volatile int size = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        var session = MemorySession.openConfined();
        MemorySegment heapIn = MemorySegment.ofArray(new byte[size]);
        MemorySegment heapOu = MemorySegment.ofArray(new byte[size]);

        MemorySegment directIn = MemorySegment.allocateNative(size, session);
        MemorySegment directOu = MemorySegment.allocateNative(size, session);

//        Stream<Path> walk = Files.walk(Path.of("/home/rado"));
//        walk.forEach(p -> {});
        for (int i=0; i < 30_000; i++) {
//            copyMemoryBytes(heapIn, heapOu, heapOu, heapIn.array());
//            copyMemoryBytes(directIn, directOu, directOu, null);
//            test2(heapIn, heapOu, heapOu, (byte[]) heapOu.array().get());
//            test2(directIn, directOu, directOu, (byte[]) heapOu.array().get());
//            test3(heapIn, heapOu, heapOu, (byte[]) heapOu.array().get());
            test3(directIn, directOu, directOu, (byte[]) heapOu.array().get());

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

//    public static int test() {
//        byte arr[] = new byte[256];
//        final var bb = ByteBuffer.wrap(arr);
//        final var ones = ByteVector.broadcast(SPECIES, 1);
//        var res = ByteVector.zero(SPECIES);
//
//        int result = 0;
//        result += arr[2];
//        res.add(ones).intoMemorySegment(bb, 0, ByteOrder.nativeOrder());
//        result += arr[2];
//
//        return result;
//    }

    public static void copyMemoryBytes(MemorySegment in, MemorySegment out, MemorySegment out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.byteSize()); i += SPECIES_BYTE.vectorByteSize()) {
            ByteVector.fromMemorySegment(SPECIES_BYTE, in, i, ByteOrder.nativeOrder())
                    .intoMemorySegment(out, i, ByteOrder.nativeOrder());
//            if (arr != null) {
//                k += i + arr[i];
//                arr[i] = 1;
//            }
            ByteVector.fromMemorySegment(SPECIES_BYTE, in, i, ByteOrder.nativeOrder())
                    .intoMemorySegment(out, i, ByteOrder.nativeOrder());
        }
    }

    static int k;

    public static void copyMemoryBytes2(byte[] in, byte[] out) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.length); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromArray(SPECIES_BYTE, in, i);
            k++;
            var v2 = ByteVector.fromArray(SPECIES_BYTE, in, i);

            ByteVector.fromArray(SPECIES_BYTE, in, i)
                    .intoArray(out, i);
        }
    }

    public static void copyMemoryBytes3(MemorySegment in, MemorySegment out, MemorySegment out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.byteSize()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromMemorySegment(SPECIES_BYTE, in, 0, ByteOrder.nativeOrder());
            arr[i] = 0;
//            k += arr[i];
            v1.intoMemorySegment(out, 0, ByteOrder.nativeOrder());
//            k += arr[i];
        }
    }

    public static void copyMemoryBytes4(MemorySegment in, MemorySegment out, MemorySegment out2, byte[] arr) {
        for (int i = 0; i < SPECIES_BYTE.loopBound(in.byteSize()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromMemorySegment(SPECIES_BYTE, in, 0, ByteOrder.nativeOrder());
            var v2 = IntVector.fromMemorySegment(SPECIES_INTEGER, in, 0, ByteOrder.nativeOrder());

//            k += arr[i];
            v1.intoMemorySegment(out, 0, ByteOrder.nativeOrder());
            v2.intoMemorySegment(out, 0, ByteOrder.nativeOrder());
//            k += arr[i];
        }
    }
    public static int test2(MemorySegment in, MemorySegment out, MemorySegment out2, byte[] arr) {
//        for (int i = 0; i < SPECIES_BYTE.loopBound(in.limit()); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromMemorySegment(SPECIES_BYTE, in, 0, ByteOrder.nativeOrder());
            arr[0] = (byte) 0;
            v1.intoMemorySegment(out, 0, ByteOrder.nativeOrder());
//        }

        return 0;
    }

    public static int test3(MemorySegment in, MemorySegment out, MemorySegment out2, byte[] arr) {
        long sz = in.byteSize();
        for (long i = 0; i < SPECIES_BYTE.loopBound(sz); i += SPECIES_BYTE.vectorByteSize()) {
            var v1 = ByteVector.fromMemorySegment(SPECIES_BYTE, in, i, ByteOrder.nativeOrder());
//            arr[i] = (byte) 0;
            v1.intoMemorySegment(out, i, ByteOrder.nativeOrder());
        }

        return 0;
    }
}